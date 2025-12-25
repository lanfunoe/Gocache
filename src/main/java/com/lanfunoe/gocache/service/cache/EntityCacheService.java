package com.lanfunoe.gocache.service.cache;

import com.lanfunoe.gocache.dto.TagResponse;
import com.lanfunoe.gocache.model.Lyric;
import com.lanfunoe.gocache.model.Singer;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 实体缓存服务接口
 * 提供基于实体类型的缓存方法，替代通用的 KV 缓存
 *
 * 缓存层级：
 * - L1: Caffeine 内存缓存（热点数据快速访问）
 * - L2: SQLite 持久化缓存（通过 Repository 访问专门的数据表）
 * - L3: 文件系统（媒体文件、歌词内容等）
 */
public interface EntityCacheService {

    // ========== 歌词缓存 ==========

    /**
     * 获取歌词（L1 -> L2 -> loader）
     *
     * @param hash      歌曲 hash
     * @param accessKey 访问密钥
     * @param loader    缓存未命中时的加载器
     * @return 歌词实体（包含从文件系统加载的内容）
     */
    Mono<Lyric> getLyric(String hash, String accessKey, java.util.function.Supplier<Mono<Lyric>> loader);

    /**
     * 保存歌词（元数据到 L2，内容到文件系统）
     *
     * @param lyric   歌词实体
     * @param content 歌词内容
     * @return 保存后的歌词实体
     */
    Mono<Lyric> saveLyric(Lyric lyric, String content);

    /**
     * 检查歌词是否已缓存
     *
     * @param hash      歌曲 hash
     * @param accessKey 访问密钥
     * @return 是否存在
     */
    Mono<Boolean> isLyricCached(String hash, String accessKey);

    // ========== 歌手缓存 ==========

    /**
     * 获取歌手信息（L1 -> L2 -> loader）
     *
     * @param singerId 歌手 ID
     * @param loader   缓存未命中时的加载器
     * @return 歌手实体
     */
    Mono<Singer> getSinger(Long singerId, java.util.function.Supplier<Mono<Singer>> loader);

    /**
     * 保存歌手信息
     *
     * @param singer 歌手实体
     * @return 保存后的歌手实体
     */
    Mono<Singer> saveSinger(Singer singer);

    // ========== 分类缓存 ==========


    /**
     * 获取所有标签（支持stale-while-revalidate模式）
     *
     * @param loader 缓存未命中时的加载器，返回List<Tag>
     * @param staleWhileRevalidate 是否启用stale-while-revalidate模式
     * @return 标签列表
     */

    Mono<List<TagResponse>> getAllTags(Supplier<Mono<List<TagResponse>>> loader, boolean staleWhileRevalidate);


    // ========== 每日推荐缓存 ==========

    /**
     * 获取每日推荐（L1 -> L2 -> loader）
     *
     * @param date   日期（格式：yyyyMMdd）
     * @param loader 缓存未命中时的加载器，返回 API 原始响应
     * @return 完整的每日推荐响应（包含元数据和歌曲列表）
     */
    Mono<Map<String, Object>> getEverydayRecommend(String date, Supplier<Mono<Map<String, Object>>> loader);

    // ========== 缓存管理 ==========

    /**
     * 清除指定类型的缓存
     *
     * @param cacheName 缓存名称
     * @return 完成信号
     */
    Mono<Void> clearCache(String cacheName);

    /**
     * 获取缓存统计信息
     *
     * @return 统计信息
     */
    Mono<java.util.Map<String, Object>> getCacheStats();

}
