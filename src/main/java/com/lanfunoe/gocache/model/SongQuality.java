package com.lanfunoe.gocache.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 歌曲音质版本实体
 * 存储不同音质版本的文件信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SongQuality extends BaseEntity {

    /**
     * 音频ID（复合主键之一）
     */
    private Long audioId;

    /**
     * 音质Hash（复合主键之一）
     */
    private String qualityHash;

    /**
     * 歌曲Hash
     */
    private String songHash;

    /**
     * 音质等级（0=低品质, 1=标准, 2=高品质, 3=无损）
     */
    private Integer level;

    /**
     * 比特率
     */
    private Integer bitrate;

    /**
     * 文件大小（字节）
     */
    private Long filesize;

    /**
     * 权限等级
     */
    private Integer privilege;

    /**
     * 额外信息（JSON格式）
     */
    private String extraInfo;
}
