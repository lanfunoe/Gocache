package com.lanfunoe.gocache.repository.custom;

import com.lanfunoe.gocache.model.SongUrl;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SongUrlRepositoryCustom {
    /**
     * 批量插入或更新歌曲URL缓存
     */
    Mono<Void> upsert(List<SongUrl> songUrls);

}