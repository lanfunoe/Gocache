package com.lanfunoe.gocache.model;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;




/**
 * 每日推荐实体
 * 对应 daily_recommend 表
 * 复合主键: (user_id, recommend_date, song_hash)
 */
@Table("daily_recommend")
@Data
public class DailyRecommend  {

    /**
     * 用户ID（复合主键之一）
     */
    @Column("user_id")
    private String userId;

    /**
     * 推荐日期（复合主键之一）格式: YYYY-MM-DD
     */
    @Column("recommend_date")
    private String recommendDate;

    /**
     * 歌曲Hash（复合主键之一）
     */
    @Column("song_hash")
    private String songHash;

    /**
     * 音频ID
     */
    @Column("audio_id")
    private Long audioId;

    /**
     * 原始音频名
     */
    @Column("ori_audio_name")
    private String oriAudioName;

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
     * 获取复合主键
     */
    public DailyRecommendId getId() {
        return new DailyRecommendId(userId, recommendDate, songHash, audioId);
    }
}