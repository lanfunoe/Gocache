package com.lanfunoe.gocache.service.cache;

import com.lanfunoe.gocache.dto.TopPlaylistResponse;
import com.lanfunoe.gocache.repository.PlaylistRepository;
import com.lanfunoe.gocache.service.data.TopPlaylistConverter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

/**
 * 热门歌单缓存服务
 * 负责L1 Caffeine缓存编排
 *
 * 缓存策略:
 * - L1: Caffeine 30分钟，最大1000条
 * - 缓存键: 包含全部请求参数（category_id, page, pagesize, withtag, withsong, sort, module_id）
 * - 缓存值: TopPlaylistResponse 实体（已分页的完整响应）
 * - 数据持久化: 异步保存到数据库（后台任务，不阻塞响应）
 */
@Slf4j
@Service
public class TopPlaylistCacheService {

    @Resource
    private ReactiveCacheService caffeineCacheService;

    @Resource
    private PlaylistRepository playlistRepository;

    @Resource
    private TopPlaylistConverter converter;

    /**
     * 获取热门歌单（完整缓存逻辑）
     *
     * @param categoryId 分类ID
     * @param page 页码（从1开始）
     * @param pageSize 每页大小
     * @param withtag 是否包含标签
     * @param withsong 是否包含歌曲
     * @param sort 排序方式
     * @param moduleId 模块ID
     * @param loader API加载器（缓存未命中时调用）
     * @return 热门歌单响应
     */
    public Mono<TopPlaylistResponse> get(
            Integer categoryId,
            Integer page,
            Integer pageSize,
            Integer withtag,
            Integer withsong,
            Integer sort,
            Integer moduleId,
            Supplier<Mono<TopPlaylistResponse>> loader) {
        
        // 生成缓存键（包含所有参数）
        String cacheKey = String.format("category:%d:page:%d:size:%d:tag:%d:song:%d:sort:%d:module:%d",
                categoryId, page, pageSize, withtag, withsong, sort, moduleId);


        // L1 检查（直接缓存 TopPlaylistResponse）
        return caffeineCacheService.getIfPresent(CacheNames.TOP_PLAYLIST, cacheKey, TopPlaylistResponse.class)
            .switchIfEmpty(Mono.defer(() ->
                // L1 未命中，调用 API
                loader.get()
                    .flatMap(response -> {
                        // 异步保存到数据库（后台任务，不阻塞）
                        saveToDatabase(response.specialList(), categoryId)
                            .subscribeOn(Schedulers.boundedElastic())
                            .onErrorResume(e -> {
                                log.error("Failed to save to database", e);
                                return Mono.empty();
                            })
                            .subscribe();


                        // 回填 L1 并返回
                        return refreshL1Cache(cacheKey, response).thenReturn(response);
                    })
            ));
    }

    /**
     * 刷新L1缓存
     */
    private Mono<Void> refreshL1Cache(String cacheKey, TopPlaylistResponse response) {
        return caffeineCacheService.put(CacheNames.TOP_PLAYLIST, cacheKey, response, Duration.ofMinutes(30))
            .onErrorResume(e -> {
                log.error("Failed to cache to L1: {}", cacheKey, e);
                return Mono.empty();
            });
    }

    /**
     * 保存到数据库（playlist 表）
     */
    private Mono<Void> saveToDatabase(List<TopPlaylistResponse.SpecialList> specialLists, Integer categoryId) {
        if (specialLists == null || specialLists.isEmpty()) {
            log.warn("Empty special list, skip saving to database");
            return Mono.empty();
        }

        return Mono.fromCallable(() -> converter.toPlaylistList(specialLists, categoryId))
                .flatMap(playlists -> playlistRepository.upsert(playlists).then())
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(e -> {
                    log.error("Failed to save to database for category {}", categoryId, e);
                    return Mono.empty();
                })
                .doOnSuccess(v -> log.info("Database save completed for category {}", categoryId))
                .contextCapture();
    }
}