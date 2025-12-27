package com.lanfunoe.gocache.model;

import lombok.Data;

/**
 * 歌单标签分类实体
 */
@Data
public class PlaylistTagCategory  {

    /**
     * 标签ID（主键）
     */
    private Long tagId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 父级标签ID（自关联）
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sort;

    private String rowUpdateTime;


    /**
     * 额外信息（JSON格式）
     */
    private String extraInfo;
}
