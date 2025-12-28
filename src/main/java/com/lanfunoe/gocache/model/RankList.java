package com.lanfunoe.gocache.model;

import lombok.Data;

import java.time.LocalDateTime;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;




/**
 * 排行榜实体
 */
@Table("rank_list")
@Data
public class RankList{

    /**
     * 排行榜ID（主键）
     */
    @Id
    @Column("rank_id")
    private Long rankId;

    /**
     * 排行榜名称
     */
    @Column("rank_name")
    private String rankName;

    /**
     * 排行榜图片URL
     */
    @Column("img_url")
    private String imgUrl;

    /**
     * 专辑封面颜色
     */
    @Column("album_cover_color")
    private String albumCoverColor;

    /**
     * 排行榜简介
     */
    @Column("intro")
    private String intro;

    /**
     * 更新频率
     */
    @Column("update_frequency")
    private String updateFrequency;

    /**
     * 额外信息（JSON格式）
     */
    @Column("extra_info")
    private String extraInfo;

    /**
     * 创建时间
     */
    @Column("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
