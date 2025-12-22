package com.lanfunoe.gocache.model;

import lombok.Data;

/**
 * 实体基类，包含通用的时间戳字段
 */
@Data
public abstract class BaseEntity {

    /**
     * 创建时间戳（毫秒）
     */
    private Long createdAt;

    /**
     * 更新时间戳（毫秒）
     */
    private Long updatedAt;

    /**
     * 设置创建和更新时间为当前时间
     */
    public void initTimestamps() {
        long now = System.currentTimeMillis();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * 更新更新时间为当前时间
     */
    public void updateTimestamp() {
        this.updatedAt = System.currentTimeMillis();
    }
}
