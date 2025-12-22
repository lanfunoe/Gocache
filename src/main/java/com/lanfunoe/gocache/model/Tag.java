package com.lanfunoe.gocache.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标签实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Tag extends BaseEntity {

    /**
     * 标签ID（主键）
     */
    private Long tagid;

    /**
     * 标签名称
     */
    private String tagname;

    /**
     * 父级标签ID（自关联外键）
     */
    private Long parentid;

    /**
     * 是否热门
     */
    private Integer hot;

    /**
     * 是否发布
     */
    private Integer publish;

    /**
     * 标签图片URL
     */
    private String imgurl;

    /**
     * 标签颜色
     */
    private String color;

    /**
     * 特殊类型
     */
    private Integer specialType;
}
