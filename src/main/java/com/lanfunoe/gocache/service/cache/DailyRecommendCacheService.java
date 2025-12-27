package com.lanfunoe.gocache.service.cache;

import com.lanfunoe.gocache.dto.EverydayRecommendResponse;
import com.lanfunoe.gocache.model.Album;
import com.lanfunoe.gocache.model.AlbumSong;
import com.lanfunoe.gocache.model.Artist;
import com.lanfunoe.gocache.model.ArtistSong;
import com.lanfunoe.gocache.model.DailyRecommend;
import com.lanfunoe.gocache.model.Song;
import com.lanfunoe.gocache.model.SongHashId;
import com.lanfunoe.gocache.model.SongQuality;
import com.lanfunoe.gocache.repository.AlbumRepository;
import com.lanfunoe.gocache.repository.ArtistRepository;
import com.lanfunoe.gocache.repository.DailyRecommendRepository;
import com.lanfunoe.gocache.repository.SongQualityRepository;
import com.lanfunoe.gocache.repository.SongRepository;
import com.lanfunoe.gocache.repository.impl.AlbumSongRepositoryImpl;
import com.lanfunoe.gocache.repository.impl.ArtistSongRepositoryImpl;
import com.lanfunoe.gocache.service.data.DailyRecommendConverter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 每日推荐缓存服务
 * 负责三级缓存编排（L1 Caffeine → L2 SQLite → API Loader）
 */
@Slf4j
@Service
public class DailyRecommendCacheService {

    @Resource
    private ReactiveCacheService caffeineCacheService;
    @Resource
    private DailyRecommendRepository dailyRecommendRepository;
    @Resource
    private SongRepository songRepository;
    @Resource
    private DailyRecommendConverter converter;

    // 新增：关联数据的 Repository
    @Resource
    private AlbumRepository albumRepository;
    @Resource
    private AlbumSongRepositoryImpl albumSongRepository;
    @Resource
    private ArtistRepository artistRepository;
    @Resource
    private ArtistSongRepositoryImpl artistSongRepository;
    @Resource
    private SongQualityRepository songQualityRepository;

    /**
     * 获取每日推荐（完整缓存逻辑）
     *
     * @param date      日期 YYYY-MM-DD
     * @param userId    用户ID
     * @param loader API加载器（缓存未命中时调用）
     * @return 每日推荐响应
     */
    public Mono<EverydayRecommendResponse> get(String date, String userId, Supplier<Mono<EverydayRecommendResponse>> loader) {
        String cacheKey = CacheNames.KEY_PREFIX_EVERYDAY + date;

        // L1 缓存检查（存储完整响应）
        return caffeineCacheService.getIfPresent(CacheNames.EVERYDAY_RECOMMEND, cacheKey, EverydayRecommendResponse.class)
                .switchIfEmpty(Mono.defer(() ->
                        // L1未命中，检查L2
                        checkL2CacheAndRefresh(date, userId, cacheKey, loader)
                ));
    }

    /**
     * 检查L2缓存并在未命中时刷新
     */
    private Mono<EverydayRecommendResponse> checkL2CacheAndRefresh(String date, String userId, String cacheKey, Supplier<Mono<EverydayRecommendResponse>> loader) {
        return dailyRecommendRepository.findByUserIdAndDate(userId, date)
                .collectList()
                .flatMap(dailyRecommends -> {
                    if (dailyRecommends.isEmpty()) {
                        return Mono.empty();
                    }
                    return rebuildResponseFromL2(dailyRecommends, cacheKey, date);
                })
                .switchIfEmpty(Mono.defer(() -> refreshCache(loader, cacheKey, date, userId)));
    }

