package com.lanfunoe.gocache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存配置类
 * 从application.yml读取缓存相关配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cache")
public class CacheConfig {

    /** Caffeine内存缓存配置 */
    private CaffeineConfig caffeine = new CaffeineConfig();

    /** SQLite持久化配置 */
    private SqliteConfig sqlite = new SqliteConfig();

    /** 文件存储配置 */
    private StorageConfig storage = new StorageConfig();

    /** 规范化存储配置 */
    private NormalizedConfig normalized = new NormalizedConfig();

    /**
     * Caffeine缓存配置
     */
    @Data
    public static class CaffeineConfig {
        /** 各缓存区域的配置 */
        private Map<String, CacheSpec> specs = new HashMap<>();

        /** 默认最大条目数 */
        private int defaultMaxSize = 1000;

        /** 默认过期时间 */
        private Duration defaultExpireAfterWrite = Duration.ofHours(1);
    }

    /**
     * 单个缓存区域的配置
     */
    @Data
    public static class CacheSpec {
        /** 最大条目数 */
        private int maxSize = 1000;

        /** 写入后过期时间 */
        private Duration expireAfterWrite;

        /** 访问后过期时间 */
        private Duration expireAfterAccess;

        /** 是否启用统计 */
        private boolean recordStats = true;
    }

    /**
     * SQLite持久化配置
     */
    @Data
    public static class SqliteConfig {
        /** 数据库文件路径 */
        private String path = "${user.home}/.gocache/cache.db";

        /** 清理过期数据的间隔 */
        private Duration cleanupInterval = Duration.ofHours(1);

        /** HikariCP配置 */
        private HikariConfig hikari = new HikariConfig();
    }

    /**
     * HikariCP连接池配置
     */
    @Data
    public static class HikariConfig {
        /** 连接池大小 */
        private int poolSize = 5;

        /** 连接超时时间(毫秒) */
        private long connectionTimeout = 30000;

        /** 空闲超时时间(毫秒) */
        private long idleTimeout = 600000;

        /** 连接最大生命周期(毫秒) */
        private long maxLifetime = 1800000;

        /** 连接测试查询 */
        private String connectionTestQuery = "SELECT 1";

        /** 初始化失败超时(-1表示不失败) */
        private long initializationFailTimeout = -1;
    }

    /**
     * 文件存储配置
     */
    @Data
    public static class StorageConfig {
        /** 是否启用文件存储 */
        private boolean enabled = true;

        /** 存储根路径 */
        private String path = "${user.home}/.gocache/media";

        /** 歌词存储路径 */
        private String lyricsPath = "${user.home}/.gocache/lyrics";

        /** 图片缓存配置 */
        private ImageConfig image = new ImageConfig();

        /** 最大存储空间(字节) */
        private long maxSize = 10L * 1024 * 1024 * 1024; // 10GB

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
    }

    /**
     * 图片缓存配置
     */
    @Data
    public static class ImageConfig {
        /**
         * 是否启用图片缓存与URL改写
         * - false: 响应中保持上游图片URL，不触发下载
         * - true: 响应中将图片URL改写为本地 /media/image/{hash}，并异步缓存图片
         */
        private boolean enabled = false;

        /**
         * 图片缓存空间上限(字节)，<=0 时沿用 storage.maxSize
         */
        private long maxSize = 0;
    }

    /**
     * 规范化存储配置
     */
    @Data
    public static class NormalizedConfig {
        /** 是否启用规范化存储 */
        private boolean enabled = true;
    }

    /**
     * 获取指定缓存区域的配置，如果不存在则返回默认配置
     */
    public CacheSpec getCacheSpec(String cacheName) {
        CacheSpec spec = caffeine.getSpecs().get(cacheName);
        if (spec == null) {
            spec = new CacheSpec();
            spec.setMaxSize(caffeine.getDefaultMaxSize());
            spec.setExpireAfterWrite(caffeine.getDefaultExpireAfterWrite());
        }
        return spec;
    }
}
