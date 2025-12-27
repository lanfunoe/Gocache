package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.DailyRecommend;
import com.lanfunoe.gocache.model.DailyRecommendId;
import reactor.core.publisher.Flux;

/**
 * 每日推荐 Repository 接口
 */
public interface DailyRecommendRepository extends ReactiveRepository<DailyRecommend, DailyRecommendId> {

    /**
     * 根据用户ID和日期查找每日推荐
     *
     * @param userId 用户ID
     * @param date   推荐日期 (YYYY-MM-DD)
     * @return 每日推荐列表
     */
    Flux<DailyRecommend> findByUserIdAndDate(String userId, String date);

}