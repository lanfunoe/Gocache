package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.DailyRecommend;
import com.lanfunoe.gocache.model.DailyRecommendId;
import com.lanfunoe.gocache.repository.custom.DailyRecommendRepositoryCustom;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

/**
 * 每日推荐 Repository 接口
 */
public interface DailyRecommendRepository extends R2dbcRepository<DailyRecommend, DailyRecommendId>, DailyRecommendRepositoryCustom {

    /**
     * 根据用户ID和日期查找每日推荐
     *
     * @param userId 用户ID
     * @param recommendDate   推荐日期 (YYYY-MM-DD)
     * @return 每日推荐列表
     */
    Flux<DailyRecommend> findByUserIdAndRecommendDate(Long userId, String recommendDate);
}