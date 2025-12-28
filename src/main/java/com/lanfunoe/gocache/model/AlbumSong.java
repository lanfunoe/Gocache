package com.lanfunoe.gocache.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


/**
 * 专辑-歌曲关联实体
 * 表示歌曲属于哪个专辑
 */
@Table("album_song")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumSong{

    /**
     * 专辑ID（复合主键之一）
     */
    @Column("album_id")
    private Long albumId;

    /**
     * 音频ID（复合主键之一）
     */
    @Column("audio_id")
    private Long audioId;

    /**
     * 歌曲哈希值
     */
    @Column("song_hash")
    private String songHash;

    /**
     * 曲目编号（专辑中的第几首歌）
     */
    private Integer trackNo;

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
    public AlbumSongId getId() {
        return new AlbumSongId(albumId, audioId);
    }
}
