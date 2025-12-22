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
     * 歌曲Hash（主键）
     */
    private String hash;

    /**
     * 音频ID
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
     * 文件大小（字节）
     */
    private Long filesize;

    /**
     * 比特率
     */
    private Integer bitrate;

    /**
     * 文件类型
     */
    private String filetype;

    /**
     * 专辑ID（外键）
     */
    private Long albumId;

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
}
