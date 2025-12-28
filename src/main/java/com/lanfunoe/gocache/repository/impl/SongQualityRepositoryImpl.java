package com.lanfunoe.gocache.repository.impl;

import com.lanfunoe.gocache.model.SongQuality;
import com.lanfunoe.gocache.model.SongQualityId;
import com.lanfunoe.gocache.repository.AbstractCompositeKeyQuerySupport;
import com.lanfunoe.gocache.repository.custom.SongQualityRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 歌曲音质版本 Repository 实现类
 * 使用 UNNEST 语法实现批量 upsert
 * 复合主键: (audio_id, quality_hash)
 */
@Slf4j
@Component
public class SongQualityRepositoryImpl extends AbstractCompositeKeyQuerySupport<SongQuality, SongQualityId>
        implements SongQualityRepositoryCustom {

    private final DatabaseClient databaseClient;

    public SongQualityRepositoryImpl(DatabaseClient databaseClient, R2dbcEntityTemplate template) {
        super(template);
        this.databaseClient = databaseClient;
    }

    @Override
    protected Map<String, Function<SongQualityId, Object>> getFieldMapping() {
        return Map.of(
                "audio_id", SongQualityId::getAudioId,
                "quality_hash", SongQualityId::getQualityHash
        );
    }

    @Override
    protected Class<SongQuality> getEntityClass() {
        return SongQuality.class;
    }

    @Override
    public Flux<SongQuality> findAllBySongQualityId(Iterable<SongQualityId> ids) {
        return findAllByIds(ids);
    }

    @Override
    public Mono<Long> upsert(List<SongQuality> songQualities) {
        if (songQualities == null || songQualities.isEmpty()) {
            return Mono.just(0L);
        }

        // 去重：确保每个 (audio_id, quality_hash) 组合只出现一次
        // 使用 LinkedHashMap 保持插入顺序，保留第一个出现的记录
        Map<SongQualityId, SongQuality> uniqueQualities = new LinkedHashMap<>();
        for (SongQuality quality : songQualities) {
            SongQualityId id = quality.getId();
            if (!uniqueQualities.containsKey(id)) {
                uniqueQualities.put(id, quality);
            }
        }
        
        List<SongQuality> deduplicatedQualities = new ArrayList<>(uniqueQualities.values());
        if (deduplicatedQualities.isEmpty()) {
            return Mono.just(0L);
        }

        // 提取字段数组（2个主键 + 6个非主键）
        Long[] audioIds = deduplicatedQualities.stream()
                .map(SongQuality::getAudioId)
                .toArray(Long[]::new);
        String[] qualityHashes = deduplicatedQualities.stream()
                .map(SongQuality::getQualityHash)
                .toArray(String[]::new);
        String[] songHashes = deduplicatedQualities.stream()
                .map(SongQuality::getSongHash)
                .toArray(String[]::new);
        Integer[] levels = deduplicatedQualities.stream()
                .map(SongQuality::getLevel)
                .toArray(Integer[]::new);
        Integer[] bitrates = deduplicatedQualities.stream()
                .map(SongQuality::getBitrate)
                .toArray(Integer[]::new);
        Long[] filesizes = deduplicatedQualities.stream()
                .map(SongQuality::getFilesize)
                .toArray(Long[]::new);
        Integer[] privileges = deduplicatedQualities.stream()
                .map(SongQuality::getPrivilege)
                .toArray(Integer[]::new);
        String[] extraInfos = deduplicatedQualities.stream()
                .map(SongQuality::getExtraInfo)
                .toArray(String[]::new);
        LocalDateTime[] createdATs = deduplicatedQualities.stream()
                .map(SongQuality::getCreatedAt)
                .toArray(LocalDateTime[]::new);

        String sql = """
            INSERT INTO song_quality (audio_id, quality_hash, song_hash, level,
                                      bitrate, filesize, privilege, extra_info,
                                      created_at)
            SELECT * FROM UNNEST($1::bigint[], $2::varchar[], $3::varchar[],
                                 $4::integer[], $5::integer[], $6::bigint[],
                                 $7::integer[], $8::varchar[], $9::timestamp[])
            ON CONFLICT (audio_id, quality_hash) DO UPDATE SET
                song_hash = EXCLUDED.song_hash,
                level = EXCLUDED.level,
                bitrate = EXCLUDED.bitrate,
                filesize = EXCLUDED.filesize,
                privilege = EXCLUDED.privilege,
                extra_info = EXCLUDED.extra_info,
                created_at = EXCLUDED.created_at
            """;

        return databaseClient.sql(sql)
                .bind("$1", audioIds)
                .bind("$2", qualityHashes)
                .bind("$3", songHashes)
                .bind("$4", levels)
                .bind("$5", bitrates)
                .bind("$6", filesizes)
                .bind("$7", privileges)
                .bind("$8", extraInfos)
                .bind("$9", createdATs)
                .fetch()
                .rowsUpdated();
    }
}