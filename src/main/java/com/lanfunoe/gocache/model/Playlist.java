package com.lanfunoe.gocache.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 歌单实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Playlist extends BaseEntity {

    /**
     * 全局收藏ID（主键）
     */
    private String globalCollectionId;

    /**
     * 列表ID
     */
    private Integer listid;

    /**
     * 创建列表ID
     */
    private Integer listCreateListid;

    /**
     * 创建用户ID
     */
    private String listCreateUserid;

    /**
     * 创建用户名
     */
    private String listCreateUsername;

    /**
     * 创建GID
     */
    private String listCreateGid;

    /**
     * 歌单名称
     */
    private String name;

    /**
     * 特殊名称（备用）
     */
    private String specialname;

    /**
     * 封面图
     */
    private String pic;

    /**
     * 灵活封面
     */
    private String flexibleCover;

    /**
     * 歌单简介
     */
    private String intro;

    /**
     * 标签（JSON数组）
     */
    private String tags;

    /**
     * 歌曲数量
     */
    private Integer count;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 作者列表（JSON数组）
     */
    private String authors;

    /**
     * 发布日期
     */
    private String publishDate;

    /**
     * 额外信息（JSON格式）
     */
    private String extraInfo;
}
