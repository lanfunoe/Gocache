package com.lanfunoe.gocache.repository.impl;

import com.lanfunoe.gocache.model.Album;
import com.lanfunoe.gocache.repository.custom.AlbumRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 专辑 Repository 实现类
 * 使用 UNNEST 语法实现批量 upsert
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlbumRepositoryImpl implements AlbumRepositoryCustom {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<Long> upsert(List<Album> albums) {
        if (albums == null || albums.isEmpty()) {
            return Mono.just(0L);
        }

        // 提取字段数组
        Long[] albumIds = albums.stream()
                .map(Album::getAlbumId)
                .toArray(Long[]::new);
        String[] albumNames = albums.stream()
                .map(Album::getAlbumName)
                .toArray(String[]::new);
        String[] sizableCovers = albums.stream()
                .map(Album::getSizableCover)
                .toArray(String[]::new);
        String[] covers = albums.stream()
                .map(Album::getCover)
                .toArray(String[]::new);
        String[] publishDates = albums.stream()
                .map(Album::getPublishDate)
                .toArray(String[]::new);
        String[] intros = albums.stream()
                .map(Album::getIntro)
                .toArray(String[]::new);
        Integer[] songCounts = albums.stream()
                .map(Album::getSongCount)
                .toArray(Integer[]::new);
        String[] extraInfos = albums.stream()
                .map(Album::getExtraInfo)
                .toArray(String[]::new);
        LocalDateTime[] createdATs = albums.stream()
                .map(Album::getCreatedAt)
                .toArray(LocalDateTime[]::new);
        LocalDateTime[] updatedATs = albums.stream()
                .map(Album::getUpdatedAt)
                .toArray(LocalDateTime[]::new);

        String sql = """
            INSERT INTO album (album_id, album_name, sizable_cover, cover,
                              publish_date, intro, song_count, extra_info,
                              created_at, updated_at)
            SELECT * FROM UNNEST($1::bigint[], $2::varchar[], $3::varchar[],
                                 $4::varchar[], $5::varchar[], $6::varchar[],
                                 $7::integer[], $8::varchar[], $9::timestamp[],
                                 $10::timestamp[])
            ON CONFLICT (album_id) DO UPDATE SET
                album_name = EXCLUDED.album_name,
                sizable_cover = EXCLUDED.sizable_cover,
                cover = EXCLUDED.cover,
                publish_date = EXCLUDED.publish_date,
                intro = EXCLUDED.intro,
                song_count = EXCLUDED.song_count,
                extra_info = EXCLUDED.extra_info,
                created_at = EXCLUDED.created_at,
                updated_at = EXCLUDED.updated_at
            """;

        return databaseClient.sql(sql)
                .bind("$1", albumIds)
                .bind("$2", albumNames)
                .bind("$3", sizableCovers)
                .bind("$4", covers)
                .bind("$5", publishDates)
                .bind("$6", intros)
                .bind("$7", songCounts)
                .bind("$8", extraInfos)
                .bind("$9", createdATs)
                .bind("$10", updatedATs)
                .fetch()
                .rowsUpdated();
    }
}
