package com.lanfunoe.gocache.service.music.strategy;

import reactor.core.publisher.Mono;
import java.util.Map;

/**
 * URL生成策略接口
 * 定义不同类型音乐URL生成策略的抽象接口
 */
public interface UrlStrategy {

    /**
     * 生成音乐URL
     *
     * @param params 生成参数
     * @return 生成的URL信息
     */
    Mono<Map<String, Object>> generateUrl(Map<String, Object> params);

    String getStrategyType();

    /**
     * 获取支持的音质列表
     *
     * @return 支持的音质列表
     */
    default String[] getSupportedQualities() {
        return new String[]{"standard", "high", "super", "excellent"};
    }
    /**
     * 验证参数
     *
     * @param params 生成参数
     * @return 验证结果
     */
    default boolean validateParams(Map<String, Object> params) {
        return params != null && !params.isEmpty();
    }

}