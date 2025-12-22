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
     * 歌单简介
     */
    private String intro;

    /**
     * 歌曲数量
     */
    private Integer songcount;

    /**
     * 播放次数
     */
    private Long playcount;

    /**
     * 分享次数
     */
    private Integer shareCount;

    /**
     * 创建者用户ID（外键）
     */
    private Long userid;

    /**
     * 创建者昵称（冗余字段）
     */
    private String nickname;

    /**
     * 歌单类型
     */
    private Integer ptype;

    /**
     * 是否全局
     */
    private Integer isglobal;

    /**
     * 创建时间戳
     */
    private Long dateline;
}
