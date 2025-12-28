package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.Artist;
import com.lanfunoe.gocache.repository.custom.ArtistRepositoryCustom;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * 艺术家/歌手 Repository 接口
 */
public interface ArtistRepository extends R2dbcRepository<Artist, Long>, ArtistRepositoryCustom {
}
