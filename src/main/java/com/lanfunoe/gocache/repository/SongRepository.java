package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.Song;
import com.lanfunoe.gocache.model.SongHashId;
import com.lanfunoe.gocache.repository.custom.SongRepositoryCustom;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * 歌曲 Repository 接口
 * 使用复合主键 (audio_id, hash)
 */
public interface SongRepository extends R2dbcRepository<Song, SongHashId>, SongRepositoryCustom {
}
