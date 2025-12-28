package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.Lyric;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * 歌词 Repository 接口
 */
public interface LyricRepository extends R2dbcRepository<Lyric, Long> {

}
