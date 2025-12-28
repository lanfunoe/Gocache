package com.lanfunoe.gocache.repository.impl;

import com.lanfunoe.gocache.model.ArtistSong;
import com.lanfunoe.gocache.model.ArtistSongId;
import com.lanfunoe.gocache.repository.AbstractCompositeKeyQuerySupport;
import com.lanfunoe.gocache.repository.custom.ArtistSongRepositoryCustom;
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
 * 歌手-歌曲关联 Repository 实现类
 * 使用 UNNEST 语法实现批量 upsert
 */
@Slf4j
@Component
public class ArtistSongRepositoryImpl extends AbstractCompositeKeyQuerySupport<ArtistSong, ArtistSongId>
        implements ArtistSongRepositoryCustom {

    private final DatabaseClient databaseClient;

    public ArtistSongRepositoryImpl(DatabaseClient databaseClient, R2dbcEntityTemplate template) {
        super(template);
        this.databaseClient = databaseClient;
    }

    @Override
    protected Map<String, Function<ArtistSongId, Object>> getFieldMapping() {
        return Map.of(
                "artist_id", ArtistSongId::getArtistId,
                "audio_id", ArtistSongId::getAudioId
        );
    }

    @Override
    protected Class<ArtistSong> getEntityClass() {
        return ArtistSong.class;
    }

    @Override
    public Flux<ArtistSong> findAllByArtistSongId(Iterable<ArtistSongId> ids) {
        return findAllByIds(ids);
    }

    @Override
    public Mono<Long> upsert(List<ArtistSong> artistSongs) {
        if (artistSongs == null || artistSongs.isEmpty()) {
            return Mono.just(0L);
        }

        // 提取字段数组（2个主键 + 4个非主键）
        Long[] artistIds = artistSongs.stream()
                .map(ArtistSong::getArtistId)
                .toArray(Long[]::new);
        Long[] audioIds = artistSongs.stream()
                .map(ArtistSong::getAudioId)
                .toArray(Long[]::new);
        String[] songHashes = artistSongs.stream()
                .map(ArtistSong::getSongHash)
                .toArray(String[]::new);
        Integer[] sorts = artistSongs.stream()
                .map(ArtistSong::getSort)
                .toArray(Integer[]::new);
        String[] extraInfos = artistSongs.stream()
                .map(ArtistSong::getExtraInfo)
                .toArray(String[]::new);
        LocalDateTime[] createdATs = artistSongs.stream()
                .map(ArtistSong::getCreatedAt)
                .toArray(LocalDateTime[]::new);

        String sql = """
            INSERT INTO artist_song (artist_id, audio_id, song_hash, sort,
                                    extra_info, created_at)
            SELECT * FROM UNNEST($1::bigint[], $2::bigint[], $3::varchar[],
                                 $4::integer[], $5::varchar[], $6::timestamp[])
            ON CONFLICT (artist_id, audio_id) DO UPDATE SET
                song_hash = EXCLUDED.song_hash,
                sort = EXCLUDED.sort,
                extra_info = EXCLUDED.extra_info,
                created_at = EXCLUDED.created_at
            """;

        return databaseClient.sql(sql)
                .bind("$1", artistIds)
                .bind("$2", audioIds)
                .bind("$3", songHashes)
                .bind("$4", sorts)
                .bind("$5", extraInfos)
                .bind("$6", createdATs)
                .fetch()
                .rowsUpdated();
    }
}