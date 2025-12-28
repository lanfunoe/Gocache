package com.lanfunoe.gocache.repository.impl;

import com.lanfunoe.gocache.model.PlaylistTagCategory;
import com.lanfunoe.gocache.repository.custom.PlaylistTagCategoryRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 歌单标签分类 Repository 实现类
 * 使用 UNNEST 语法实现批量 upsert
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PlaylistTagCategoryRepositoryImpl implements PlaylistTagCategoryRepositoryCustom {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<Long> upsert(List<PlaylistTagCategory> categories) {
        if (categories == null || categories.isEmpty()) {
            return Mono.just(0L);
        }

        // 提取字段数组
        Long[] tagIds = categories.stream()
                .map(PlaylistTagCategory::getTagId)
                .toArray(Long[]::new);
        String[] tagNames = categories.stream()
                .map(PlaylistTagCategory::getTagName)
                .toArray(String[]::new);
        Long[] parentIds = categories.stream()
                .map(PlaylistTagCategory::getParentId)
                .toArray(Long[]::new);
        Integer[] sorts = categories.stream()
                .map(PlaylistTagCategory::getSort)
                .toArray(Integer[]::new);
        String[] rowUpdateTimes = categories.stream()
                .map(PlaylistTagCategory::getRowUpdateTime)
                .toArray(String[]::new);
        String[] extraInfos = categories.stream()
                .map(PlaylistTagCategory::getExtraInfo)
                .toArray(String[]::new);

        String sql = """
            INSERT INTO playlist_tag_category (tag_id, tag_name, parent_id, sort,
                                               row_update_time, extra_info)
            SELECT * FROM UNNEST($1::bigint[], $2::varchar[], $3::bigint[],
                                 $4::integer[], $5::varchar[], $6::varchar[])
            ON CONFLICT (tag_id) DO UPDATE SET
                tag_name = EXCLUDED.tag_name,
                parent_id = EXCLUDED.parent_id,
                sort = EXCLUDED.sort,
                row_update_time = EXCLUDED.row_update_time,
                extra_info = EXCLUDED.extra_info
            """;

        return databaseClient.sql(sql)
                .bind("$1", tagIds)
                .bind("$2", tagNames)
                .bind("$3", parentIds)
                .bind("$4", sorts)
                .bind("$5", rowUpdateTimes)
                .bind("$6", extraInfos)
                .fetch()
                .rowsUpdated();
    }
}
