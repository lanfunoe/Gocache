package com.lanfunoe.gocache.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * 专辑实体
 */
@Table("album")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Album {

    /**
     * 专辑ID（主键）
     */
    @Id
    @Column("album_id")
    private Long albumId;

    /**
     * 专辑名称
     */
    @Column("album_name")
    private String albumName;

    /**
     * 可变尺寸封面
     */
    @Column("sizable_cover")
    private String sizableCover;

    /**
     * 固定封面
     */
    @Column("cover")
    private String cover;

    /**
     * 发布日期
     */
    @Column("publish_date")
    private String publishDate;

    /**
     * 专辑简介
     */
    @Column("intro")
    private String intro;

    /**
     * 歌曲数量
     */
    @Column("song_count")
    private Integer songCount;

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
