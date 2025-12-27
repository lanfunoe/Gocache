package com.lanfunoe.gocache.model;

import lombok.Data;

/**
 * 图片资源实体
 * 统一管理各实体的图片资源
 */
@Data
public class Image {

    /**
     * 自增主键
     */
    private Long id;

    /**
     * 实体类型：singer, album, playlist, user, song
     */
    private String entityType;

    /**
     * 实体ID（可能是复合ID如 "audio_id:hash"）
     */
    private String entityId;

    /**
     * 尺寸类型：original, large, medium, small, thumbnail
     */
    private String sizeType;

    /**
     * 图片URL
     */
    private String url;

    /**
     * 本地缓存路径
     */
    private String localPath;

    /**
     * 图片宽度
     */
    private Integer width;

    /**
     * 图片高度
     */
    private Integer height;

    /**
     * 实体类型枚举
     */
    public enum EntityType {
        SINGER("singer"),
        ALBUM("album"),
        PLAYLIST("playlist"),
        USER("user"),
        SONG("song"),
        RANK("rank");

        private final String code;

        EntityType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static EntityType fromCode(String code) {
            for (EntityType type : values()) {
                if (type.code.equalsIgnoreCase(code)) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * 尺寸类型枚举
     */
    public enum SizeType {
        ORIGINAL("original"),
        LARGE("large"),
        MEDIUM("medium"),
        SMALL("small"),
        THUMBNAIL("thumbnail");

        private final String code;

        SizeType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static SizeType fromCode(String code) {
            for (SizeType type : values()) {
                if (type.code.equalsIgnoreCase(code)) {
                    return type;
                }
            }
            return ORIGINAL;
        }
    }

    /**
     * 构建复合实体ID
     */
    public static String buildEntityId(Long audioId, String hash) {
        return audioId + ":" + hash;
    }

    /**
     * 构建单一实体ID
     */
    public static String buildEntityId(Long id) {
        return String.valueOf(id);
    }
}
