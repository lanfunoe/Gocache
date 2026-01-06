package com.lanfunoe.gocache.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * 歌曲URL缓存实体
 * 对应数据库表 song_url
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("song_url")
public class SongUrl {

    @Column("audio_id")
    private Long audioId;

    @Column("hash")
    private String hash;

    @Column("quality")
    private String quality;

    @Column("url")
    private String url;

    @Column("backup_url")
    private String backupUrl;

    @Column("file_size")
    private Long fileSize;

    @Column("time_length")
    private Long timeLength;

    @Column("ext_name")
    private String extName;

    @Column("expire_time")
    private LocalDateTime expireTime;

    @Column("extra_info")
    private String extraInfo;

    @Column("created_at")
    private LocalDateTime createdAt;
}