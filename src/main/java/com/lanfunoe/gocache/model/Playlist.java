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
     * 歌单ID（主键）
     */
    private Long specialid;

    /**
     * 歌单名称
     */
    private String specialname;

    /**
     * 歌单封面URL
     */
    private String imgurl;

    /**
     * 灵活封面
     */
    private String flexibleCover;

    /**
     * 歌单简介
     */
    private String intro;

    /**
     * 标签（逗号分隔）
     */
    private String tags;

    /**
     * 歌曲数量
     */
    private Integer songcount;

    /**
     * 播放次数
     */
    private Long playcount;

    /**
     * 收藏次数
     */
    private Integer collectcount;

    /**
     * 分享次数
     */
    private Integer shareCount;

    /**
     * 创建者用户ID
     */
    private Long userid;

    /**
     * 创建者昵称
     */
    private String nickname;

    /**
     * 创建者头像
     */
    private String userPic;

    /**
     * 歌单类型
     */
    private Integer ptype;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 是否全局
     */
    private Integer isglobal;

    /**
     * 是否发布
     */
    private Integer isPublish;

    /**
     * 是否精选
     */
    private Integer isFeatured;

    /**
     * 是否私有
     */
    private Integer isPri;

    /**
     * 全局收藏ID
     */
    private String globalCollectionId;

    /**
     * 发布日期
     */
    private String publishDate;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;
}
