package com.lanfunoe.gocache.service.cache;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 响应式缓存服务接口
 * 支持WebFlux的Mono/Flux响应式编程模型
 */
public interface ReactiveCacheService {

    /**
     * 获取缓存，如果不存在则从loader加载并缓存
     *
     * @param cacheName 缓存区域名称
     * @param key       缓存键
     * @param type      值类型
     * @param loader    缓存未命中时的数据加载器
     * @param <T>       值类型
     * @return 缓存值的Mono
     */
    <T> Mono<T> get(String cacheName, String key, Class<T> type, Supplier<Mono<T>> loader);

    /**
     * 获取缓存，带自定义TTL
     *
     * @param cacheName 缓存区域名称
     * @param key       缓存键
     * @param type      值类型
     * @param ttl       过期时间
     * @param loader    缓存未命中时的数据加载器
     * @param <T>       值类型
     * @return 缓存值的Mono
     */
    <T> Mono<T> get(String cacheName, String key, Class<T> type, Duration ttl, Supplier<Mono<T>> loader);

    /**
     * 直接获取缓存值，不触发加载
     *
     * @param cacheName 缓存区域名称
     * @param key       缓存键
     * @param type      值类型
     * @param <T>       值类型
     * @return 缓存值的Mono，如果不存在返回empty
     */
    <T> Mono<T> getIfPresent(String cacheName, String key, Class<T> type);

    /**
     * 设置缓存
     *
     * @param cacheName 缓存区域名称
     * @param key       缓存键
     * @param value     缓存值
     * @param <T>       值类型
     * @return 完成信号
     */
    <T> Mono<Void> put(String cacheName, String key, T value);

    /**
     * 设置缓存，带自定义TTL
     *
     * @param cacheName 缓存区域名称
     * @param key       缓存键
     * @param value     缓存值
     * @param ttl       过期时间
     * @param <T>       值类型
     * @return 完成信号
     */
    <T> Mono<Void> put(String cacheName, String key, T value, Duration ttl);

    /**
     * 删除指定缓存项
     *
     * @param cacheName 缓存区域名称
     * @param key       缓存键
     * @return 完成信号
     */
    Mono<Void> evict(String cacheName, String key);

    /**
     * 清空指定缓存区域
     *
     * @param cacheName 缓存区域名称
     * @return 完成信号
     */
    Mono<Void> clear(String cacheName);

    /**
     * 检查缓存是否存在
     *
     * @param cacheName 缓存区域名称
     * @param key       缓存键
     * @return 是否存在
     */
    Mono<Boolean> exists(String cacheName, String key);

    /**
     * 获取缓存统计信息
     *
     * @return 统计信息Map
     */
    Mono<Map<String, Object>> getStats();

    /**
     * 获取指定缓存区域的统计信息
     *
     * @param cacheName 缓存区域名称
     * @return 统计信息Map
     */
    Mono<Map<String, Object>> getStats(String cacheName);
}
