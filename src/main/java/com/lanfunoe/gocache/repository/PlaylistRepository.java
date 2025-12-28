package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.Playlist;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * 歌单 Repository 接口
 */
public interface PlaylistRepository extends R2dbcRepository<Playlist, String> {

}
