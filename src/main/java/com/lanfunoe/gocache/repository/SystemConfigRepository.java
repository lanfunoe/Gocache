package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.SystemConfig;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

/**
 * 系统配置 Repository
 */
public interface SystemConfigRepository extends R2dbcRepository<SystemConfig, String> {

    Flux<SystemConfig> findByConfigKeyStartingWith(String prefix);
}