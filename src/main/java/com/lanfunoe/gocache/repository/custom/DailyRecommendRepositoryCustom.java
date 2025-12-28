package com.lanfunoe.gocache.repository.custom;

import com.lanfunoe.gocache.model.DailyRecommend;
import com.lanfunoe.gocache.model.DailyRecommendId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 每日推荐 Repository 自定义方法接口
 */
public interface DailyRecommendRepositoryCustom {
    Mono<Long> upsert(List<DailyRecommend> dailyRecommends);

    Flux<DailyRecommend> findAllByDailyRecommendId(Iterable<DailyRecommendId> ids);
}
