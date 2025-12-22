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
     * 歌手头像URL
     */
    private String avatar;

    /**
     * 性别（1-男，2-女，0-未知）
     */
    private Integer gender;

    /**
     * 国家
     */
    private String country;

    /**
     * 歌手简介
     */
    private String intro;

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
}
