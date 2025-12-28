package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.SongQuality;
import com.lanfunoe.gocache.model.SongQualityId;
import com.lanfunoe.gocache.repository.custom.SongQualityRepositoryCustom;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * 歌曲音质版本 Repository 接口
 */
public interface SongQualityRepository extends R2dbcRepository<SongQuality, SongQualityId>, SongQualityRepositoryCustom {
}
