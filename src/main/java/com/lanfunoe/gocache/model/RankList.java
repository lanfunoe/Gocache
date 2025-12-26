package com.lanfunoe.gocache.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 排行榜实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RankList extends BaseEntity {

    /**
     * 排行榜ID（主键）
     */
    private Long rankId;

    /**
     * 排行榜名称
     */
    private String rankName;

    /**
     * 排行榜图片URL
     */
    private String imgUrl;

    /**
     * 专辑封面颜色
     */
    private String albumCoverColor;

    /**
     * 排行榜简介
     */
    private String intro;

    /**
     * 更新频率
     */
    private String updateFrequency;

    /**
     * 额外信息（JSON格式）
     */
    private String extraInfo;
}
