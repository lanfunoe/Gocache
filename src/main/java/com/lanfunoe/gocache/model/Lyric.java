package com.lanfunoe.gocache.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 歌词元数据实体
 * 歌词内容存储在文件系统中
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Lyric extends BaseEntity {

    /**
     * 歌词ID（主键，自增）
     */
    private Long id;

    /**
     * 歌曲Hash
     */
    private String hash;

    /**
     * 访问密钥
     */
    private String accesskey;

    /**
     * 歌曲名称
     */
    private String songname;

    /**
     * 歌手名称
     */
    private String artistname;

    /**
     * 时长（毫秒）
     */
    private Integer duration;

    /**
     * 歌词文件路径（相对路径）
     */
    private String filePath;

    /**
     * 歌词版本
     */
    private Integer lyricVersion;

    /**
     * 翻译类型
     */
    private Integer transType;

    // ========== 非数据库字段 ==========

    /**
     * 歌词内容（从文件系统加载）
     */
    private transient String content;
}
