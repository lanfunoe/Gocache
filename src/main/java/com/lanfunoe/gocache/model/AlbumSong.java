package com.lanfunoe.gocache.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 专辑-歌曲关联实体
 * 表示歌曲属于哪个专辑
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumSong{

    /**
     * 专辑ID（复合主键之一）
     */
    private Long albumId;

    /**
     * 音频ID（复合主键之一）
     */
    private Long audioId;

    /**
     * 歌曲哈希值
     */
    private String songHash;

    /**
     * 曲目编号（专辑中的第几首歌）
     */
    @Builder.Default
    private Integer trackNo = 0;

    /**
     * 额外信息（JSON格式）
     */
    private String extraInfo;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 获取复合主键
     */
    public AlbumSongId getId() {
        return new AlbumSongId(albumId, audioId);
    }
}
