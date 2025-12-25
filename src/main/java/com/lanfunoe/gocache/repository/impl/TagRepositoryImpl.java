package com.lanfunoe.gocache.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanfunoe.gocache.model.Tag;
import com.lanfunoe.gocache.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 标签 Repository 实现
 */
@Slf4j
@Repository
public class TagRepositoryImpl extends AbstractSqliteRepository<Tag, Long> implements TagRepository {

    public TagRepositoryImpl(@Qualifier("sqliteDataSource") DataSource dataSource, ObjectMapper objectMapper) {
        super(dataSource, objectMapper);
    }

    @Override
    protected String getTableName() {
        return "tags";
    }

    @Override
    protected String getIdColumnName() {
        return "tagid";
    }

    @Override
    protected Tag mapRow(ResultSet rs) throws SQLException {
        Tag entity = new Tag();
        entity.setTagid(getLong(rs, "tagid"));
        entity.setTagname(getString(rs, "tagname"));
        entity.setParentid(getLong(rs, "parentid"));
        entity.setSort(getInteger(rs, "sort"));
        entity.setCreatedAt(getLong(rs, "created_at"));
        entity.setUpdatedAt(getLong(rs, "updated_at"));
        return entity;
    }

    @Override
    protected String getInsertSql() {
        return """
            INSERT INTO tags (tagid, tagname, parentid, sort, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
    }

    @Override
    protected String getUpdateSql() {
        return """
            UPDATE tags SET tagname = ?, parentid = ?, sort = ?, updated_at = ?
            WHERE tagid = ?
            """;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Tag entity) throws SQLException {
        ps.setLong(1, entity.getTagid());
        ps.setString(2, entity.getTagname());
        ps.setLong(3, entity.getParentid());

        // Handle nullable Integer fields
        if (entity.getSort() != null) {
            ps.setInt(4, entity.getSort());
        } else {
            ps.setNull(4, Types.INTEGER);
        }

        ps.setLong(5, entity.getCreatedAt());
        ps.setLong(6, entity.getUpdatedAt());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Tag entity) throws SQLException {
        ps.setString(1, entity.getTagname());
        ps.setLong(2, entity.getParentid());

        // Handle nullable Integer fields
        if (entity.getSort() != null) {
            ps.setInt(3, entity.getSort());
        } else {
            ps.setNull(3, java.sql.Types.INTEGER);
        }

        ps.setLong(4, entity.getUpdatedAt());
        ps.setLong(5, entity.getTagid());
    }

    @Override
    protected Long getId(Tag entity) {
        return entity.getTagid();
    }

    @Override
    public Flux<Tag> findByParentId(Long parentId) {
        String sql = "SELECT * FROM tags WHERE parentid = ?";
        return executeQuery(sql, ps -> ps.setLong(1, parentId));
    }

    @Override
    public Flux<Tag> findTopLevelTags() {
        String sql = "SELECT * FROM tags WHERE parentid = 0 ORDER BY sort ASC";
        return executeQuery(sql, ps -> {});
    }

    @Override
    public Flux<Tag> findValidTags() {
        // 查询7天内更新过的标签
        String sql = "SELECT * FROM tags WHERE updated_at >= ? ORDER BY updated_at DESC";
        long sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L);
        return executeQuery(sql, ps -> ps.setLong(1, sevenDaysAgo));
    }
}