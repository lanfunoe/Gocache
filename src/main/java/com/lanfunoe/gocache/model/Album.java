package com.lanfunoe.gocache.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 专辑实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Album extends BaseEntity {

    /**
     * 专辑ID（主键）
     */
    private Long albumId;

    /**
     * 专辑名称
     */
    private String albumName;

    /**
     * 可变尺寸封面
     */
    private String sizableCover;

    /**
     * 固定封面
     */
    private String cover;

    /**
     * 发布日期
     */
    private String publishDate;

    /**
     * 专辑简介
     */
    private String intro;

    /**
     * 歌曲数量
     */
    private Integer songCount;

    /**
     * 额外信息（JSON格式）
     */
    private String extraInfo;
}
