package com.lanfunoe.gocache.repository.impl;

import com.lanfunoe.gocache.model.Artist;
import com.lanfunoe.gocache.repository.custom.ArtistRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 艺术家/歌手 Repository 实现类
 * 使用 UNNEST 语法实现批量 upsert
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ArtistRepositoryImpl implements ArtistRepositoryCustom {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<Long> upsert(List<Artist> artists) {
        if (artists == null || artists.isEmpty()) {
            return Mono.just(0L);
        }

        // 提取字段数组
        Long[] artistIds = artists.stream()
                .map(Artist::getArtistId)
                .toArray(Long[]::new);
        String[] authorNames = artists.stream()
                .map(Artist::getAuthorName)
                .toArray(String[]::new);
        String[] sizableAvatars = artists.stream()
                .map(Artist::getSizableAvatar)
                .toArray(String[]::new);
        String[] pics = artists.stream()
                .map(Artist::getPic)
                .toArray(String[]::new);
        Integer[] songCounts = artists.stream()
                .map(Artist::getSongCount)
                .toArray(Integer[]::new);
        Integer[] albumCounts = artists.stream()
                .map(Artist::getAlbumCount)
                .toArray(Integer[]::new);
        Integer[] mvCounts = artists.stream()
                .map(Artist::getMvCount)
                .toArray(Integer[]::new);
        Integer[] fansnums = artists.stream()
                .map(Artist::getFansnums)
                .toArray(Integer[]::new);
        String[] intros = artists.stream()
                .map(Artist::getIntro)
                .toArray(String[]::new);
        String[] longIntros = artists.stream()
                .map(Artist::getLongIntro)
                .toArray(String[]::new);
        String[] extraInfos = artists.stream()
                .map(Artist::getExtraInfo)
                .toArray(String[]::new);
        LocalDateTime[] createdATs = artists.stream()
                .map(Artist::getCreatedAt)
                .toArray(LocalDateTime[]::new);
        LocalDateTime[] updatedATs = artists.stream()
                .map(Artist::getUpdatedAt)
                .toArray(LocalDateTime[]::new);

        String sql = """
            INSERT INTO artist (artist_id, author_name, sizable_avatar, pic,
                               song_count, album_count, mv_count, fansnums,
                               intro, long_intro, extra_info, created_at, updated_at)
            SELECT * FROM UNNEST($1::bigint[], $2::varchar[], $3::varchar[],
                                 $4::varchar[], $5::integer[], $6::integer[],
                                 $7::integer[], $8::integer[], $9::varchar[],
                                 $10::varchar[], $11::varchar[], $12::timestamp[],
                                 $13::timestamp[])
            ON CONFLICT (artist_id) DO UPDATE SET
                author_name = EXCLUDED.author_name,
                sizable_avatar = EXCLUDED.sizable_avatar,
                pic = EXCLUDED.pic,
                song_count = EXCLUDED.song_count,
                album_count = EXCLUDED.album_count,
                mv_count = EXCLUDED.mv_count,
                fansnums = EXCLUDED.fansnums,
                intro = EXCLUDED.intro,
                long_intro = EXCLUDED.long_intro,
                extra_info = EXCLUDED.extra_info,
                created_at = EXCLUDED.created_at,
                updated_at = EXCLUDED.updated_at
            """;

        return databaseClient.sql(sql)
                .bind("$1", artistIds)
                .bind("$2", authorNames)
                .bind("$3", sizableAvatars)
                .bind("$4", pics)
                .bind("$5", songCounts)
                .bind("$6", albumCounts)
                .bind("$7", mvCounts)
                .bind("$8", fansnums)
                .bind("$9", intros)
                .bind("$10", longIntros)
                .bind("$11", extraInfos)
                .bind("$12", createdATs)
                .bind("$13", updatedATs)
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
