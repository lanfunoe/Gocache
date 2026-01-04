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
import com.lanfunoe.gocache.repository.AlbumSongRepository;
import com.lanfunoe.gocache.repository.ArtistRepository;
import com.lanfunoe.gocache.repository.ArtistSongRepository;
import com.lanfunoe.gocache.repository.DailyRecommendRepository;
import com.lanfunoe.gocache.repository.SongQualityRepository;
import com.lanfunoe.gocache.repository.SongRepository;
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

@Slf4j
@Service
public class DailyRecommendCacheService {

    /**
     * 刷新数据库所需的数据持有记录
     */
    private record RefreshData(
        List<DailyRecommend> dailyRecommends,
        List<Song> songs,
        List<Album> albums,
        List<AlbumSong> albumSongs,
        List<Artist> artists,
        List<ArtistSong> artistSongs,
        List<SongQuality> songQualities
    ) {}

    @Resource
    private ReactiveCacheService caffeineService;
    @Resource
    private DailyRecommendRepository dailyRecommendRepository;
    @Resource
    private SongRepository songRepository;
    @Resource
    private DailyRecommendConverter converter;
    @Resource
    private AlbumRepository albumRepository;
    @Resource
    private AlbumSongRepository albumSongRepository;
    @Resource
    private ArtistRepository artistRepository;
    @Resource
    private ArtistSongRepository artistSongRepository;
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
        String cacheKey = userId + ":" + date;
        // L1 缓存检查（存储完整响应）
        return caffeineService.getIfPresent(CacheNames.EVERYDAY_RECOMMEND, cacheKey, EverydayRecommendResponse.class)
                .switchIfEmpty(getFromDb(date, userId, cacheKey).doOnSuccess(response -> caffeineService.put(CacheNames.EVERYDAY_RECOMMEND, cacheKey, response)))
                .switchIfEmpty(refreshAll(loader, cacheKey, date, userId));
    }


    private Mono<EverydayRecommendResponse> getFromDb(String date, String userId, String cacheKey) {
        return dailyRecommendRepository.findByUserIdAndRecommendDate(userId, date)
                .collectList()
                .flatMap(dailyRecommends -> buildResponseFromDailyRecommends(dailyRecommends, date, cacheKey))
                .onErrorResume(e -> {
                    log.error("Failed to load daily recommendations from database for user {} date {}", userId, date, e);
                    return Mono.empty();
                });
    }

    private Mono<EverydayRecommendResponse> buildResponseFromDailyRecommends(List<DailyRecommend> dailyRecommends, String date, String cacheKey) {
        if (dailyRecommends.size() != 30) {
            return Mono.empty();
        }
        Map<SongHashId, String> songParams = dailyRecommends.stream()
                .collect(Collectors.toMap(
                        dr -> SongHashId.of(dr.getAudioId(), dr.getSongHash()),
                        DailyRecommend::getOriAudioName
                ));
        return songRepository.findAllByIds(songParams.keySet())
                .collectList()
                .flatMap(songs -> {
                    if (songs == null || songs.size() != dailyRecommends.size()) {
                        return Mono.empty();
                    }
                    log.info("Database HIT: {}:{}", CacheNames.EVERYDAY_RECOMMEND, cacheKey);
                    return Mono.just(buildResponse(songs, songParams, date));
                });
    }

    private EverydayRecommendResponse buildResponse(List<Song> songs, Map<SongHashId, String> recommendMap, String date) {
        List<EverydayRecommendResponse.SongItem> songItems = songs.stream()
                .filter(song -> recommendMap.containsKey(SongHashId.of(song)))
                .map(song -> converter.toSongItem(song, recommendMap.get(SongHashId.of(song))))
                .toList();

        return EverydayRecommendResponse.create(date, songItems);
    }

    private Mono<EverydayRecommendResponse> refreshAll(Supplier<Mono<EverydayRecommendResponse>> loader, String cacheKey, String date, String userId) {
        return loader.get().flatMap(response -> {
            caffeineService.put(CacheNames.EVERYDAY_RECOMMEND, cacheKey, response);
            refreshDatabase(response, date, userId);
            return Mono.just(response);
        });
    }

    /**
     * 保存到Database缓存（PostgreSQL）
     * 保存歌曲、专辑、歌手、音质版本等所有关联数据
     */
    private void refreshDatabase(EverydayRecommendResponse response, String date, String userId) {
        if (response == null || response.songList() == null || response.songList().isEmpty()) {
            log.warn("Empty everyday recommend response, skip saving to Database");
        }
        Mono.fromCallable(() -> prepareRefreshData(response, date, userId))
                .flatMap(data -> {
                    log.info("Saving to Database: {}:{}", CacheNames.EVERYDAY_RECOMMEND, date);
                    return Mono.when(
                            dailyRecommendRepository.upsert(data.dailyRecommends()),
                            songRepository.upsert(data.songs()),
                            albumRepository.upsert(data.albums()),
                            albumSongRepository.upsert(data.albumSongs()),
                            artistRepository.upsert(data.artists()),
                            artistSongRepository.upsert(data.artistSongs()),
                            songQualityRepository.upsert(data.songQualities())
                    );
                })
                .subscribeOn(Schedulers.parallel())
                .onErrorResume(e -> {
                    log.error("Failed to save everyday recommend to Database", e);
                    return Mono.empty();
                })
                .subscribe();
    }

    /**
     * 准备刷新数据库所需的数据
     */
    private RefreshData prepareRefreshData(EverydayRecommendResponse response, String date, String userId) {
        List<DailyRecommend> dailyRecommends = converter.toDailyRecommendList(date, userId, response.songList());
        List<Song> songs = converter.toSongList(response.songList());
        List<Album> albums = converter.toAlbumList(response.songList());
        List<AlbumSong> albumSongs = converter.toAlbumSongList(response.songList());
        List<Artist> artists = converter.toArtistList(response.songList());
        List<ArtistSong> artistSongs = converter.toAllArtistSongList(response.songList());
        List<SongQuality> songQualities = converter.toAllSongQualityList(response.songList());

        return new RefreshData(dailyRecommends, songs, albums, albumSongs, artists, artistSongs, songQualities);
    }
}