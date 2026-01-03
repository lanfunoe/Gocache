package com.lanfunoe.gocache.model;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;




/**
 * 歌单实体
 */
@Table("playlist")
@Data
public class Playlist  {

    /**
     * 全局收藏ID（复合主键之一）
     */
    @Column("global_collection_id")
    private String globalCollectionId;

    /**
     * 分类ID（复合主键之一）
     */
    @Column("category_id")
    private Integer categoryId;

    /**
     * 列表ID
     */
    @Column("listid")
    private Integer listid;

    /**
     * 创建用户ID
     */
    @Column("list_create_userid")
    private Long listCreateUserid;

    /**
     * 创建用户名
     */
    @Column("list_create_username")
    private String listCreateUsername;

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
     * 歌曲数量
     */
    @Column("count")
    private Integer count;

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
     * 获取复合主键
     */
    public PlaylistId getId() {
        return new PlaylistId(globalCollectionId, categoryId);
    }
}
