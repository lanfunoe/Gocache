package com.lanfunoe.gocache.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 每日推荐实体
 * 对应 daily_recommend 表
 * 复合主键: (user_id, recommend_date, song_hash)
 */
@Data
public class DailyRecommend  {

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
     * 额外信息（JSON格式）
     */
    private String extraInfo;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}