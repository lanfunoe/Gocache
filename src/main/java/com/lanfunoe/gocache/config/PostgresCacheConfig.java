package com.lanfunoe.gocache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * PostgreSQL持久化缓存配置
 * 从application.yml读取postgresql相关配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "gocache.cache.postgresql")
public class PostgresCacheConfig {

    /** 是否启用PostgreSQL */
    private boolean enabled = true;

    /** 数据库主机 */
    private String host = "localhost";

    /** 数据库端口 */
    private int port = 5432;

    /** 数据库名称 */
    private String database = "gocache";

    /** 数据库用户名 */
    private String username = "lanfunoe";

    /** 数据库密码 */
    private String password;

    /** 是否使用SSL */
    private boolean ssl = false;

    /** R2DBC连接池配置 */
    private R2dbcPoolConfig pool = new R2dbcPoolConfig();

    /**
     * R2DBC连接池配置
     */
    @Data
    public static class R2dbcPoolConfig {
        /** 初始连接数 */
        private int initialSize = 5;

        /** 最大连接数 */
        private int maxSize = 20;

        /** 最大空闲时间 */
        private Duration maxIdleTime = Duration.ofMinutes(30);

        /** 最大生命周期 */
        private Duration maxLifeTime = Duration.ofHours(1);
    }
}