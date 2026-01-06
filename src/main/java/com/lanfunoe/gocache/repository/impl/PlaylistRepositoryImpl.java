package com.lanfunoe.gocache.repository.impl;

import com.lanfunoe.gocache.model.Playlist;
import com.lanfunoe.gocache.model.PlaylistId;
import com.lanfunoe.gocache.repository.AbstractCompositeKeyQuerySupport;
import com.lanfunoe.gocache.repository.custom.PlaylistRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 歌单 Repository 实现类
 * 使用 UNNEST 语法实现批量 upsert
 * 复合主键: (global_collection_id, category_id)
 */
@Slf4j
@Component
public class PlaylistRepositoryImpl extends AbstractCompositeKeyQuerySupport<Playlist, PlaylistId>
        implements PlaylistRepositoryCustom {

    private final DatabaseClient databaseClient;

    public PlaylistRepositoryImpl(DatabaseClient databaseClient, R2dbcEntityTemplate template) {
        super(template);
        this.databaseClient = databaseClient;
    }

    @Override
    protected Map<String, Function<PlaylistId, Object>> getFieldMapping() {
        return Map.of(
                "global_collection_id", PlaylistId::getGlobalCollectionId,
                "category_id", PlaylistId::getCategoryId
        );
    }

    @Override
    protected Class<Playlist> getEntityClass() {
        return Playlist.class;
    }

    @Override
    public Mono<Long> upsert(List<Playlist> playlists) {
        if (playlists == null || playlists.isEmpty()) {
            return Mono.just(0L);
        }

        // 提取字段数组
        String[] globalCollectionIds = playlists.stream()
                .map(Playlist::getGlobalCollectionId)
                .toArray(String[]::new);
        Integer[] categoryIds = playlists.stream()
                .map(p -> p.getCategoryId() != null ? p.getCategoryId() : 0)
                .toArray(Integer[]::new);
        Integer[] listids = playlists.stream()
                .map(Playlist::getListid)
                .toArray(Integer[]::new);
        Long[] listCreateUserids = playlists.stream()
                .map(Playlist::getListCreateUserid)
                .toArray(Long[]::new);
        String[] listCreateUsernames = playlists.stream()
                .map(Playlist::getListCreateUsername)
                .toArray(String[]::new);
        String[] specialnames = playlists.stream()
                .map(Playlist::getSpecialname)
                .toArray(String[]::new);
        String[] pics = playlists.stream()
                .map(Playlist::getPic)
                .toArray(String[]::new);
        String[] flexibleCovers = playlists.stream()
                .map(Playlist::getFlexibleCover)
                .toArray(String[]::new);
        String[] intros = playlists.stream()
                .map(Playlist::getIntro)
                .toArray(String[]::new);
        String[] publishDates = playlists.stream()
                .map(Playlist::getPublishDate)
                .toArray(String[]::new);
        String[] extraInfos = playlists.stream()
                .map(Playlist::getExtraInfo)
                .toArray(String[]::new);

        String sql = """
            INSERT INTO playlist (
                global_collection_id, category_id, listid,
                list_create_userid, list_create_username,
                specialname, pic, flexible_cover, intro,
                publish_date, extra_info
            )
            SELECT * FROM UNNEST($1::varchar[], $2::integer[], $3::integer[],
                                 $4::bigint[], $5::varchar[],
                                 $6::varchar[], $7::varchar[], $8::varchar[], $9::varchar[],
                                 $10::varchar[], $11::varchar[])
            ON CONFLICT (global_collection_id, category_id)
            DO UPDATE SET
                listid = EXCLUDED.listid,
                list_create_userid = EXCLUDED.list_create_userid,
                list_create_username = EXCLUDED.list_create_username,
                specialname = EXCLUDED.specialname,
                pic = EXCLUDED.pic,
                flexible_cover = EXCLUDED.flexible_cover,
                intro = EXCLUDED.intro,
                publish_date = EXCLUDED.publish_date,
                extra_info = EXCLUDED.extra_info
            """;

        return databaseClient.sql(sql)
                .bind("$1", globalCollectionIds)
                .bind("$2", categoryIds)
                .bind("$3", listids)
                .bind("$4", listCreateUserids)
                .bind("$5", listCreateUsernames)
                .bind("$6", specialnames)
                .bind("$7", pics)
                .bind("$8", flexibleCovers)
                .bind("$9", intros)
                .bind("$10", publishDates)
                .bind("$11", extraInfos)
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