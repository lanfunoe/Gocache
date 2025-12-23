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
     * 专辑封面URL
     */
    private String albumImg;

    /**
     * 专辑简介
     */
    private String intro;

    /**
     * 发布日期
     */
    private String publishDate;

    /**
     * 是否发布
     */
    private Integer publish;

    /**
     * 歌曲数量
     */
    private Integer songCount;

    /**
     * 语言
     */
    private String language;

    /**
     * 流派
     */
    private String genre;
}
