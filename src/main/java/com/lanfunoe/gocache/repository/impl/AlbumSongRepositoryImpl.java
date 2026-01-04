package com.lanfunoe.gocache.repository.impl;

import com.lanfunoe.gocache.model.AlbumSong;
import com.lanfunoe.gocache.model.AlbumSongId;
import com.lanfunoe.gocache.repository.AbstractCompositeKeyQuerySupport;
import com.lanfunoe.gocache.repository.custom.AlbumSongRepositoryCustom;
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
 * 专辑-歌曲关联 Repository 实现类
 * 使用 UNNEST 语法实现批量 upsert
 */
@Slf4j
@Component
public class AlbumSongRepositoryImpl extends AbstractCompositeKeyQuerySupport<AlbumSong, AlbumSongId>
        implements AlbumSongRepositoryCustom {

    private final DatabaseClient databaseClient;

    public AlbumSongRepositoryImpl(DatabaseClient databaseClient, R2dbcEntityTemplate template) {
        super(template);
        this.databaseClient = databaseClient;
    }

    @Override
    protected Map<String, Function<AlbumSongId, Object>> getFieldMapping() {
        return Map.of(
                "album_id", AlbumSongId::getAlbumId,
                "audio_id", AlbumSongId::getAudioId
        );
    }

    @Override
    protected Class<AlbumSong> getEntityClass() {
        return AlbumSong.class;
    }

    @Override
    public Flux<AlbumSong> findAllByAlbumSongId(Iterable<AlbumSongId> ids) {
        return findAllByIds(ids);
    }

    @Override
    public Mono<Long> upsert(List<AlbumSong> albumSongs) {
        if (albumSongs == null || albumSongs.isEmpty()) {
            return Mono.just(0L);
        }

        // 提取字段数组（2个主键 + 4个非主键）
        Long[] albumIds = albumSongs.stream()
                .map(AlbumSong::getAlbumId)
                .toArray(Long[]::new);
        Long[] audioIds = albumSongs.stream()
                .map(AlbumSong::getAudioId)
                .toArray(Long[]::new);
        String[] songHashes = albumSongs.stream()
                .map(AlbumSong::getSongHash)
                .toArray(String[]::new);
        Integer[] trackNos = albumSongs.stream()
                .map(AlbumSong::getTrackNo)
                .toArray(Integer[]::new);
        String[] extraInfos = albumSongs.stream()
                .map(AlbumSong::getExtraInfo)
                .toArray(String[]::new);
        LocalDateTime[] createdATs = albumSongs.stream()
                .map(AlbumSong::getCreatedAt)
                .toArray(LocalDateTime[]::new);

        String sql = """
            INSERT INTO album_song (album_id, audio_id, song_hash, track_no,
                                   extra_info, created_at)
            SELECT * FROM UNNEST($1::bigint[], $2::bigint[], $3::varchar[],
                                 $4::integer[], $5::varchar[], $6::timestamp[])
            ON CONFLICT (album_id, audio_id) DO UPDATE SET
                song_hash = EXCLUDED.song_hash,
                track_no = EXCLUDED.track_no,
                extra_info = EXCLUDED.extra_info,
                created_at = EXCLUDED.created_at
            """;

        return databaseClient.sql(sql)
                .bind("$1", albumIds)
                .bind("$2", audioIds)
                .bind("$3", songHashes)
                .bind("$4", trackNos)
                .bind("$5", extraInfos)
                .bind("$6", createdATs)
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