package com.lanfunoe.gocache.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 歌手-歌曲关联实体
 * 表示歌曲由哪个歌手演唱（支持多歌手）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistSong {

    /**
     * 歌手ID（复合主键之一）
     */
    private Long artistId;

    /**
     * 音频ID（复合主键之一）
     */
    private Long audioId;

    /**
     * 歌曲哈希值
     */
    private String songHash;

    /**
     * 排序（歌手在歌曲中的顺序，例如第一歌手、第二歌手等）
     */
    private Integer sort;

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
    public ArtistSongId getId() {
        return new ArtistSongId(artistId, audioId);
    }
}
