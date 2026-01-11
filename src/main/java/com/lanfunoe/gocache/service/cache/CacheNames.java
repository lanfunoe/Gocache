package com.lanfunoe.gocache.service.cache;

/**
 * 缓存名称常量定义
 *
 * 缓存层级说明:
 * - L1: Caffeine内存缓存 (热点数据快速访问)
 * - L2: SQLite持久化缓存 (元数据持久化)
 * - L3: 本地文件系统 (媒体文件存储)
 */
public final class CacheNames {

    private CacheNames() {
        // 防止实例化
    }

    // ========== L1 + L2: 持久化数据缓存 ==========

    /** 歌词缓存 - 通过hash获取，永久有效 */
    public static final String LYRICS = "lyrics";

    /** 歌手基本信息 - 7天过期 */
    public static final String ARTIST_INFO = "artist_info";

    /** 分类数据 - 24小时过期 */
    public static final String CATEGORIES = "categories";

    /** 标签数据 - 24小时过期 */
    public static final String TAGS = "tags";

    /** 每日推荐 - 24小时过期（实际到当天24:00） */
    public static final String EVERYDAY_RECOMMEND = "everyday_recommend";

    // ========== L3: 媒体文件存储 ==========

    /** 图片文件 - 永久存储 */
    public static final String IMAGES = "images";

    /** 歌曲文件 - 永久存储 */
    public static final String SONGS = "songs";

    // ========== L1: 仅内存缓存 ==========

    /** 热门搜索词 - 30分钟过期 */
    public static final String SEARCH_HOT = "search_hot";

    /** 搜索结果 - 5分钟过期 */
    public static final String SEARCH_RESULTS = "search_results";

    /** 用户信息 - 1小时过期 */
    public static final String USER_INFO = "user_info";

    /** 歌单详情 - 2小时过期 */
    public static final String PLAYLIST_DETAIL = "playlist_detail";

    /** 歌手作品列表 - 2小时过期 */
    public static final String ARTIST_WORKS = "artist_works";

    /** 热门歌单 - 30分钟过期 */
    public static final String TOP_PLAYLIST = "top_playlist";

    /** 歌曲URL缓存 - 2小时过期 */
    public static final String SONG_URL = "song_url";



}