    private Mono<EverydayRecommendResponse> rebuildResponseFromL2(List<DailyRecommend> dailyRecommends, String cacheKey, String date) {
        Map<SongHashId, String> recommendMap = dailyRecommends.stream()
                .collect(Collectors.toMap(
                        dr -> SongHashId.of(dr.getAudioId(), dr.getSongHash()),
                        DailyRecommend::getOriAudioName
                ));


        return songRepository.findAllByIds(recommendMap.keySet())
                .collectList()
                .flatMap(songs -> {
                    if (songs == null || songs.size() != recommendMap.size()) {
                        return Mono.empty();
                    }
                    EverydayRecommendResponse response = buildResponse(songs, recommendMap, date);
                    log.info("L2 Cache HIT: {}:{}", CacheNames.EVERYDAY_RECOMMEND, cacheKey);
                    return refreshL1Cache(cacheKey, response).thenReturn(response);
                });
    }

    private EverydayRecommendResponse buildResponse(List<Song> songs, Map<SongHashId, String> recommendMap, String date) {
        List<EverydayRecommendResponse.SongItem> songItems = songs.stream()
                .filter(song -> recommendMap.containsKey(SongHashId.of(song)))
                .map(song -> converter.toSongItem(song, recommendMap.get(SongHashId.of(song))))
                .toList();

        return EverydayRecommendResponse.create(date, songItems);
    }

    /**
     * 从API加载并刷新所有缓存层级
     */
    private Mono<EverydayRecommendResponse> refreshCache(Supplier<Mono<EverydayRecommendResponse>> loader, String cacheKey, String date, String userId) {
        return loader.get().flatMap(response -> {
            // 执行后台缓存和标准化任务，但不阻塞主流程
            Mono<Void> backgroundTask = Mono.when(
                    refreshL1Cache(cacheKey, response),
                    saveToL2Cache(response, date, userId)
            ).subscribeOn(Schedulers.boundedElastic())
                    .onErrorResume(e -> {
                        log.error("Failed to refresh everyday recommend cache", e);
                        return Mono.empty();
                    });

            backgroundTask.subscribe();
            return Mono.just(response);
        });
    }

    /**
     * 刷新L1缓存
     */
    private Mono<Void> refreshL1Cache(String cacheKey, EverydayRecommendResponse response) {
        return caffeineCacheService.put(CacheNames.EVERYDAY_RECOMMEND, cacheKey, response)
                .onErrorResume(e -> {
                    log.error("Failed to cache everyday recommend to L1", e);
                    return Mono.empty();
                });
    }

    /**
     * 保存到L2缓存（SQLite）
     * 保存歌曲、专辑、歌手、音质版本等所有关联数据
     */
    private Mono<Void> saveToL2Cache(EverydayRecommendResponse response, String date, String userId) {
        if (response == null || response.songList() == null || response.songList().isEmpty()) {
            log.warn("Empty everyday recommend response, skip saving to L2");
            return Mono.empty();
        }

        return Mono.fromCallable(() -> {
            List<DailyRecommend> dailyRecommends = converter.toDailyRecommendList(date, userId, response.songList());
            List<Song> songs = converter.toSongList(response.songList());
            List<Album> albums = converter.toAlbumList(response.songList());
            List<AlbumSong> albumSongs = converter.toAlbumSongList(response.songList());
            List<Artist> artists = converter.toArtistList(response.songList());
            List<ArtistSong> artistSongs = converter.toAllArtistSongList(response.songList());
            List<SongQuality> songQualities = converter.toAllSongQualityList(response.songList());
            Mono.when(
                dailyRecommendRepository.upsert(dailyRecommends).then(),
                songRepository.upsert(songs).then(),
                albumRepository.upsert(albums).then(),
                albumSongRepository.upsert(albumSongs).then(),//
                artistRepository.upsert(artists).then(),
                artistSongRepository.upsert(artistSongs).then(),
                songQualityRepository.upsert(songQualities).then()//
            ).subscribeOn(Schedulers.boundedElastic())
             .subscribe();
            return null;
        }).subscribeOn(Schedulers.boundedElastic()).then()
        .onErrorResume(e -> {
            log.error("Failed to save everyday recommend to L2", e);
            return Mono.empty();
        });
    }
}