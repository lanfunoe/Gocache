package com.lanfunoe.gocache.repository.impl;

import com.lanfunoe.gocache.model.PlaylistTag;
import com.lanfunoe.gocache.repository.custom.PlaylistTagRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlaylistTagRepositoryImpl implements PlaylistTagRepositoryCustom {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<Long> upsert(List<PlaylistTag> playlistTags) {
        if (playlistTags == null || playlistTags.isEmpty()) {
            return Mono.just(0L);
        }

        String[] globalCollectionIds = playlistTags.stream()
                .map(PlaylistTag::getGlobalCollectionId)
                .toArray(String[]::new);
        Long[] tagIds = playlistTags.stream()
                .map(PlaylistTag::getTagId)
                .toArray(Long[]::new);

        String sql = """
            INSERT INTO playlist_tag (global_collection_id, tag_id)
            SELECT * FROM UNNEST($1::varchar[], $2::bigint[])
            ON CONFLICT (global_collection_id, tag_id) DO NOTHING
            """;

        return databaseClient.sql(sql)
                .bind("$1", globalCollectionIds)
                .bind("$2", tagIds)
                .fetch()
                .rowsUpdated();
    }
}