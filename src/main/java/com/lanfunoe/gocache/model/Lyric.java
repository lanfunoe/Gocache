package com.lanfunoe.gocache.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;




/**
 * 歌词元数据实体
 */
@Table("lyric")
@Data
public class Lyric {

    /**
     * 数据库主键（自增）
     */
    @JsonIgnore
    @Id
    @Column("id")
    private Long id;

    /**
     * 音频ID（关联歌曲）
     */
    @JsonProperty("audio_id")
    @Column("audio_id")
    private Long audioId;

    /**
     * 歌曲Hash
     */
    @Column("hash")
    private String hash;

    /**
     * 访问密钥
     */
    @Column("accesskey")
    private String accesskey;

    /**
     * API返回的歌词ID（用于API请求）
     * 注意：这是KuGou API返回的id字段，与数据库主键id不同
     */
    @JsonProperty("id")
    @Column("lyric_id")
    private String lyricId;

    /**
     * 歌词语言（可能与歌曲语言不同）
     */
    @Column("language")
    private String language;

    /**
     * 歌词类型
     */
    @Column("krctype")
    private Integer krctype;

    /**
     * 内容类型
     */
    @Column("contenttype")
    private Integer contenttype;

    /**
     * 内容格式
     */
    @JsonProperty("content_format")
    @Column("content_format")
    private Integer contentFormat;

    /**
     * 来源（官方推荐歌词等）
     */
    @JsonProperty("product_from")
    @Column("product_from")
    private String productFrom;

    /**
     * 歌词文件路径（相对路径）
     */
    @JsonIgnore
    @Column("file_path")
    private String filePath;

    /**
     * 歌词版本
     */
    @JsonProperty("lyric_version")
    @Column("lyric_version")
    private Integer lyricVersion;

    /**
     * 翻译类型
     */
    @JsonProperty("trans_type")
    @Column("trans_type")
    private Integer transType;

    /**
     * 评分
     */
    @Column("score")
    private Integer score;

    /**
     * 上传者ID
     */
    @Column("uid")
    private String uid;

    /**
     * 格式（krc/lrc/txt）
     */
    @Column("fmt")
    private String fmt;

    /**
     * 歌曲名称
     */
    @Column("song_name")
    private String songName;

    /**
     * 歌手名称
     */
    @Column("singer")
    private String singer;

    /**
     * 歌曲时长（秒）
     */
    @Column("duration")
    private Integer duration;

    /**
     * 额外信息（JSON格式）
     */
    @JsonIgnore
    @Column("extra_info")
    private String extraInfo;

    /**
     * 创建时间
     */
    @JsonIgnore
    @Column("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonIgnore
    @Column("updated_at")
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
