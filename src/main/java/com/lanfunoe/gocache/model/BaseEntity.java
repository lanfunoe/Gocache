package com.lanfunoe.gocache.model;

import lombok.Data;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 实体基类，包含通用的时间戳字段
 */
@Data
public abstract class BaseEntity {

    /**
     * 创建时间（ISO 8601 格式的日期时间字符串）
     * 与数据库 DATETIME 类型对应
     */
    private String createdAt;

    /**
     * 更新时间（ISO 8601 格式的日期时间字符串）
     * 与数据库 DATETIME 类型对应
     */
    private String updatedAt;

    /**
     * 设置创建和更新时间为当前时间
     */
    public void initTimestamps() {
        String now = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * 更新更新时间为当前时间
     */
    public void updateTimestamp() {
        String now = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this.updatedAt = now;
    }
}
