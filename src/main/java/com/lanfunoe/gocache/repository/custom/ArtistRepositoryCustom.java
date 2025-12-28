package com.lanfunoe.gocache.repository.custom;

import com.lanfunoe.gocache.model.Artist;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 艺术家 Repository 自定义方法接口
 */
public interface ArtistRepositoryCustom {
    Mono<Long> upsert(List<Artist> artists);
}
