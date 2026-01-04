package com.lanfunoe.gocache.model;

import lombok.Data;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;



/**
 * 歌单标签分类实体
 */
@Table("tag")
@Data
public class Tag  {

    /**
     * 标签ID（主键）
     */
    @Id
    @Column("tag_id")
    private Long tagId;
    /**
     * 标签名称
     */
    @Column("tag_name")
    private String tagName;

    /**
     * 父级标签ID（自关联）
     */
    @Column("parent_id")
    private Long parentId;

    @Column("row_update_time")
    private String rowUpdateTime;

    /**
     * 额外信息（JSON格式）
     */
    @Column("extra_info")
    private String extraInfo;
}
