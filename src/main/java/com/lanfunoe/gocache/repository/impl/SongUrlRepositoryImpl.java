package com.lanfunoe.gocache.repository.impl;

import com.lanfunoe.gocache.model.SongUrl;
import com.lanfunoe.gocache.model.SongUrlId;
import com.lanfunoe.gocache.repository.AbstractCompositeKeyQuerySupport;
import com.lanfunoe.gocache.repository.custom.SongUrlRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class SongUrlRepositoryImpl extends AbstractCompositeKeyQuerySupport<SongUrl, SongUrlId> implements SongUrlRepositoryCustom {

    private final DatabaseClient databaseClient;
    private final R2dbcEntityTemplate template;

    public SongUrlRepositoryImpl(DatabaseClient databaseClient, R2dbcEntityTemplate template) {
        super(template);
        this.databaseClient = databaseClient;
        this.template = template;
    }

    @Override
    protected Map<String, Function<SongUrlId, Object>> getFieldMapping() {
        return Map.of(
                "audio_id", SongUrlId::getAudioId,
                "hash", SongUrlId::getHash,
                "quality", SongUrlId::getQuality
        );
    }

    @Override
    protected Class<SongUrl> getEntityClass() {
        return SongUrl.class;
    }


    @Override
    public Mono<Long> upsert(List<SongUrl> songUrls) {
        if (songUrls == null || songUrls.isEmpty()) {
            return Mono.just(0L);
        }

        // 提取字段数组（3个主键 + 8个非主键）
        Long[] audioIds = songUrls.stream()
                .map(SongUrl::getAudioId)
                .toArray(Long[]::new);
        String[] hashes = songUrls.stream()
                .map(SongUrl::getHash)
                .toArray(String[]::new);
        String[] qualities = songUrls.stream()
                .map(SongUrl::getQuality)
                .toArray(String[]::new);
        String[] urls = songUrls.stream()
                .map(SongUrl::getUrl)
                .toArray(String[]::new);
        String[] backupUrls = songUrls.stream()
                .map(SongUrl::getBackupUrl)
                .toArray(String[]::new);
        Long[] fileSizes = songUrls.stream()
                .map(SongUrl::getFileSize)
                .toArray(Long[]::new);
        Long[] timeLengths = songUrls.stream()
                .map(SongUrl::getTimeLength)
                .toArray(Long[]::new);
        String[] extNames = songUrls.stream()
                .map(SongUrl::getExtName)
                .toArray(String[]::new);
        LocalDateTime[] expireTimes = songUrls.stream()
                .map(SongUrl::getExpireTime)
                .toArray(LocalDateTime[]::new);
        String[] extraInfos = songUrls.stream()
                .map(SongUrl::getExtraInfo)
                .toArray(String[]::new);
        LocalDateTime[] createdATs = songUrls.stream()
                .map(SongUrl::getCreatedAt)
                .toArray(LocalDateTime[]::new);
        Boolean[] isDownloadeds = songUrls.stream()
                .map(songUrl -> songUrl.getIsDownloaded() != null ? songUrl.getIsDownloaded() : false)
                .toArray(Boolean[]::new);

        String sql = """
            INSERT INTO song_url (audio_id, hash, quality, url, backup_url,
                                  filesize, time_length, extname, expire_time,
                                  extra_info, created_at, is_downloaded)
            SELECT * FROM UNNEST($1::bigint[], $2::varchar[], $3::varchar[],
                                 $4::varchar[], $5::varchar[], $6::bigint[],
                                 $7::bigint[], $8::varchar[], $9::timestamp[],
                                 $10::varchar[], $11::timestamp[], $12::boolean[])
            ON CONFLICT (audio_id, hash, quality) DO UPDATE SET
                url = EXCLUDED.url,
                backup_url = EXCLUDED.backup_url,
                filesize = EXCLUDED.filesize,
                time_length = EXCLUDED.time_length,
                extname = EXCLUDED.extname,
                expire_time = EXCLUDED.expire_time,
                extra_info = EXCLUDED.extra_info,
                created_at = EXCLUDED.created_at,
                is_downloaded = EXCLUDED.is_downloaded
            """;

        return databaseClient.sql(sql)
                .bind("$1", audioIds)
                .bind("$2", hashes)
                .bind("$3", qualities)
                .bind("$4", urls)
                .bind("$5", backupUrls)
                .bind("$6", fileSizes)
                .bind("$7", timeLengths)
                .bind("$8", extNames)
                .bind("$9", expireTimes)
                .bind("$10", extraInfos)
                .bind("$11", createdATs)
                .bind("$12", isDownloadeds)
                .fetch()
                .rowsUpdated()
                .doOnSuccess(rowsUpdated -> log.info("Upserted {} rows", rowsUpdated))
                .onErrorResume(e -> {
                    log.error("Failed to upsert:", e);
                    return Mono.just(0L);
                })
                .contextCapture();
    }

    @Override
    public Mono<Void> markAsDownloaded(String hash, String quality, String url) {
        Query query = Query.query(
                Criteria.where("hash").is(hash)
                        .and("quality").is(quality)
                        .and("url").is(url)
        );

        return template.update(SongUrl.class)
                .matching(query)
                .apply(Update.update("is_downloaded", true))
                .doOnSuccess(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        log.info("Marked song as downloaded: hash={}, quality={}", hash, quality);
                    } else {
                        log.warn("No song URL found to mark as downloaded: hash={}, quality={}", hash, quality);
                    }
                })
                .onErrorResume(e -> {
                    log.warn("Failed to mark song as downloaded: hash={}, quality={}, error={}",
                            hash, quality, e.getMessage());
                    return Mono.just(0L);
                })
                .then();
    }
}