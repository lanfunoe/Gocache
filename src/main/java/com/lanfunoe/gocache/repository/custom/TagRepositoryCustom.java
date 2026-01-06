package com.lanfunoe.gocache.repository.custom;

import com.lanfunoe.gocache.model.Tag;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 歌单标签分类 Repository 自定义方法接口
 */
public interface TagRepositoryCustom {
    Mono<Long> upsert(List<Tag> categories);
}
