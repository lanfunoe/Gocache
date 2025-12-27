package com.lanfunoe.gocache.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQLite DataSource configuration using HikariCP
 * Provides a production-ready connection pool for SQLite cache
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SQLiteDataSourceConfig {

    private final CacheConfig cacheConfig;
    private HikariDataSource dataSource;

    @Bean(name = "sqliteDataSource")
    public DataSource sqliteDataSource() {
        CacheConfig.SqliteConfig sqliteConfig = cacheConfig.getSqlite();
        CacheConfig.HikariConfig hikariConfig = sqliteConfig.getHikari();

        // Resolve path, replacing ${user.home}
        String dbPath = sqliteConfig.getPath().replace("${user.home}", System.getProperty("user.home"));

        // Ensure directory exists
        try {
            Path dbDir = Paths.get(dbPath).getParent();
            if (dbDir != null && !Files.exists(dbDir)) {
                Files.createDirectories(dbDir);
                log.info("Created cache directory: {}", dbDir);
            }
        } catch (Exception e) {
            log.error("Failed to create cache directory", e);
            throw new RuntimeException("Failed to create cache directory: " + Paths.get(dbPath).getParent(), e);
        }

        HikariConfig config = getHikariConfig(dbPath, hikariConfig);

        // Create the data source
        dataSource = new HikariDataSource(config);

        log.info("SQLite HikariCP DataSource initialized at: {} with pool size: {}",
                dbPath, hikariConfig.getPoolSize());

        return dataSource;
    }

    private static @NonNull HikariConfig getHikariConfig(String dbPath, CacheConfig.HikariConfig hikariConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(hikariConfig.getJdbcUrlPrefix() + dbPath);
        config.setPoolName(hikariConfig.getPoolName());

        // Pool sizing
        config.setMaximumPoolSize(hikariConfig.getPoolSize());
        config.setMinimumIdle(Math.min(2, hikariConfig.getPoolSize()));

        // Timeouts
        config.setConnectionTimeout(hikariConfig.getConnectionTimeout());
        config.setIdleTimeout(hikariConfig.getIdleTimeout());
        config.setMaxLifetime(hikariConfig.getMaxLifetime());

        // Connection testing
        config.setConnectionTestQuery(hikariConfig.getConnectionTestQuery());

        // SQLite-specific settings
        // Disable auto-commit for better control
        config.setAutoCommit(true);

        // Don't fail if DB doesn't exist yet (SQLite creates it)
        config.setInitializationFailTimeout(hikariConfig.getInitializationFailTimeout());

        // Ensure all connections use WAL mode by setting connection initialization SQL
        config.setConnectionInitSql("PRAGMA journal_mode=WAL; PRAGMA synchronous=NORMAL;");

        return config;
    }



    @PreDestroy
    public void destroy() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            log.info("SQLite HikariCP DataSource closed");
        }
    }
}