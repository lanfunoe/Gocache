package com.lanfunoe.gocache.model;

import lombok.Data;

import java.time.LocalDateTime;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;




/**
 * 歌单实体
 */
@Table("playlist")
@Data
public class Playlist  {

    /**
     * 全局收藏ID（主键）
     */
    @Id
    @Column("global_collection_id")
    private String globalCollectionId;

    /**
     * 列表ID
     */
    @Column("listid")
    private Integer listid;

    /**
     * 创建列表ID
     */
    @Column("list_create_listid")
    private Integer listCreateListid;

    /**
     * 创建用户ID
     */
    @Column("list_create_userid")
    private String listCreateUserid;

    /**
     * 创建用户名
     */
    @Column("list_create_username")
    private String listCreateUsername;

    /**
     * 创建GID
     */
    @Column("list_create_gid")
    private String listCreateGid;

    /**
     * 歌单名称
     */
    @Column("name")
    private String name;

    /**
     * 特殊名称（备用）
     */
    @Column("specialname")
    private String specialname;

    /**
     * 封面图
     */
    @Column("pic")
    private String pic;

    /**
     * 灵活封面
     */
    @Column("flexible_cover")
    private String flexibleCover;

    /**
     * 歌单简介
     */
    @Column("intro")
    private String intro;

    /**
     * 标签（JSON数组）
     */
    @Column("tags")
    private String tags;

    /**
     * 歌曲数量
     */
    @Column("count")
    private Integer count;

    /**
     * 排序
     */
    @Column("sort")
    private Integer sort;

    /**
     * 作者列表（JSON数组）
     */
    @Column("authors")
    private String authors;

    /**
     * 发布日期
     */
    @Column("publish_date")
    private String publishDate;

    /**
     * 额外信息（JSON格式）
     */
    @Column("extra_info")
    private String extraInfo;

    /**
     * 创建时间
     */
    @Column("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
