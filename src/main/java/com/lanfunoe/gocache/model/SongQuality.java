package com.lanfunoe.gocache.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;




/**
 * 歌曲音质版本实体
 * 存储不同音质版本的文件信息
 */
@Table("song_quality")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongQuality {

    /**
     * 音频ID（复合主键之一）
     */
    @Column("audio_id")
    private Long audioId;

    /**
     * 音质Hash（复合主键之一）
     */
    @Column("quality_hash")
    private String qualityHash;

    /**
     * 歌曲Hash（复合主键之一）
     */
    @Column("song_hash")
    private String songHash;

    /**
     * 音质等级（0=低品质, 1=标准, 2=高品质, 3=无损）
     */
    @Column("level")
    private Integer level;

    /**
     * 比特率
     */
    @Column("bitrate")
    private Integer bitrate;

    /**
     * 文件大小（字节）
     */
    @Column("filesize")
    private Long filesize;

    /**
     * 权限等级
     */
    @Column("privilege")
    private Integer privilege;

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
    public SongQualityId getId() {
        return new SongQualityId(audioId, qualityHash, songHash);
    }
}
