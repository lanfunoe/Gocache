package com.lanfunoe.gocache.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 歌手实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Singer extends BaseEntity {

    /**
     * 歌手ID（主键）
     */
    private Long singerid;

    /**
     * 歌手名称
     */
    private String singername;

    /**
     * 拼音首字母
     */
    private String pinyinInitial;

    /**
     * 歌手头像URL
     */
    private String avatar;

    /**
     * 性别（1-男，2-女，0-未知）
     */
    private Integer gender;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 国家
     */
    private String country;

    /**
     * 地区ID
     */
    private String areaId;

    /**
     * 语言
     */
    private String language;

    /**
     * 歌手简介
     */
    private String intro;

    /**
     * 详细介绍（JSON格式）
     */
    private String longIntro;

    /**
     * 身份标识
     */
    private Integer identity;

    /**
     * 是否发布
     */
    private Integer isPublish;

    /**
     * 粉丝数量
     */
    private Integer fansCount;

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
     * 用户状态
     */
    private Integer userStatus;
}
