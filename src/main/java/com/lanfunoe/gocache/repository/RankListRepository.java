package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.RankList;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * 排行榜 Repository 接口
 */
public interface RankListRepository extends R2dbcRepository<RankList, Long> {
}
