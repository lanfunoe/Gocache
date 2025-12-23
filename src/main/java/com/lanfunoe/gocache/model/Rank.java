package com.lanfunoe.gocache.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 排行榜实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Rank extends BaseEntity {

    /**
     * 排行榜ID（主键）
     */
    private Long rankid;

    /**
     * 排行榜名称
     */
    private String rankname;

    /**
     * 排行榜简介
     */
    private String intro;

    /**
     * 排行榜图片URL
     */
    private String imgurl;

    /**
     * 9宫格图
     */
    private String img9;

    /**
     * 横幅图
     */
    private String banner9;

    /**
     * 横幅URL
     */
    private String bannerurl;

    /**
     * 专辑图
     */
    private String albumImg9;

    /**
     * 分享背景
     */
    private String shareBg;

    /**
     * 分享logo
     */
    private String shareLogo;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 排行榜类型
     */
    private Integer ranktype;

    /**
     * 分类
     */
    private Integer classify;

    /**
     * 榜单ID
     */
    private Long bangid;

    /**
     * 排行榜CID
     */
    private Long rankCid;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 更新频率
     */
    private String updateFrequency;

    /**
     * 更新频率类型
     */
    private Integer updateFrequencyType;

    /**
     * 播放次数
     */
    private Long playTimes;

    /**
     * 期数
     */
    private Integer issue;

    /**
     * 是否定时
     */
    private Integer isTiming;

    /**
     * 是否有子榜
     */
    private Integer haschildren;

    /**
     * 区域
     */
    private String zone;

    /**
     * 跳转URL
     */
    private String jumpUrl;
}
