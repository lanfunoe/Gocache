package com.lanfunoe.gocache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 规范化存储缓存配置
 * 从application.yml读取normalized相关配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "gocache.cache.normalized")
public class NormalizedCacheConfig {

    /** 是否启用规范化存储 */
    private boolean enabled = true;
}