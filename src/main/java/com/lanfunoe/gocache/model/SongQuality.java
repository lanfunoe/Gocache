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
     * 自增主键
     */
    private Long id;

    /**
     * 音频ID（关联 songs 表）
     */
    private Long audioId;

    /**
     * 歌曲Hash（关联 songs 表的 hash）
     */
    private String songHash;

    /**
     * 音质类型：128, 320, flac, sq, high, super
     */
    private String qualityType;

    /**
     * 该音质版本的文件Hash
     */
    private String qualityHash;

    /**
     * 文件大小（字节）
     */
    private Long filesize;

    /**
     * 比特率
     */
    private Integer bitrate;

    /**
     * 文件类型（mp3, flac 等）
     */
    private String filetype;

    /**
     * 权限等级
     */
    private Integer privilege;

    /**
     * 本地缓存路径
     */
    private String localPath;

    /**
     * 音质类型枚举
     */
    public enum QualityType {
        Q128("128", 128),
        Q320("320", 320),
        FLAC("flac", 0),
        SQ("sq", 0),
        HIGH("high", 0),
        SUPER("super", 0);

        private final String code;
        private final int defaultBitrate;

        QualityType(String code, int defaultBitrate) {
            this.code = code;
            this.defaultBitrate = defaultBitrate;
        }

        public String getCode() {
            return code;
        }

        public int getDefaultBitrate() {
            return defaultBitrate;
        }

        public static QualityType fromCode(String code) {
            for (QualityType type : values()) {
                if (type.code.equalsIgnoreCase(code)) {
                    return type;
                }
            }
            return Q128;
        }
    }
}
