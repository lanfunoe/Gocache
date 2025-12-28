package com.lanfunoe.gocache.model;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;




/**
 * 歌曲实体
 */
@Table("song")
@Data
public class Song {

    /**
     * 歌曲Hash（复合主键之一）
     */
    @Column("hash")
    private String hash;

    /**
     * 音频ID（复合主键之一）
     */
    @Column("audio_id")
    private Long audioId;

    /**
     * 混音歌曲ID
     */
    @Column("mixsongid")
    private String mixsongid;


    /**
     * 歌曲名称
     */
    @Column("songname")
    private String songname;

    /**
     * 音频名称
     */
    @Column("audio_name")
    private String audioName;

    /**
     * 文件名
     */
    @Column("filename")
    private String filename;

    /**
     * 歌手名称
     */
    @Column("author_name")
    private String authorName;

    /**
     * 专辑ID
     */
    @Column("album_id")
    private Long albumId;

    /**
     * 专辑名称
     */
    @Column("album_name")
    private String albumName;

    /**
     * 固定封面
     */
    @Column("cover")
    private String cover;

    /**
     * 可变尺寸封面
     */
    @Column("sizable_cover")
    private String sizableCover;

    /**
     * 时长（秒）
     */
    @Column("duration")
    private Integer duration;

    /**
     * 比特率
     */
    @Column("bitrate")
    private Integer bitrate;

    /**
     * 文件大小
     */
    @Column("filesize")
    private Long filesize;

    /**
     * 文件扩展名
     */
    @Column("extname")
    private String extname;

    /**
     * 备注
     */
    @Column("remark")
    private String remark;

    /**
     * 权限
     */
    @Column("privilege")
    private Integer privilege;

    /**
     * MV Hash
     */
    @Column("mvhash")
    private String mvhash;

    /**
     * 128K音质Hash
     */
    @Column("hash_128")
    private String hash128;

    /**
     * 320K音质Hash
     */
    @Column("hash_320")
    private String hash320;

    /**
     * 无损音质Hash
     */
    @Column("hash_flac")
    private String hashFlac;

    /**
     * 语言
     */
    @Column("language")
    private String language;

    /**
     * 发布日期
     */
    @Column("publish_date")
    private String publishDate;

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

    // ========== 关联数据（非数据库字段）==========

    /**
     * 关联的歌手ID列表
     */
    private transient List<Long> singerIds;

    /**
     * 关联的音质版本列表
     */
    private transient List<SongQuality> qualities;

    /**
     * 获取复合主键
     */
    public SongHashId getId() {
        return new SongHashId(audioId, hash);
    }
}
