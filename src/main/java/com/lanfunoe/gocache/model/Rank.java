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
     * 类型
     */
    private Integer type;

    /**
     * 榜单ID
     */
    private Long bangid;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类ID
     */
    private Long categoryId;
}
