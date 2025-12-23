package com.lanfunoe.gocache.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 歌曲实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Song extends BaseEntity {

    /**
     * 歌曲Hash（复合主键之一）
     */
    private String hash;

    /**
     * 音频ID（复合主键之一）
     */
    private Long audioId;

    /**
     * 歌曲名称
     */
    private String songname;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 时长（秒）
     */
    private Integer duration;

    /**
     * 专辑ID
     */
    private Long albumId;

    /**
     * 语言
     */
    private String language;

    /**
     * 流派
     */
    private String genre;

    /**
     * 发布日期
     */
    private String publishDate;

    /**
     * 播放次数
     */
    private Long playCount;

    /**
     * 收藏次数
     */
    private Long collectCount;

    /**
     * 分享次数
     */
    private Long shareCount;

    /**
     * 评论次数
     */
    private Long commentCount;

    /**
     * MV Hash
     */
    private String mvhash;

    // ========== 关联数据（非数据库字段）==========

    /**
     * 歌手名称（冗余字段，用于显示）
     */
    private transient String singername;

    /**
     * 专辑名称（冗余字段，用于显示）
     */
    private transient String albumName;

    /**
     * 关联的歌手ID列表
     */
    private transient List<Long> singerIds;

    /**
     * 关联的音质版本列表
     */
    private transient List<SongQuality> qualities;
}
