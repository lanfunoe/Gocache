package com.lanfunoe.gocache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 每日推荐复合主键
 * 由 user_id, recommend_date, song_hash 组成
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecommendId implements Serializable {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 推荐日期（格式: YYYY-MM-DD）
     */
    private String recommendDate;

    /**
     * 歌曲Hash
     */
    private String songHash;

    private Long audioId;

    /**
     * 从 DailyRecommend 实体创建主键
     */
    public static DailyRecommendId of(DailyRecommend dailyRecommend) {
        return new DailyRecommendId(
                dailyRecommend.getUserId(),
                dailyRecommend.getRecommendDate(),
                dailyRecommend.getSongHash(),
                dailyRecommend.getAudioId()
        );
    }

    /**
     * 创建主键
     */
    public static DailyRecommendId of(String userId, String recommendDate, String songHash, Long audioId) {
        return new DailyRecommendId(userId, recommendDate, songHash, audioId);
    }
}