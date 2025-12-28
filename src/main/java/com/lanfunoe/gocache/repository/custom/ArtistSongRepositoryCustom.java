package com.lanfunoe.gocache.repository.custom;

import com.lanfunoe.gocache.model.ArtistSong;
import com.lanfunoe.gocache.model.ArtistSongId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 歌手-歌曲关联 Repository 自定义方法接口
 */
public interface ArtistSongRepositoryCustom {
    Mono<Long> upsert(List<ArtistSong> artistSongs);

    Flux<ArtistSong> findAllByArtistSongId(Iterable<ArtistSongId> ids);
}
