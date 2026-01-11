package com.lanfunoe.gocache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * 文件存储缓存配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "gocache.cache.storage")
public class StorageConfig {

    /** 是否启用文件存储 */
    private boolean enabled = true;

    /** 歌曲存储路径 */
    private String songPath = "${user.home}/.gocache/songs";

    /** 歌词存储路径 */
    private String lyricsPath = "${user.home}/.gocache/lyrics";

    /** 图片存储路径 */
    private String imgPath = "${user.home}/.gocache/images";

    /** 本地服务基础URL，用于替换歌曲URL */
    private String baseUrl = "http://localhost:" + System.getProperty("server.port");

    /** 最大存储空间(字节) */
    private Long maxSize = 10L * 1024 * 1024 * 1024; // 10GB

    /** 触发清理的阈值(0-1) */
    private double cleanupThreshold = 0.9;

    /** 单次清理的目标使用率 */
    private double cleanupTarget = 0.7;

    /** 下载并发数 */
    private int downloadConcurrency = 3;

    /** 下载超时时间 */
    private Duration downloadTimeout = Duration.ofMinutes(10);

    /** 下载重试次数 */
    private int downloadRetries = 3;

    /** 下载重试退避基准 */
    private Duration downloadRetryBackoff = Duration.ofSeconds(2);

    /** 下载重试退避上限 */
    private Duration downloadRetryMaxBackoff = Duration.ofSeconds(30);

    /** 流式下载是否启用 */
    private boolean downloadStreamingEnabled = true;

    /** 单个文件下载最大大小(字节)，0表示不限制 */
    private long downloadMaxFileSize = 0L;
}