package com.lanfunoe.gocache.service.cache;

import com.lanfunoe.gocache.dto.TopPlaylistResponse;
import com.lanfunoe.gocache.repository.PlaylistRepository;
import com.lanfunoe.gocache.repository.PlaylistTagRepository;
import com.lanfunoe.gocache.service.data.TopPlaylistConverter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.function.Supplier;

/**
 * 热门歌单缓存服务
 */
@Slf4j
@Service
public class TopPlaylistCacheService {

    @Resource
    private ReactiveCacheService caffeineService;

    @Resource
    private PlaylistRepository playlistRepository;

    @Resource
    private TopPlaylistConverter converter;

    @Resource
    private PlaylistTagRepository playlistTagRepository;

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
        String cacheKey = String.format("category:%d:page:%d:size:%d:tag:%d:song:%d:sort:%d:module:%d",
                categoryId, page, pageSize, withtag, withsong, sort, moduleId);

        return caffeineService.getIfPresent(CacheNames.TOP_PLAYLIST, cacheKey, TopPlaylistResponse.class)
                .switchIfEmpty(Mono.defer(() ->
                        // todo:这个 sort 排序未知不可缓存Db
                        loader.get().flatMap(response -> {
                            caffeineService.put(CacheNames.TOP_PLAYLIST, cacheKey, response);
                            refreshDatabase(response.specialList(), categoryId);
                            return Mono.just(response);
                        }))
                );
    }

    private void refreshDatabase(List<TopPlaylistResponse.SpecialList> specialLists, Integer categoryId) {
        if (specialLists == null || specialLists.isEmpty()) {
            log.warn("Empty special list, skip saving to database");
        }
        Mono.when(
                playlistRepository.upsert(converter.toPlaylistList(specialLists, categoryId)),
                playlistTagRepository.upsert(converter.toPlaylistTagList(specialLists))
        ).subscribeOn(Schedulers.parallel())
        .subscribe();
    }
}