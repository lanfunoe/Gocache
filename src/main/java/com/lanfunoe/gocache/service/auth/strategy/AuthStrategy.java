package com.lanfunoe.gocache.service.auth.strategy;

import reactor.core.publisher.Mono;
import java.util.Map;

/**
 * 认证策略接口
 * 定义认证行为的抽象接口
 */
public interface AuthStrategy {

    /**
     * 执行认证
     *
     * @param params 认证参数
     * @return 认证结果
     */
    Mono<Map<String, Object>> authenticate(Map<String, Object> params);

    /**
     * 获取策略类型
     *
     * @return 策略类型标识
     */
    String getStrategyType();

    /**
     * 验证参数
     *
     * @param params 认证参数
     * @return 验证结果
     */
    default boolean validateParams(Map<String, Object> params) {
        return params != null && !params.isEmpty();
    }
}