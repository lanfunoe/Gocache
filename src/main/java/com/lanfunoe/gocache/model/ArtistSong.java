package com.lanfunoe.gocache.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;




/**
 * 歌手-歌曲关联实体
 * 表示歌曲由哪个歌手演唱（支持多歌手）
 */
@Table("artist_song")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistSong {

    /**
     * 歌手ID（复合主键之一）
     */
    @Column("artist_id")
    private Long artistId;

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
     * 排序（歌手在歌曲中的顺序，例如第一歌手、第二歌手等）
     */
    @Column("sort")
    private Integer sort;

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
    public ArtistSongId getId() {
        return new ArtistSongId(artistId, audioId);
    }
}
