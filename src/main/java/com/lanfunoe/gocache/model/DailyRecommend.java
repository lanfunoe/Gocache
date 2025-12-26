package com.lanfunoe.gocache.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 每日推荐实体
 * 对应 daily_recommend 表
 * 复合主键: (user_id, recommend_date, song_hash)
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DailyRecommend extends BaseEntity {

    /**
     * 用户ID（复合主键之一）
     */
    private String userId;

    /**
     * 推荐日期（复合主键之一）格式: YYYY-MM-DD
     */
    private String recommendDate;

    /**
     * 歌曲Hash（复合主键之一）
     */
    private String songHash;

    /**
     * 音频ID
     */
    private Long audioId;

    /**
     * 原始音频名
     */
    private String oriAudioName;

    /**
     * 歌曲名
     */
    private String songname;

    /**
     * 可变尺寸封面
     */
    private String sizableCover;

    /**
     * 歌手名
     */
    private String authorName;

    /**
     * 时长（秒）
     */
    private Integer timeLength;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 额外信息（JSON格式）
     */
    private String extraInfo;
}