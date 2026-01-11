package com.lanfunoe.gocache.repository.custom;

import com.lanfunoe.gocache.model.SongUrl;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SongUrlRepositoryCustom {
    /**
     * 批量插入或更新歌曲URL缓存
     */
    Mono<Long> upsert(List<SongUrl> songUrls);

    /**
     * 标记歌曲URL为已下载
     */
    Mono<Void> markAsDownloaded(String hash, String quality, String url);
}