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
     * 父级标签ID（自关联）
     */
    private Long parentid;

    /**
     * 排序
     */
    private Integer sort;
}
