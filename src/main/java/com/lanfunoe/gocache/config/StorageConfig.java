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
    private int downloadConcurrency = 10;

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

    /** 下载队列配置 */
    private QueueConfig queue = new QueueConfig();

    /** 下载连接池配置 */
    private PoolConfig pool = new PoolConfig();

    /**
     * 下载队列配置
     */
    @Data
    public static class QueueConfig {
        /** 最小并发数 */
        private int minConcurrency = 3;

        /** 最大并发数 */
        private int maxConcurrency = 80;

        /** CPU使用率阈值(0-1) */
        private double cpuThreshold = 0.85;

        /** 内存使用率阈值(0-1) */
        private double memoryThreshold = 0.9;

        /** 并发调整间隔 */
        private Duration adjustmentInterval = Duration.ofSeconds(30);

        /** 最大缓冲区大小 */
        private int maxBufferSize = 2000;

        /** 最大活跃任务数 */
        private int maxActiveTasks = 10000;

        /** 任务过期时间，超过此时间的任务将被清理 */
        private Duration staleTaskTimeout = Duration.ofMinutes(30);

        /** 过期任务清理间隔 */
        private Duration staleTaskCleanupInterval = Duration.ofMinutes(5);
    }

    /**
     * 下载连接池配置
     */
    @Data
    public static class PoolConfig {
        /** 最大连接数 */
        private int maxConnections = 50;

        /** 连接超时(毫秒) */
        private int connectTimeout = 5000;

        /** 读超时(秒) */
        private int readTimeout = 600;

        /** 写超时(秒) */
        private int writeTimeout = 30;

        /** 连接获取超时(毫秒) */
        private long pendingAcquireTimeout = 5000;

        /** 是否启用Keep-Alive */
        private boolean keepAlive = true;

        /** 是否启用TCP_NODELAY */
        private boolean tcpNoDelay = true;

        /** 是否启用压缩 */
        private boolean compress = true;
    }
}