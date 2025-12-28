package com.lanfunoe.gocache.repository.custom;

import com.lanfunoe.gocache.model.Song;
import com.lanfunoe.gocache.model.SongHashId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 歌曲 Repository 自定义方法接口
 */
public interface SongRepositoryCustom {

    Mono<Long> upsert(List<Song> songs);

    Flux<Song> findAllIn(Iterable<Song> songs);

    Flux<Song> findAllByIds(Iterable<SongHashId> ids);
}