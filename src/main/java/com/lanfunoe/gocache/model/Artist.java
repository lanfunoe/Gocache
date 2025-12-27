package com.lanfunoe.gocache.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 艺术家/歌手实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    /**
     * 艺术家ID（主键）
     */
    private Long artistId;

    /**
     * 艺术家名称
     */
    private String authorName;

    /**
     * 可变尺寸头像
     */
    private String sizableAvatar;

    /**
     * 固定头像
     */
    private String pic;

    /**
     * 歌曲数量
     */
    private Integer songCount;

    /**
     * 专辑数量
     */
    private Integer albumCount;

    /**
     * MV数量
     */
    private Integer mvCount;

    /**
     * 粉丝数量
     */
    private Integer fansnums;

    /**
     * 艺术家简介
     */
    private String intro;

    /**
     * 详细介绍
     */
    private String longIntro;

    /**
     * 额外信息（JSON格式）
     */
    private String extraInfo;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
