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
     * 混音歌曲ID
     */
    private String mixsongid;

    /**
     * 歌曲名称
     */
    private String name;

    /**
     * 歌曲名称（备用字段，与name相同）
     */
    private String songname;

    /**
     * 音频名称
     */
    private String audioName;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 歌手名称
     */
    private String authorName;

    /**
     * 专辑ID
     */
    private Long albumId;

    /**
     * 专辑名称
     */
    private String albumName;

    /**
     * 固定封面
     */
    private String cover;

    /**
     * 可变尺寸封面
     */
    private String sizableCover;

    /**
     * 时长（秒）
     */
    private Integer duration;

    /**
     * 比特率
     */
    private Integer bitrate;

    /**
     * 文件大小
     */
    private Long filesize;

    /**
     * 文件扩展名
     */
    private String extname;

    /**
     * 备注
     */
    private String remark;

    /**
     * 权限
     */
    private Integer privilege;

    /**
     * MV Hash
     */
    private String mvhash;

    /**
     * 128K音质Hash
     */
    private String hash128;

    /**
     * 320K音质Hash
     */
    private String hash320;

    /**
     * 无损音质Hash
     */
    private String hashFlac;

    /**
     * 语言
     */
    private String language;

    /**
     * 发布日期
     */
    private String publishDate;

    /**
     * 额外信息（JSON格式）
     */
    private String extraInfo;

    // ========== 关联数据（非数据库字段）==========

    /**
     * 关联的歌手ID列表
     */
    private transient List<Long> singerIds;

    /**
     * 关联的音质版本列表
     */
    private transient List<SongQuality> qualities;
}
