package com.lanfunoe.gocache.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 歌词元数据实体
 */
@Data
public class Lyric {

    /**
     * 数据库主键（自增）
     */
    @JsonIgnore
    private Long id;

    /**
     * 音频ID（关联歌曲）
     */
    @JsonProperty("audio_id")
    private Long audioId;

    /**
     * 歌曲Hash
     */
    private String hash;

    /**
     * 访问密钥
     */
    private String accesskey;

    /**
     * API返回的歌词ID（用于API请求）
     * 注意：这是KuGou API返回的id字段，与数据库主键id不同
     */
    @JsonProperty("id")
    private String lyricId;

    /**
     * 歌词语言（可能与歌曲语言不同）
     */
    private String language;

    /**
     * 歌词类型
     */
    private Integer krctype;

    /**
     * 内容类型
     */
    private Integer contenttype;

    /**
     * 内容格式
     */
    @JsonProperty("content_format")
    private Integer contentFormat;

    /**
     * 来源（官方推荐歌词等）
     */
    @JsonProperty("product_from")
    private String productFrom;

    /**
     * 歌词文件路径（相对路径）
     */
    @JsonIgnore
    private String filePath;

    /**
     * 歌词版本
     */
    @JsonProperty("lyric_version")
    private Integer lyricVersion;

    /**
     * 翻译类型
     */
    @JsonProperty("trans_type")
    private Integer transType;

    /**
     * 评分
     */
    private Integer score;

    /**
     * 上传者ID
     */
    private String uid;

    /**
     * 格式（krc/lrc/txt）
     */
    private String fmt;

    /**
     * 歌曲名称
     */
    private String songName;

    /**
     * 歌手名称
     */
    private String singer;

    /**
     * 歌曲时长（秒）
     */
    private Integer duration;

    /**
     * 额外信息（JSON格式）
     */
    @JsonIgnore
    private String extraInfo;

    /**
     * 创建时间
     */
    @JsonIgnore
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonIgnore
    private LocalDateTime updatedAt;

    // ========== 非数据库字段（运行时填充） ==========

    /**
     * 歌词内容（从文件系统加载，Base64编码）
     */
    private transient String content;

    /**
     * 解码后的歌词内容（运行时计算，不持久化）
     */
    private transient String decodeContent;

    /**
     * 状态码（API响应字段，运行时填充）
     */
    private transient Integer status;

    /**
     * 状态信息（API响应字段，运行时填充）
     */
    private transient String info;

    /**
     * 错误码（API响应字段，运行时填充）
     */
    @JsonProperty("error_code")
    private transient Integer errorCode;
}
