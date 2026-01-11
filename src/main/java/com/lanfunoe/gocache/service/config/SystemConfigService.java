package com.lanfunoe.gocache.service.config;

import com.lanfunoe.gocache.config.StorageConfig;
import com.lanfunoe.gocache.model.SystemConfig;
import com.lanfunoe.gocache.repository.SystemConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统配置服务
 * 管理动态配置，支持数据库持久化
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemConfigService {

    public static final String KEY_SONG_PATH = "storage.song_path";
    public static final String KEY_LYRICS_PATH = "storage.lyrics_path";
    public static final String KEY_IMG_PATH = "storage.img_path";
    public static final String STORAGE_ENABLE = "storage.enabled";
     private static final String STORAGE_MAX_SIZE = "storage.max_size";

    private final SystemConfigRepository repository;
    private final StorageConfig storageConfig;
    private final Map<String, String> configCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        initializeConfigs()
                .doOnSuccess(v -> log.info("System config initialized"))
                .subscribe();
    }

    private Mono<Void> initializeConfigs() {

        // 首先加载现有的数据库配置
        return loadConfigs()
                .then(Mono.defer(() -> {
                    // 检查并初始化默认配置（如果不存在的话）
                    return Mono.when(
                            ensureConfigExists(KEY_SONG_PATH, storageConfig.getSongPath(), "歌曲存储路径"),
                            ensureConfigExists(KEY_LYRICS_PATH, storageConfig.getLyricsPath(), "歌词存储路径"),
                            ensureConfigExists(KEY_IMG_PATH, storageConfig.getImgPath(), "图片存储路径"),
                            ensureConfigExists(STORAGE_MAX_SIZE, storageConfig.getMaxSize().toString(), "存储空间限制"),
                            ensureConfigExists(STORAGE_ENABLE, "true", "是否启用存储功能")

                    );
                }));
    }

    private Mono<Void> loadConfigs() {
        return repository.findAll()
                .doOnNext(config -> configCache.put(config.getConfigKey(), resolvePath(config.getConfigValue())))
                .then();
    }

    /**
     * 确保配置项存在，如果不存在则创建，如果存在则覆写缓存
     */
    private Mono<Void> ensureConfigExists(String key, String defaultValue, String description) {
        if (configCache.containsKey(key)) {
            // 如果缓存中已有该配置，说明数据库中已存在，直接返回
            return Mono.empty();
        }
        // 数据库中不存在，创建新配置
        log.info("Creating default config: {} = {}", key, defaultValue);
        return setConfigInternal(key, defaultValue, description);
    }

    private Mono<Void> setConfigInternal(String key, String value, String description) {
        return ensureDirectoryExists(value)
                .then(Mono.defer(() -> {
                    SystemConfig config = new SystemConfig();
                    config.setConfigKey(key);
                    config.setConfigValue(value);
                    config.setDescription(description);
                    config.setUpdatedAt(LocalDateTime.now());
                    return repository.save(config);
                }))
                .doOnSuccess(c -> {
                    configCache.put(key, resolvePath(value));
                    log.info("Config saved: {} = {}", key, value);
                })
                .then();
    }

    public String getSongPath() {
        return configCache.get(KEY_SONG_PATH);
    }
    
     public boolean isStorageEnabled() {
        return Boolean.parseBoolean(configCache.get(STORAGE_ENABLE)) ;
    }

    public String getLyricsPath() {
        return configCache.get(KEY_LYRICS_PATH);
    }

    public String getImgPath() {
        return configCache.get(KEY_IMG_PATH);
    }

     public Long getMaxStorageSize() {
        String maxSizeStr = configCache.get(STORAGE_MAX_SIZE);
        return Long.parseLong(maxSizeStr);
    }

    public Mono<Void> setSongPath(String path) {
        return setConfig(KEY_SONG_PATH, path, "歌曲存储路径");
    }

    public Mono<Void> setLyricsPath(String path) {
        return setConfig(KEY_LYRICS_PATH, path, "歌词存储路径");
    }

    public Mono<Void> setImgPath(String path) {
        return setConfig(KEY_IMG_PATH, path, "图片存储路径");
    }

    public Mono<String> getConfig(String key) {
        String cached = configCache.get(key);
        if (cached != null) {
            return Mono.just(cached);
        }
        return repository.findById(key)
                .map(SystemConfig::getConfigValue);
    }

    public Mono<Void> setConfig(String key, String value, String description) {
        String resolvedValue = resolvePath(value);
        return setConfigInternal(key, resolvedValue, description);
    }

    public Mono<Map<String, String>> getAllStoragePaths() {
        return Mono.just(Map.of(
                "songPath", getSongPath(),
                "lyricsPath", getLyricsPath(),
                "imgPath", getImgPath()
        ));
    }

    private String resolvePath(String path) {
        if (path == null) return null;
        return path.replace("${user.home}", System.getProperty("user.home"));
    }

    private Mono<Void> ensureDirectoryExists(String path) {
        return Mono.fromRunnable(() -> {
            try {
                Path dir = Paths.get(path);
                if (!Files.exists(dir)) {
                    Files.createDirectories(dir);
                    log.info("Created directory: {}", path);
                }
            } catch (Exception e) {
                log.error("Failed to create directory: {}", path, e);
            }
        });
    }
}