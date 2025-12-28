package com.lanfunoe.gocache.repository.custom;

import com.lanfunoe.gocache.model.AlbumSong;
import com.lanfunoe.gocache.model.AlbumSongId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 专辑-歌曲关联 Repository 自定义方法接口
 */
public interface AlbumSongRepositoryCustom {
    Mono<Long> upsert(List<AlbumSong> albumSongs);

    Flux<AlbumSong> findAllByAlbumSongId(Iterable<AlbumSongId> ids);
}
