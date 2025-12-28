package com.lanfunoe.gocache.repository.custom;

import com.lanfunoe.gocache.model.SongQuality;
import com.lanfunoe.gocache.model.SongQualityId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 歌曲音质版本 Repository 自定义方法接口
 */
public interface SongQualityRepositoryCustom {
    Mono<Long> upsert(List<SongQuality> songQualities);

    Flux<SongQuality> findAllBySongQualityId(Iterable<SongQualityId> ids);
}
