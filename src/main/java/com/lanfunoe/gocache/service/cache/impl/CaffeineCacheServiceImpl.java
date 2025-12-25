package com.lanfunoe.gocache.service.cache.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.lanfunoe.gocache.service.cache.CacheConfig;
import com.lanfunoe.gocache.service.cache.CacheNames;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Caffeine内存缓存服务实现
 * 提供高性能的本地缓存，支持响应式编程
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CaffeineCacheServiceImpl implements ReactiveCacheService {

    private final CacheConfig cacheConfig;
    private final ObjectMapper objectMapper;
//todo 为什么ConcurrentHashMap
    private final Map<String, AsyncCache<String, Object>> caches = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {

        // 初始化预定义的缓存区域
        initCache(CacheNames.LYRICS, 2000, null, Duration.ofHours(12));
        initCache(CacheNames.ARTIST_INFO, 1000, Duration.ofHours(168), null);
        initCache(CacheNames.CATEGORIES, 100, Duration.ofHours(24), null);
        initCache(CacheNames.EVERYDAY_RECOMMEND, 100, Duration.ofHours(24), null);
        initCache(CacheNames.SEARCH_HOT, 50, Duration.ofMinutes(30), null);
        initCache(CacheNames.SEARCH_RESULTS, 1000, Duration.ofMinutes(5), null);
        initCache(CacheNames.USER_INFO, 500, Duration.ofHours(1), null);
        initCache(CacheNames.PLAYLIST_DETAIL, 500, Duration.ofHours(2), null);
        initCache(CacheNames.ARTIST_WORKS, 500, Duration.ofHours(2), null);
        initCache(CacheNames.IMAGES, 5000, Duration.ofHours(24), null);
        initCache(CacheNames.SONGS, 1000, Duration.ofHours(24), null);

        // 从配置文件加载自定义配置覆盖默认值
        cacheConfig.getCaffeine().getSpecs().forEach((name, spec) -> {
            initCache(name, spec.getMaxSize(), spec.getExpireAfterWrite(), spec.getExpireAfterAccess());
        });

        log.info("Caffeine cache initialized with {} regions", caches.size());
    }

    private void initCache(String name, int maxSize, Duration expireAfterWrite, Duration expireAfterAccess) {
        Caffeine<Object, Object> builder = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .recordStats();

        if (expireAfterWrite != null) {
            builder.expireAfterWrite(expireAfterWrite);
        }
        if (expireAfterAccess != null) {
            builder.expireAfterAccess(expireAfterAccess);
        }

        AsyncCache<String, Object> cache = builder.buildAsync();
        caches.put(name, cache);
        log.info("Initialized cache region: {} (maxSize={}, expireAfterWrite={}, expireAfterAccess={})",
                name, maxSize, expireAfterWrite, expireAfterAccess);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Mono<T> get(String cacheName, String key, Class<T> type, Supplier<Mono<T>> loader) {

        AsyncCache<String, Object> cache = getOrCreateCache(cacheName);

        return Mono.fromFuture(() ->
                        cache.get(key, (k, executor) ->
                                loader.get()
                                        .doOnNext(v -> log.debug("Cache MISS: {}:{}", cacheName, key))
                                        .map(v -> (Object) v)
                                        .toFuture()
                        )
                ).map(v -> convertValue(v, type))
                .doOnSubscribe(s -> log.trace("Cache lookup: {}:{}", cacheName, key));
    }

    @Override
    public <T> Mono<T> get(String cacheName, String key, Class<T> type, Duration ttl, Supplier<Mono<T>> loader) {
        // Caffeine不支持单个key的TTL，使用默认行为
        return get(cacheName, key, type, loader);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Mono<T> getIfPresent(String cacheName, String key, Class<T> type) {

        AsyncCache<String, Object> cache = caches.get(cacheName);
        if (cache == null) {
            return Mono.empty();
        }

        CompletableFuture<Object> future = cache.getIfPresent(key);
        if (future == null) {
            return Mono.empty();
        }

        return Mono.fromFuture(future)
                .map(v -> convertValue(v, type))
                .doOnNext(v -> log.debug("Cache HIT: {}:{}", cacheName, key));
    }

    @Override
    public <T> Mono<Void> put(String cacheName, String key, T value) {

        AsyncCache<String, Object> cache = getOrCreateCache(cacheName);
        cache.put(key, CompletableFuture.completedFuture(value));
        log.debug("Cache PUT: {}:{}", cacheName, key);
        return Mono.empty();
    }

    @Override
    public <T> Mono<Void> put(String cacheName, String key, T value, Duration ttl) {
        // Caffeine不支持单个key的TTL，使用默认行为
        return put(cacheName, key, value);
    }

    @Override
    public Mono<Void> evict(String cacheName, String key) {
        AsyncCache<String, Object> cache = caches.get(cacheName);
        if (cache != null) {
            cache.synchronous().invalidate(key);
            log.debug("Cache EVICT: {}:{}", cacheName, key);
        }
        return Mono.empty();
    }

    @Override
    public Mono<Void> clear(String cacheName) {
        AsyncCache<String, Object> cache = caches.get(cacheName);
        if (cache != null) {
            cache.synchronous().invalidateAll();
            log.info("Cache CLEAR: {}", cacheName);
        }
        return Mono.empty();
    }

    @Override
    public Mono<Boolean> exists(String cacheName, String key) {
        AsyncCache<String, Object> cache = caches.get(cacheName);
        if (cache == null) {
            return Mono.just(false);
        }
        return Mono.just(cache.getIfPresent(key) != null);
    }

    @Override
    public Mono<Map<String, Object>> getStats() {
        Map<String, Object> allStats = new HashMap<>();
        caches.forEach((name, cache) -> {
            allStats.put(name, buildStatsMap(cache.synchronous().stats()));
        });
        return Mono.just(allStats);
    }

    @Override
    public Mono<Map<String, Object>> getStats(String cacheName) {
        AsyncCache<String, Object> cache = caches.get(cacheName);
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

    private AsyncCache<String, Object> getOrCreateCache(String cacheName) {
        return caches.computeIfAbsent(cacheName, name -> {
            CacheConfig.CacheSpec spec = cacheConfig.getCacheSpec(name);
            Caffeine<Object, Object> builder = Caffeine.newBuilder()
                    .maximumSize(spec.getMaxSize())
                    .recordStats();

            if (spec.getExpireAfterWrite() != null) {
                builder.expireAfterWrite(spec.getExpireAfterWrite());
            }
            if (spec.getExpireAfterAccess() != null) {
                builder.expireAfterAccess(spec.getExpireAfterAccess());
            }

            log.info("Created new cache region on-demand: {}", name);
            return builder.buildAsync();
        });
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
