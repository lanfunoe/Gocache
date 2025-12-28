package com.lanfunoe.gocache.repository.custom;

import com.lanfunoe.gocache.model.PlaylistTagCategory;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 歌单标签分类 Repository 自定义方法接口
 */
public interface PlaylistTagCategoryRepositoryCustom {
    Mono<Long> upsert(List<PlaylistTagCategory> categories);
}
