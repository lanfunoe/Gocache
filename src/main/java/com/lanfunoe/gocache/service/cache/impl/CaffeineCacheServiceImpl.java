package com.lanfunoe.gocache.service.cache.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.lanfunoe.gocache.config.CacheConfig;
import com.lanfunoe.gocache.service.cache.CacheNames;
import com.lanfunoe.gocache.service.cache.CaffeineConfigManager;
import com.lanfunoe.gocache.service.cache.ReactiveCacheService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Caffeine内存缓存服务实现
 * 提供高性能的本地缓存，支持响应式编程
 *
 * 优化说明: 使用预定义缓存区域替代 ConcurrentHashMap 动态创建
 * - 所有缓存区域在启动时初始化，避免内存泄漏风险
 * - 移除动态缓存创建逻辑，提高性能和可靠性
 * - 支持配置文件覆盖默认缓存配置
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CaffeineCacheServiceImpl implements ReactiveCacheService {

    private final CacheConfig cacheConfig;
    private final ObjectMapper objectMapper;
    private final CaffeineConfigManager caffeineConfigManager;

    // 预定义所有缓存区域，使用不可变 Map 替代 ConcurrentHashMap
    private Map<String, AsyncCache<String, Object>> caches;

    @PostConstruct
    public void init() {
        Map<String, AsyncCache<String, Object>> cacheMap = new HashMap<>();

        // 初始化所有已知的缓存区域，严格按照 CacheNames 定义
        cacheMap.put(CacheNames.LYRICS, createCache(2000, null, Duration.ofHours(12)));
        cacheMap.put(CacheNames.ARTIST_INFO, createCache(1000, Duration.ofHours(168), null));
        cacheMap.put(CacheNames.CATEGORIES, createCache(100, Duration.ofHours(24), null));
        cacheMap.put(CacheNames.TAGS, createCache(500, Duration.ofHours(24), null));
        cacheMap.put(CacheNames.EVERYDAY_RECOMMEND, createCache(100, Duration.ofHours(24), null));
        cacheMap.put(CacheNames.IMAGES, createCache(5000, Duration.ofHours(24), null));
        cacheMap.put(CacheNames.SONGS, createCache(1000, Duration.ofHours(24), null));
        cacheMap.put(CacheNames.SEARCH_HOT, createCache(50, Duration.ofMinutes(30), null));
        cacheMap.put(CacheNames.SEARCH_RESULTS, createCache(1000, Duration.ofMinutes(5), null));
        cacheMap.put(CacheNames.USER_INFO, createCache(500, Duration.ofHours(1), null));
        cacheMap.put(CacheNames.PLAYLIST_DETAIL, createCache(500, Duration.ofHours(2), null));
        cacheMap.put(CacheNames.ARTIST_WORKS, createCache(500, Duration.ofHours(2), null));
        cacheMap.put(CacheNames.TOP_PLAYLIST, createCache(1000, Duration.ofMinutes(30), null));

        // 从配置文件加载自定义配置覆盖默认值
        cacheConfig.getCaffeine().getSpecs().forEach((name, spec) -> {
            if (cacheMap.containsKey(name)) {
                // 覆盖预定义的缓存配置
                cacheMap.put(name, createCache(spec.getMaxSize(), spec.getExpireAfterWrite(), spec.getExpireAfterAccess()));
                log.info("Overriding cache config for {}: maxSize={}, expireAfterWrite={}, expireAfterAccess={}",
                    name, spec.getMaxSize(), spec.getExpireAfterWrite(), spec.getExpireAfterAccess());
            } else {
                log.warn("Unknown cache region in config: {}. Available regions: {}",
                    name, String.join(", ", cacheMap.keySet()));
            }
        });

        // 创建不可变视图，防止意外修改
        this.caches = Map.copyOf(cacheMap);
        log.info("Caffeine cache initialized with {} regions", caches.size());
    }

    private AsyncCache<String, Object> createCache(int maxSize, Duration expireAfterWrite, Duration expireAfterAccess) {
        Caffeine<Object, Object> builder = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .recordStats();

        if (expireAfterWrite != null) {
            builder.expireAfterWrite(expireAfterWrite);
        }
        if (expireAfterAccess != null) {
            builder.expireAfterAccess(expireAfterAccess);
        }

        return builder.buildAsync();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Mono<T> getIfPresent(String cacheName, String key, Class<T> type) {
        AsyncCache<String, Object> cache = getCache(cacheName);
        if (cache == null) {
            return Mono.empty();
        }

        CompletableFuture<Object> future = cache.getIfPresent(key);
        if (future == null) {
            return Mono.empty();
        }

        return Mono.fromFuture(future)
                .map(v -> convertValue(v, type))
                .doOnNext(v -> log.info("L1 Cache HIT: {}:{}", cacheName, key));
    }

    @Override
    public <T> void put(String cacheName, String key, T value) {
        // 检查L1缓存开关
        if (!caffeineConfigManager.isEnabled()) {
            log.trace("L1 cache disabled, ignoring put for {}:{}", cacheName, key);
            return;
        }

        AsyncCache<String, Object> cache = getCache(cacheName);
        if (cache == null) {
            log.warn("Attempted to put to unknown cache region: {}", cacheName);
            return;
        }

        cache.put(key, CompletableFuture.completedFuture(value));
        log.info("Cache PUT: {}:{}", cacheName, key);
    }

    @Override
    public <T> void put(String cacheName, String key, T value, Duration ttl) {
        // Caffeine不支持单个key的TTL，使用默认行为
        put(cacheName, key, value);
    }

    @Override
    public Mono<Void> evict(String cacheName, String key) {
        if (!caffeineConfigManager.isEnabled()) {
            return Mono.empty();
        }

        AsyncCache<String, Object> cache = getCache(cacheName);
        if (cache != null) {
            cache.synchronous().invalidate(key);
            log.debug("Cache EVICT: {}:{}", cacheName, key);
        }
        return Mono.empty();
    }

    @Override
    public Mono<Void> clear(String cacheName) {
        if (!caffeineConfigManager.isEnabled()) {
            return Mono.empty();
        }

        AsyncCache<String, Object> cache = getCache(cacheName);
        if (cache != null) {
            cache.synchronous().invalidateAll();
            log.info("Cache CLEAR: {}", cacheName);
        }
        return Mono.empty();
    }

    @Override
    public Mono<Boolean> exists(String cacheName, String key) {
        AsyncCache<String, Object> cache = getCache(cacheName);
        if (cache == null) {
            return Mono.just(false);
        }
        return Mono.just(cache.getIfPresent(key) != null);
    }

    @Override
    public Mono<Map<String, Object>> getStats() {
        Map<String, Object> allStats = new HashMap<>();
        allStats.put("caffeineEnabled", caffeineConfigManager.isEnabled()); // 新增
        caches.forEach((name, cache) -> {
            allStats.put(name, buildStatsMap(cache.synchronous().stats()));
        });
        return Mono.just(allStats);
    }

    @Override
    public Mono<Map<String, Object>> getStats(String cacheName) {
        AsyncCache<String, Object> cache = getCache(cacheName);
        if (cache == null) {
            return Mono.just(Map.of("error", "Cache not found: " + cacheName));
        }
        return Mono.just(buildStatsMap(cache.synchronous().stats()));
    }

    private Map<String, Object> buildStatsMap(CacheStats stats) {
        Map<String, Object> map = new HashMap<>();
        map.put("hitCount", stats.hitCount());
        map.put("missCount", stats.missCount());
        map.put("hitRate", stats.hitRate());
        map.put("missRate", stats.missRate());
        map.put("loadSuccessCount", stats.loadSuccessCount());
        map.put("loadFailureCount", stats.loadFailureCount());
        map.put("evictionCount", stats.evictionCount());
        map.put("averageLoadPenalty", stats.averageLoadPenalty());
        return map;
    }

    /**
     * 获取预定义的缓存实例
     * @param cacheName 缓存区域名称
     * @return 缓存实例，如果不存在则返回 null
     */
    private AsyncCache<String, Object> getCache(String cacheName) {
        return caches.get(cacheName);
    }

    @SuppressWarnings("unchecked")
    private <T> T convertValue(Object value, Class<T> type) {
        if (value == null) {
            return null;
        }
        if (type.isInstance(value)) {
            return (T) value;
        }
        // 如果类型不匹配，尝试通过ObjectMapper转换
        if (value instanceof Map && type == Map.class) {
            return (T) value;
        }
        return objectMapper.convertValue(value, type);
    }
}