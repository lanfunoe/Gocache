package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.AlbumSong;
import com.lanfunoe.gocache.model.AlbumSongId;
import com.lanfunoe.gocache.repository.custom.AlbumSongRepositoryCustom;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * 专辑-歌曲关联 Repository 接口
 */
public interface AlbumSongRepository extends R2dbcRepository<AlbumSong, AlbumSongId>, AlbumSongRepositoryCustom {
}