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
 * 艺术家/歌手实体
 */
@Table("artist")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    /**
     * 艺术家ID（主键）
     */
    @Id
    @Column("artist_id")
    private Long artistId;

    /**
     * 艺术家名称
     */
    @Column("author_name")
    private String authorName;

    /**
     * 可变尺寸头像
     */
    @Column("sizable_avatar")
    private String sizableAvatar;

    /**
     * 固定头像
     */
    @Column("pic")
    private String pic;

    /**
     * 歌曲数量
     */
    @Column("song_count")
    private Integer songCount;

    /**
     * 专辑数量
     */
    @Column("album_count")
    private Integer albumCount;

    /**
     * MV数量
     */
    @Column("mv_count")
    private Integer mvCount;

    /**
     * 粉丝数量
     */
    @Column("fansnums")
    private Integer fansnums;

    /**
     * 艺术家简介
     */
    @Column("intro")
    private String intro;

    /**
     * 详细介绍
     */
    @Column("long_intro")
    private String longIntro;

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
