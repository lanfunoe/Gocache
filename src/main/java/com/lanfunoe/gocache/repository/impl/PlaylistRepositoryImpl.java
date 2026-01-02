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
        Integer[] listCreateListids = playlists.stream()
                .map(Playlist::getListCreateListid)
                .toArray(Integer[]::new);
        Long[] listCreateUserids = playlists.stream()
                .map(Playlist::getListCreateUserid)
                .toArray(Long[]::new);
        String[] listCreateUsernames = playlists.stream()
                .map(Playlist::getListCreateUsername)
                .toArray(String[]::new);
        String[] listCreateGids = playlists.stream()
                .map(Playlist::getListCreateGid)
                .toArray(String[]::new);
        String[] names = playlists.stream()
                .map(Playlist::getName)
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
        String[] tags = playlists.stream()
                .map(Playlist::getTags)
                .toArray(String[]::new);
        Integer[] counts = playlists.stream()
                .map(Playlist::getCount)
                .toArray(Integer[]::new);
        Integer[] sorts = playlists.stream()
                .map(Playlist::getSort)
                .toArray(Integer[]::new);
        String[] authors = playlists.stream()
                .map(Playlist::getAuthors)
                .toArray(String[]::new);
        String[] publishDates = playlists.stream()
                .map(Playlist::getPublishDate)
                .toArray(String[]::new);
        String[] extraInfos = playlists.stream()
                .map(Playlist::getExtraInfo)
                .toArray(String[]::new);

        String sql = """
            INSERT INTO playlist (
                global_collection_id, category_id, listid, list_create_listid,
                list_create_userid, list_create_username, list_create_gid, name,
                specialname, pic, flexible_cover, intro, tags, count, sort,
                authors, publish_date, extra_info
            )
            SELECT * FROM UNNEST($1::varchar[], $2::integer[], $3::integer[], $4::integer[],
                                 $5::bigint[], $6::varchar[], $7::varchar[], $8::varchar[],
                                 $9::varchar[], $10::varchar[], $11::varchar[], $12::varchar[],
                                 $13::varchar[], $14::integer[], $15::integer[], $16::varchar[],
                                 $17::varchar[], $18::varchar[])
            ON CONFLICT (global_collection_id, category_id)
            DO UPDATE SET
                listid = EXCLUDED.listid,
                list_create_listid = EXCLUDED.list_create_listid,
                list_create_userid = EXCLUDED.list_create_userid,
                list_create_username = EXCLUDED.list_create_username,
                list_create_gid = EXCLUDED.list_create_gid,
                name = EXCLUDED.name,
                specialname = EXCLUDED.specialname,
                pic = EXCLUDED.pic,
                flexible_cover = EXCLUDED.flexible_cover,
                intro = EXCLUDED.intro,
                tags = EXCLUDED.tags,
                count = EXCLUDED.count,
                sort = EXCLUDED.sort,
                authors = EXCLUDED.authors,
                publish_date = EXCLUDED.publish_date,
                extra_info = EXCLUDED.extra_info
            """;

        return databaseClient.sql(sql)
                .bind("$1", globalCollectionIds)
                .bind("$2", categoryIds)
                .bind("$3", listids)
                .bind("$4", listCreateListids)
                .bind("$5", listCreateUserids)
                .bind("$6", listCreateUsernames)
                .bind("$7", listCreateGids)
                .bind("$8", names)
                .bind("$9", specialnames)
                .bind("$10", pics)
                .bind("$11", flexibleCovers)
                .bind("$12", intros)
                .bind("$13", tags)
                .bind("$14", counts)
                .bind("$15", sorts)
                .bind("$16", authors)
                .bind("$17", publishDates)
                .bind("$18", extraInfos)
                .fetch()
                .rowsUpdated()
                .doOnNext(rowsUpdated -> log.debug("Upserted {} playlists", rowsUpdated))
                .onErrorResume(e -> {
                    log.error("Failed to upsert playlists", e);
                    return Mono.just(0L);
                });
    }
}