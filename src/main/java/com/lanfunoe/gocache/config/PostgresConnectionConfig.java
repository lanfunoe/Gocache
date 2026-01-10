package com.lanfunoe.gocache.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

/**
 * PostgreSQL R2DBC连接配置
 * 配置R2DBC连接池、DatabaseClient、R2dbcEntityTemplate和事务管理器
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableR2dbcRepositories(basePackages = "com.lanfunoe.gocache.repository")
@EnableTransactionManagement
public class PostgresConnectionConfig extends AbstractR2dbcConfiguration {

    private final PostgresCacheConfig postgresCacheConfig;

    /**
     * 配置R2DBC连接工厂（使用连接池）
     */
    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        PostgresCacheConfig postgresConfig = postgresCacheConfig;
        PostgresCacheConfig.R2dbcPoolConfig poolConfig = postgresCacheConfig.getPool();

        // 创建PostgreSQL连接工厂选项
        ConnectionFactoryOptions.Builder builder = ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(PROTOCOL, "postgresql")
                .option(HOST, postgresConfig.getHost())
                .option(PORT, postgresConfig.getPort())
                .option(DATABASE, postgresConfig.getDatabase())
                .option(USER, postgresConfig.getUsername())
                .option(PASSWORD, postgresConfig.getPassword());

        if (postgresConfig.isSsl()) {
            builder.option(SSL, true);
        }

        ConnectionFactoryOptions postgresOptions = builder.build();

        // 创建底层的PostgreSQL连接工厂
        ConnectionFactory postgresConnectionFactory = ConnectionFactories.get(postgresOptions);

        // 创建连接池配置
        ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder(postgresConnectionFactory)
                .initialSize(poolConfig.getInitialSize())
                .maxSize(poolConfig.getMaxSize())
                .maxIdleTime(poolConfig.getMaxIdleTime())
                .maxCreateConnectionTime(poolConfig.getMaxLifeTime())
                .build();

        ConnectionPool connectionPool = new ConnectionPool(poolConfiguration);

        log.info("PostgreSQL R2DBC connection factory initialized: {}@{}:{}/{}",
                postgresConfig.getUsername(),
                postgresConfig.getHost(),
                postgresConfig.getPort(),
                postgresConfig.getDatabase());

        return connectionPool;
    }


    /**
     * 配置 R2dbcEntityTemplate（用于 upsert 和动态查询）
     */
    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }


    /**
     * 配置响应式事务管理器
     * 注意：AbstractR2dbcConfiguration 已经提供了这个 Bean，这里可以省略
     */
    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
}
