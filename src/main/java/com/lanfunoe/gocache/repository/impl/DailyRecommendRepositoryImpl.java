package com.lanfunoe.gocache.repository.impl;

import com.lanfunoe.gocache.model.DailyRecommend;
import com.lanfunoe.gocache.model.DailyRecommendId;
import com.lanfunoe.gocache.repository.AbstractCompositeKeyQuerySupport;
import com.lanfunoe.gocache.repository.custom.DailyRecommendRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 每日推荐 Repository 实现类
 * 使用 UNNEST 语法实现批量 upsert
 * 复合主键: (user_id, recommend_date, song_hash, audio_id)
 */
@Slf4j
@Component
public class DailyRecommendRepositoryImpl extends AbstractCompositeKeyQuerySupport<DailyRecommend, DailyRecommendId>
        implements DailyRecommendRepositoryCustom {

    private final DatabaseClient databaseClient;

    public DailyRecommendRepositoryImpl(DatabaseClient databaseClient, R2dbcEntityTemplate template) {
        super(template);
        this.databaseClient = databaseClient;
    }

    @Override
    protected Map<String, Function<DailyRecommendId, Object>> getFieldMapping() {
        return Map.of(
                "user_id", DailyRecommendId::getUserId,
                "recommend_date", DailyRecommendId::getRecommendDate,
                "song_hash", DailyRecommendId::getSongHash,
                "audio_id", DailyRecommendId::getAudioId
        );
    }

    @Override
    protected Class<DailyRecommend> getEntityClass() {
        return DailyRecommend.class;
    }

    @Override
    public Flux<DailyRecommend> findAllByDailyRecommendId(Iterable<DailyRecommendId> ids) {
        return findAllByIds(ids);
    }

    @Override
    public Mono<Long> upsert(List<DailyRecommend> dailyRecommends) {
        if (dailyRecommends == null || dailyRecommends.isEmpty()) {
            return Mono.just(0L);
        }

        // 提取字段数组（4个主键 + 3个非主键）
        Long[] userIds = dailyRecommends.stream()
                .map(DailyRecommend::getUserId)
                .toArray(Long[]::new);
        String[] recommendDates = dailyRecommends.stream()
                .map(DailyRecommend::getRecommendDate)
                .toArray(String[]::new);
        String[] songHashes = dailyRecommends.stream()
                .map(DailyRecommend::getSongHash)
                .toArray(String[]::new);
        Long[] audioIds = dailyRecommends.stream()
                .map(DailyRecommend::getAudioId)
                .toArray(Long[]::new);
        String[] oriAudioNames = dailyRecommends.stream()
                .map(DailyRecommend::getOriAudioName)
                .toArray(String[]::new);
        String[] extraInfos = dailyRecommends.stream()
                .map(DailyRecommend::getExtraInfo)
                .toArray(String[]::new);
        LocalDateTime[] createdATs = dailyRecommends.stream()
                .map(DailyRecommend::getCreatedAt)
                .toArray(LocalDateTime[]::new);

        String sql = """
            INSERT INTO daily_recommend (user_id, recommend_date, song_hash,
                                        audio_id, ori_audio_name, extra_info,
                                        created_at)
            SELECT * FROM UNNEST($1::bigint[], $2::varchar[], $3::varchar[],
                                 $4::bigint[], $5::varchar[], $6::varchar[],
                                 $7::timestamp[])
            ON CONFLICT (user_id, recommend_date, song_hash, audio_id) DO UPDATE SET
                ori_audio_name = EXCLUDED.ori_audio_name,
                extra_info = EXCLUDED.extra_info,
                created_at = EXCLUDED.created_at
            """;

        return databaseClient.sql(sql)
                .bind("$1", userIds)
                .bind("$2", recommendDates)
                .bind("$3", songHashes)
                .bind("$4", audioIds)
                .bind("$5", oriAudioNames)
                .bind("$6", extraInfos)
                .bind("$7", createdATs)
                .fetch()
                .rowsUpdated()
                .doOnSuccess(rowsUpdated -> log.info("Upserted {} rows", rowsUpdated))
                .onErrorResume(e -> {
                    log.error("Failed to upsert:", e);
                    return Mono.just(0L);
                })
                .contextCapture();
    }
}