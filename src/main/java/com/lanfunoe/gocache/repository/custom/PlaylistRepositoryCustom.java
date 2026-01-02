package com.lanfunoe.gocache.repository.custom;

import com.lanfunoe.gocache.model.Playlist;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 歌单 Repository 自定义方法接口
 */
public interface PlaylistRepositoryCustom {

    /**
     * 批量upsert歌单数据
     *
     * @param playlists 歌单列表
     * @return 影响的行数
     */
    Mono<Long> upsert(List<Playlist> playlists);
}