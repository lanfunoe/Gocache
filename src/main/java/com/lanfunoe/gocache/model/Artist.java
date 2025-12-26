package com.lanfunoe.gocache.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 艺术家/歌手实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Artist extends BaseEntity {

    /**
     * 艺术家ID（主键）
     */
    private Long artistId;

    /**
     * 艺术家名称
     */
    private String authorName;

    /**
     * 可变尺寸头像
     */
    private String sizableAvatar;

    /**
     * 固定头像
     */
    private String pic;

    /**
     * 歌曲数量
     */
    private Integer songCount;

    /**
     * 专辑数量
     */
    private Integer albumCount;

    /**
     * MV数量
     */
    private Integer mvCount;

    /**
     * 粉丝数量
     */
    private Integer fansnums;

    /**
     * 艺术家简介
     */
    private String intro;

    /**
     * 详细介绍
     */
    private String longIntro;

    /**
     * 额外信息（JSON格式）
     */
    private String extraInfo;
}
