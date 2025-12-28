package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.Album;
import com.lanfunoe.gocache.repository.custom.AlbumRepositoryCustom;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * 专辑 Repository 接口
 */
public interface AlbumRepository extends R2dbcRepository<Album, Long>, AlbumRepositoryCustom {
}
