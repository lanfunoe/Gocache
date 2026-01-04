package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.service.cache.CaffeineConfigManager;
import com.lanfunoe.gocache.service.cache.CombinedCacheService;
import com.lanfunoe.gocache.service.cache.ReactiveCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 缓存管理API
 * 提供缓存统计、清理等管理功能
 *
 * 重构说明：
 * - 移除了 SqliteCacheServiceImpl 的依赖
 * - PostgreSQL 缓存现在通过 Repository 管理，不再使用通用 KV 缓存
 * - 统计信息通过 CombinedCacheService 获取
 */
@RestController
@RequestMapping("/cache")
@RequiredArgsConstructor
public class CacheController extends BaseController {

    private final ReactiveCacheService caffeineCacheService;
    private final CombinedCacheService combinedCacheService;
    private final CaffeineConfigManager caffeineConfigManager;

    /**
     * 获取所有缓存统计信息
     * 包括 Caffeine 内存缓存和文件存储统计
     */
    @GetMapping("/stats")
    public Mono<Map<String, Object>> getStats() {
        return combinedCacheService.getAllStats();
    }

    /**
     * 获取Caffeine内存缓存统计
     */
    @GetMapping("/stats/caffeine")
    public Mono<Map<String, Object>> getCaffeineStats() {
        return caffeineCacheService.getStats();
    }

    /**
     * 获取指定缓存区域的统计
     */
    @GetMapping("/stats/caffeine/{cacheName}")
    public Mono<Map<String, Object>> getCaffeineStats(@PathVariable String cacheName) {
        return caffeineCacheService.getStats(cacheName);
    }

    /**
     * 清空指定的Caffeine缓存区域
     */
    @DeleteMapping("/caffeine/{cacheName}")
    public Mono<Map<String, Object>> clearCaffeineCache(@PathVariable String cacheName) {
        return caffeineCacheService.clear(cacheName)
                .thenReturn(Map.of(
                        "success", true,
                        "message", "Cache cleared: " + cacheName
                ));
    }

    /**
     * 删除指定的Caffeine缓存项
     */
    @DeleteMapping("/caffeine/{cacheName}/{key}")
    public Mono<Map<String, Object>> evictCaffeineCache(
            @PathVariable String cacheName,
            @PathVariable String key) {
        return caffeineCacheService.evict(cacheName, key)
                .thenReturn(Map.of(
                        "success", true,
                        "message", "Cache entry evicted: " + cacheName + ":" + key
                ));
    }

    /**
     * 获取L1缓存配置
     */
    @GetMapping("/l1/config")
    public Mono<Map<String, Object>> getConfig() {
        return Mono.just(Map.of(
                "success", true,
                "caffeineEnabled", caffeineConfigManager.isEnabled(),
                "message", "L1 cache config fetched"
        ));
    }

    /**
     * 修改L1缓存开关
     */
    @PutMapping("/l1/enabled")
    public Mono<Map<String, Object>> setEnabled(@RequestParam boolean enabled) {
        return caffeineConfigManager.setEnabled(enabled)
                .thenReturn(Map.of(
                        "success", true,
                        "caffeineEnabled", enabled,
                        "message", "L1 cache enabled updated, will sync to yml in 1 minute"
                ));
    }
}
