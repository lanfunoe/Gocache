package com.lanfunoe.gocache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Caffeine内存缓存配置
 * 从application.yml读取caffeine相关配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "gocache.cache.caffeine")
public class CaffeineCacheConfig {

    /** 是否启用Caffeine缓存 */
    private boolean enabled = true;

    /** 各缓存区域的配置 */
    private Map<String, CacheSpec> specs = new HashMap<>();

    /** 默认最大条目数 */
    private int defaultMaxSize = 1000;

    /** 默认过期时间 */
    private Duration defaultExpireAfterWrite = Duration.ofHours(1);

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
}