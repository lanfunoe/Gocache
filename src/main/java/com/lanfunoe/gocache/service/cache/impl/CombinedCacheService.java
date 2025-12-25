package com.lanfunoe.gocache.service.cache.impl;

import com.lanfunoe.gocache.dto.TagResponse;
import com.lanfunoe.gocache.model.EverydayRecommend;
import com.lanfunoe.gocache.model.Lyric;
import com.lanfunoe.gocache.model.Singer;
import com.lanfunoe.gocache.model.Song;
import com.lanfunoe.gocache.model.Tag;
import com.lanfunoe.gocache.repository.EverydayRecommendRepository;
import com.lanfunoe.gocache.repository.LyricRepository;
import com.lanfunoe.gocache.repository.SingerRepository;
import com.lanfunoe.gocache.repository.SongRepository;
import com.lanfunoe.gocache.repository.TagRepository;
import com.lanfunoe.gocache.service.cache.CacheNames;
import com.lanfunoe.gocache.service.cache.EntityCacheService;
import com.lanfunoe.gocache.service.cache.ReactiveCacheService;
import com.lanfunoe.gocache.service.data.DataNormalizationService;
import com.lanfunoe.gocache.service.data.SongDataExtractor;
import com.lanfunoe.gocache.service.data.TagDataConverter;
import com.lanfunoe.gocache.service.storage.DownloadTaskQueue;
import com.lanfunoe.gocache.service.storage.MediaStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 组合缓存服务
 * 整合L1(Caffeine内存缓存)、L2(Repository持久化)和L3(文件存储)
 *
 * 重构说明：
 * - 移除了 SqliteCacheServiceImpl 的依赖
 * - L2 缓存现在通过各个 Repository 直接访问专门的数据表
 * - 实现了 EntityCacheService 接口，提供基于实体的缓存方法
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CombinedCacheService implements EntityCacheService {

    private static final long L2_CACHE_TTL_MILLIS = 7 * 24 * 60 * 60 * 1000L; // 7天

    private final ReactiveCacheService caffeineCacheService;
    private final MediaStorageService mediaStorageService;
    private final DownloadTaskQueue downloadTaskQueue;
    private final EverydayRecommendRepository everydayRecommendRepository;
    private final LyricRepository lyricRepository;
    private final SingerRepository singerRepository;
    private final SongRepository songRepository;
    private final TagRepository tagRepository;
    private final DataNormalizationService dataNormalizationService;
    private final SongDataExtractor songDataExtractor;
    private final TagDataConverter tagDataConverter;

    /**
     * 检查L2缓存数据是否仍在有效期内（7天内）
     */
     private boolean isL2CacheValid(List<Tag> tags) {
        if (tags.isEmpty()) {
            return false;
        }

        // 使用最新的更新时间戳
        long latestUpdatedAt = tags.stream()
                .mapToLong(tag -> tag.getUpdatedAt() != null ? tag.getUpdatedAt() : 0)
                .max()
                .orElse(0);

        if (latestUpdatedAt == 0) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        return (currentTime - latestUpdatedAt) < L2_CACHE_TTL_MILLIS;
    }


    /**
     * 构建每日推荐完整响应
     */
    private Map<String, Object> buildEverydayResponse(EverydayRecommend meta, List<Song> songs) {
        Map<String, Object> response = new HashMap<>();
        response.put("creation_date", meta.getCreationDate());
        response.put("mid", meta.getMid());
        response.put("bi_biz", meta.getBiBiz());
        response.put("sign", meta.getSign());
        response.put("song_list_size", songs.size());
        response.put("song_list", songs);
        response.put("sub_title", meta.getSubTitle());
        response.put("cover_img_url", meta.getCoverImgUrl());
        response.put("client_playlist_flag", meta.getClientPlaylistFlag());
        response.put("is_guarantee_rec", meta.getIsGuaranteeRec());
        return response;
    }


    @Override
    public Mono<List<TagResponse>> getAllTags(Supplier<Mono<List<TagResponse>>> loader, boolean staleWhileRevalidate) {
        String cacheKey = CacheNames.KEY_PREFIX_TAGS + "all";

        // L1 缓存检查 (24小时过期，由Caffeine自动管理)
        return caffeineCacheService.getIfPresent(CacheNames.TAGS, cacheKey, List.class)
                .cast(List.class)
                .map(list -> (List<TagResponse>) list)
                .switchIfEmpty(Mono.defer(() -> {
                    // L1未命中，检查L2缓存
                    return tagRepository.findAll().collectList().flatMap(tagsL2 -> {
                        // 检查L2数据是否仍在有效期内（7天内）
                        if (tagsL2.isEmpty()) {
                            return Mono.empty();
                        }
                        boolean l2Valid = isL2CacheValid(tagsL2);
                        List<TagResponse> tagsResponseL2 = tagDataConverter.convertToTagsResponse(tagsL2);
                        if (l2Valid) {
                            return caffeineCacheService.put(CacheNames.TAGS, cacheKey, tagsResponseL2).thenReturn(tagsResponseL2);
                        } else if (staleWhileRevalidate) {
                            refreshTagsCache(loader, cacheKey).subscribeOn(Schedulers.boundedElastic()).subscribe();
                            return Mono.just(tagsResponseL2);
                        }
                        return Mono.empty();
                    }).switchIfEmpty(Mono.defer(() -> refreshTagsCache(loader, cacheKey)));
                }));
    }

    private Mono<List<TagResponse>> refreshTagsCache(Supplier<Mono<List<TagResponse>>> loader, String cacheKey) {
        return Mono.defer(loader)
                .flatMap(tagsResponse -> {
                    Mono<Void> cachePutTask = caffeineCacheService.put(CacheNames.TAGS, cacheKey, tagsResponse)
                            .doOnError(e -> log.error("Failed to cache tags for key: {}", cacheKey, e))
                            .onErrorResume(e -> Mono.empty());

                    // 转换 TagResponse 到 Tag 实体列表并保存到数据库
                    List<Tag> tagsToSave = tagDataConverter.convertToTags(tagsResponse);
                    Mono<Void> dbSaveTask = tagRepository.saveAll(tagsToSave).then()
                            .doOnError(e -> log.error("Failed to save {} tags to database for key: {}",
                                    tagsToSave.size(), cacheKey, e));

                    // 执行后台缓存和数据库保存任务
                    Mono.when(cachePutTask, dbSaveTask)
                            .doOnError(e -> log.error("Failed to complete background cache tasks for key: {}", cacheKey, e))
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe();

                    return Mono.just(tagsResponse);
                })
                .doOnError(error -> log.error("Error during tags refresh for key: {}", cacheKey, error));
    }




    @Override
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> getEverydayRecommend(String date, Supplier<Mono<Map<String, Object>>> loader) {
        String cacheKey = CacheNames.KEY_PREFIX_EVERYDAY + date;

        // L1 缓存检查（存储完整响应）
        return caffeineCacheService.getIfPresent(CacheNames.EVERYDAY_RECOMMEND, cacheKey, Map.class)
                .map(map -> (Map<String, Object>) map)
                .switchIfEmpty(Mono.defer(() ->
                        // L1 未命中，查 L2 (元数据表 + 歌曲关联表)
                        everydayRecommendRepository.findByDate(date)
                                .flatMap(meta ->
                                        // 从歌曲关联表加载歌曲列表
                                        songRepository.findEverydayRecommend(date)
                                                .collectList()
                                                .map(songs -> buildEverydayResponse(meta, songs))
                                                .flatMap(response ->
                                                        // 回填 L1
                                                        caffeineCacheService.put(CacheNames.EVERYDAY_RECOMMEND, cacheKey, response).thenReturn(response)
                                                )
                                )
                                .switchIfEmpty(Mono.defer(() ->
                                        // L2 也为空，调用 loader
                                        loader.get().flatMap(response -> {
                                            Map<String, Object> data = (Map<String, Object>) response.get("data");

                                            return Mono.just(data).doOnSuccess(in -> {
                                                // 执行后台缓存和标准化任务，但不阻塞主流程
                                                Mono<Void> backgroundTask = Mono.when(caffeineCacheService.put(CacheNames.EVERYDAY_RECOMMEND, cacheKey, response).onErrorResume(e -> {
                                                    log.error("Failed to cache everyday recommend", e);
                                                    return Mono.empty();
                                                }), dataNormalizationService.normalizeEverydayRecommend(data, date).onErrorResume(e -> {
                                                    log.error("Failed to normalize everyday recommend", e);
                                                    return Mono.empty();
                                                })).subscribeOn(Schedulers.boundedElastic());

                                                // 确保后台任务被触发，但主流程继续
                                                backgroundTask.subscribe();
                                            });

                                        })
                                ))
                ));
    }

    // ========== EntityCacheService 实现 - 歌词缓存 ==========

    @Override
    public Mono<Lyric> getLyric(String hash, String accessKey, Supplier<Mono<Lyric>> loader) {
        String cacheKey = buildLyricCacheKey(hash, accessKey);

        // L1 缓存检查
        return caffeineCacheService.getIfPresent(CacheNames.LYRICS, cacheKey, Lyric.class)
                .switchIfEmpty(Mono.defer(() ->
                        // L1 未命中，查 L2 (Repository)
                        lyricRepository.findByHashAndAccessKey(hash, accessKey)
                                .flatMap(lyric -> {
                                    // 从文件系统加载歌词内容
                                    return loadLyricContent(lyric)
                                            .flatMap(lyricWithContent -> {
                                                // 回填 L1
                                                return caffeineCacheService.put(CacheNames.LYRICS, cacheKey, lyricWithContent)
                                                        .thenReturn(lyricWithContent);
                                            });
                                })
                                .switchIfEmpty(Mono.defer(() ->
                                        // L2 也未命中，调用 loader
                                        loader.get()
                                                .flatMap(lyric -> saveLyric(lyric, lyric.getContent()))
                                ))
                ));
    }

    @Override
    public Mono<Lyric> saveLyric(Lyric lyric, String content) {
        String cacheKey = buildLyricCacheKey(lyric.getHash(), lyric.getAccesskey());

        // 保存到 L2 (Repository + 文件系统)
        return lyricRepository.saveWithContent(lyric, content)
                .flatMap(saved -> {
                    // 设置内容到实体（用于 L1 缓存）
                    saved.setContent(content);
                    // 回填 L1
                    return caffeineCacheService.put(CacheNames.LYRICS, cacheKey, saved)
                            .thenReturn(saved);
                });
    }

    @Override
    public Mono<Boolean> isLyricCached(String hash, String accessKey) {
        String cacheKey = buildLyricCacheKey(hash, accessKey);

        // 先检查 L1
        return caffeineCacheService.getIfPresent(CacheNames.LYRICS, cacheKey, Lyric.class)
                .hasElement()
                .flatMap(inL1 -> {
                    if (inL1) {
                        return Mono.just(true);
                    }
                    // 检查 L2
                    return lyricRepository.findByHashAndAccessKey(hash, accessKey)
                            .hasElement();
                });
    }

    /**
     * 从文件系统加载歌词内容
     */
    private Mono<Lyric> loadLyricContent(Lyric lyric) {
        if (lyric.getFilePath() == null) {
            return Mono.just(lyric);
        }
        return lyricRepository.getLyricContent(lyric.getId())
                .map(content -> {
                    lyric.setContent(content);
                    return lyric;
                })
                .defaultIfEmpty(lyric);
    }

    private String buildLyricCacheKey(String hash, String accessKey) {
        return CacheNames.KEY_PREFIX_LYRIC + hash + ":" + accessKey;
    }

    // ========== EntityCacheService 实现 - 歌手缓存 ==========

    @Override
    public Mono<Singer> getSinger(Long singerId, Supplier<Mono<Singer>> loader) {
        String cacheKey = CacheNames.KEY_PREFIX_ARTIST + singerId;

        // L1 缓存检查
        return caffeineCacheService.getIfPresent(CacheNames.ARTIST_INFO, cacheKey, Singer.class)
                .switchIfEmpty(Mono.defer(() ->
                        // L1 未命中，查 L2 (Repository)
                        singerRepository.findById(singerId)
                                .flatMap(singer -> {
                                    // 回填 L1
                                    return caffeineCacheService.put(CacheNames.ARTIST_INFO, cacheKey, singer)
                                            .thenReturn(singer);
                                })
                                .switchIfEmpty(Mono.defer(() ->
                                        // L2 也未命中，调用 loader
                                        loader.get()
                                                .flatMap(this::saveSinger)
                                ))
                ));
    }

    @Override
    public Mono<Singer> saveSinger(Singer singer) {
        String cacheKey = CacheNames.KEY_PREFIX_ARTIST + singer.getSingerid();

        // 保存到 L2 (Repository)
        return singerRepository.save(singer)
                .flatMap(saved -> {
                    // 回填 L1
                    return caffeineCacheService.put(CacheNames.ARTIST_INFO, cacheKey, saved)
                            .thenReturn(saved);
                });
    }

    // ========== EntityCacheService 实现 - 分类缓存 ==========




    // ========== EntityCacheService 实现 - 缓存管理 ==========

    @Override
    public Mono<Void> clearCache(String cacheName) {
        return caffeineCacheService.clear(cacheName);
    }

    @Override
    public Mono<Map<String, Object>> getCacheStats() {
        return getAllStats();
    }

    // ========== 媒体文件相关方法 ==========

    /**
     * 获取歌曲文件，如果不存在则触发异步下载
     */
    public Mono<MediaStorageService.MediaFile> getSongFile(String hash, String url) {
        return mediaStorageService.getFile(hash, MediaStorageService.MediaType.SONG)
                .flatMap(opt -> {
                    if (opt.isPresent()) {
                        log.debug("Song file found in cache: {}", hash);
                        return Mono.just(opt.get());
                    }
                    // 触发异步下载
                    if (url != null && !url.isEmpty()) {
                        downloadTaskQueue.submit(hash, url, MediaStorageService.MediaType.SONG).subscribe();
                        log.info("Song download triggered: {}", hash);
                    }
                    return Mono.empty();
                });
    }

    /**
     * 获取图片文件，如果不存在则触发异步下载
     */
    public Mono<MediaStorageService.MediaFile> getImageFile(String hash, String url) {
        return mediaStorageService.getFile(hash, MediaStorageService.MediaType.IMAGE)
                .flatMap(opt -> {
                    if (opt.isPresent()) {
                        log.debug("Image file found in cache: {}", hash);
                        return Mono.just(opt.get());
                    }
                    // 触发异步下载
                    if (url != null && !url.isEmpty()) {
                        downloadTaskQueue.submit(hash, url, MediaStorageService.MediaType.IMAGE).subscribe();
                        log.info("Image download triggered: {}", hash);
                    }
                    return Mono.empty();
                });
    }

    /**
     * 检查歌曲是否已缓存
     */
    public Mono<Boolean> isSongCached(String hash) {
        return mediaStorageService.getFile(hash, MediaStorageService.MediaType.SONG)
                .map(opt -> opt.isPresent());
    }

    /**
     * 检查图片是否已缓存
     */
    public Mono<Boolean> isImageCached(String hash) {
        return mediaStorageService.getFile(hash, MediaStorageService.MediaType.IMAGE)
                .map(opt -> opt.isPresent());
    }

    /**
     * 获取所有缓存统计
     */
    public Mono<Map<String, Object>> getAllStats() {
        return Mono.zip(
                caffeineCacheService.getStats(),
                mediaStorageService.getStats()
        ).map(tuple -> {
            Map<String, Object> stats = new HashMap<>();
            stats.put("caffeine", tuple.getT1());
            stats.put("storage", tuple.getT2());
            return stats;
        });
    }
}
