package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.ArtistSong;
import com.lanfunoe.gocache.model.ArtistSongId;
import com.lanfunoe.gocache.repository.custom.ArtistSongRepositoryCustom;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * 歌手-歌曲关联 Repository 接口
 */
public interface ArtistSongRepository extends R2dbcRepository<ArtistSong, ArtistSongId>, ArtistSongRepositoryCustom {
}