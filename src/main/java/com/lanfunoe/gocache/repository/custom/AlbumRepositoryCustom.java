package com.lanfunoe.gocache.repository.custom;

import com.lanfunoe.gocache.model.Album;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 专辑 Repository 自定义方法接口
 */
public interface AlbumRepositoryCustom {
    Mono<Long> upsert(List<Album> albums);
}
