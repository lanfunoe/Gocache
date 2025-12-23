package com.lanfunoe.gocache.config;

import com.lanfunoe.gocache.service.cache.CacheConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQLite DataSource configuration using HikariCP
 * Provides a production-ready connection pool for SQLite cache
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SQLiteDataSourceConfig {

    private final CacheConfig cacheConfig;
    private HikariDataSource dataSource;

    @Bean(name = "sqliteDataSource")
    public DataSource sqliteDataSource() {
        CacheConfig.SqliteConfig sqliteConfig = cacheConfig.getSqlite();
        CacheConfig.HikariConfig hikariConfig = sqliteConfig.getHikari();

        // Resolve path, replacing ${user.home}
        String dbPath = sqliteConfig.getPath().replace("${user.home}", System.getProperty("user.home"));

        // Ensure directory exists
        try {
            Path dbDir = Paths.get(dbPath).getParent();
            if (dbDir != null && !Files.exists(dbDir)) {
                Files.createDirectories(dbDir);
                log.info("Created cache directory: {}", dbDir);
            }
        } catch (Exception e) {
            log.error("Failed to create cache directory", e);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + dbPath);
        config.setPoolName("SQLiteCache-HikariPool");

        // Pool sizing
        config.setMaximumPoolSize(hikariConfig.getPoolSize());
        config.setMinimumIdle(Math.min(2, hikariConfig.getPoolSize()));

        // Timeouts
        config.setConnectionTimeout(hikariConfig.getConnectionTimeout());
        config.setIdleTimeout(hikariConfig.getIdleTimeout());
        config.setMaxLifetime(hikariConfig.getMaxLifetime());

        // Connection testing
        config.setConnectionTestQuery(hikariConfig.getConnectionTestQuery());

        // SQLite-specific settings
        // Disable auto-commit for better control
        config.setAutoCommit(true);

        // Don't fail if DB doesn't exist yet (SQLite creates it)
        config.setInitializationFailTimeout(hikariConfig.getInitializationFailTimeout());

        // Create the data source
        dataSource = new HikariDataSource(config);

        // Initialize WAL mode on first connection
        initializeWalMode();

        log.info("SQLite HikariCP DataSource initialized at: {} with pool size: {}",
                dbPath, hikariConfig.getPoolSize());

        return dataSource;
    }

    /**
     * Initialize SQLite WAL mode for better concurrency
     */
    private void initializeWalMode() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL");
            stmt.execute("PRAGMA synchronous=NORMAL");
            log.debug("SQLite WAL mode enabled");

            // Initialize normalized tables
            initializeNormalizedTables(stmt);
        } catch (SQLException e) {
            log.warn("Failed to enable WAL mode", e);
        }
    }

    /**
     * Initialize normalized database tables
     */
    private void initializeNormalizedTables(Statement stmt) throws SQLException {
        // ========== 核心实体表 ==========

        // 用户表（扩展）
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS users (
                userid INTEGER PRIMARY KEY,
                nickname TEXT,
                k_nickname TEXT,
                fx_nickname TEXT,
                pic TEXT,
                k_pic TEXT,
                fx_pic TEXT,
                bg_pic TEXT,
                gender INTEGER DEFAULT 0,
                birthday TEXT,
                city TEXT,
                province TEXT,
                occupation TEXT,
                descri TEXT,
                follows INTEGER DEFAULT 0,
                fans INTEGER DEFAULT 0,
                visitors INTEGER DEFAULT 0,
                friends INTEGER DEFAULT 0,
                vip_type INTEGER DEFAULT 0,
                m_type INTEGER DEFAULT 0,
                y_type INTEGER DEFAULT 0,
                svip_level INTEGER DEFAULT 0,
                svip_score INTEGER DEFAULT 0,
                p_grade INTEGER DEFAULT 0,
                constellation INTEGER DEFAULT -1,
                is_star INTEGER DEFAULT -1,
                star_id INTEGER DEFAULT 0,
                star_status INTEGER DEFAULT 0,
                singer_status INTEGER DEFAULT 0,
                auth_info TEXT,
                auth_info_singer TEXT,
                logintime INTEGER,
                rtime INTEGER,
                created_at INTEGER NOT NULL,
                updated_at INTEGER NOT NULL
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_users_nickname ON users(nickname)");

        // 歌手表（扩展）
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS singers (
                singerid INTEGER PRIMARY KEY,
                singername TEXT NOT NULL,
                pinyin_initial TEXT,
                avatar TEXT,
                gender INTEGER DEFAULT 0,
                birthday TEXT,
                country TEXT,
                area_id TEXT,
                language TEXT,
                intro TEXT,
                long_intro TEXT,
                identity INTEGER DEFAULT 0,
                is_publish INTEGER DEFAULT 1,
                fans_count INTEGER DEFAULT 0,
                song_count INTEGER DEFAULT 0,
                album_count INTEGER DEFAULT 0,
                mv_count INTEGER DEFAULT 0,
                user_status INTEGER DEFAULT 0,
                created_at INTEGER NOT NULL,
                updated_at INTEGER NOT NULL
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_singers_name ON singers(singername)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_singers_pinyin ON singers(pinyin_initial)");

        // 专辑表（扩展）
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS albums (
                album_id INTEGER PRIMARY KEY,
                album_name TEXT NOT NULL,
                album_img TEXT,
                intro TEXT,
                publish_date TEXT,
                publish INTEGER DEFAULT 1,
                song_count INTEGER DEFAULT 0,
                language TEXT,
                genre TEXT,
                created_at INTEGER NOT NULL,
                updated_at INTEGER NOT NULL
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_albums_name ON albums(album_name)");

        // 歌曲表（修改）- 复合主键 (audio_id, hash)
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS songs (
                audio_id INTEGER NOT NULL,
                hash TEXT NOT NULL,
                songname TEXT NOT NULL,
                filename TEXT,
                duration INTEGER DEFAULT 0,
                album_id INTEGER,
                language TEXT,
                genre TEXT,
                publish_date TEXT,
                play_count INTEGER DEFAULT 0,
                collect_count INTEGER DEFAULT 0,
                share_count INTEGER DEFAULT 0,
                comment_count INTEGER DEFAULT 0,
                mvhash TEXT,
                created_at INTEGER NOT NULL,
                updated_at INTEGER NOT NULL,
                PRIMARY KEY (audio_id, hash)
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_songs_name ON songs(songname)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_songs_audio_id ON songs(audio_id)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_songs_hash ON songs(hash)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_songs_album ON songs(album_id)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_songs_language ON songs(language)");

        // 歌曲音质表（新增）
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS song_qualities (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                audio_id INTEGER NOT NULL,
                song_hash TEXT NOT NULL,
                quality_type TEXT NOT NULL,
                quality_hash TEXT NOT NULL,
                filesize INTEGER DEFAULT 0,
                bitrate INTEGER DEFAULT 0,
                filetype TEXT,
                privilege INTEGER DEFAULT 0,
                local_path TEXT,
                created_at INTEGER NOT NULL,
                updated_at INTEGER NOT NULL,
                UNIQUE(audio_id, song_hash, quality_type)
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_song_qualities_audio ON song_qualities(audio_id, song_hash)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_song_qualities_hash ON song_qualities(quality_hash)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_song_qualities_type ON song_qualities(quality_type)");

        // 图片资源表（新增）
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS images (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                entity_type TEXT NOT NULL,
                entity_id TEXT NOT NULL,
                size_type TEXT NOT NULL,
                url TEXT NOT NULL,
                local_path TEXT,
                width INTEGER,
                height INTEGER,
                created_at INTEGER NOT NULL,
                updated_at INTEGER NOT NULL,
                UNIQUE(entity_type, entity_id, size_type)
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_images_entity ON images(entity_type, entity_id)");

        // 歌单表（扩展）
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS playlists (
                specialid INTEGER PRIMARY KEY,
                specialname TEXT NOT NULL,
                imgurl TEXT,
                flexible_cover TEXT,
                intro TEXT,
                tags TEXT,
                songcount INTEGER DEFAULT 0,
                playcount INTEGER DEFAULT 0,
                collectcount INTEGER DEFAULT 0,
                share_count INTEGER DEFAULT 0,
                userid INTEGER,
                nickname TEXT,
                user_pic TEXT,
                ptype INTEGER DEFAULT 0,
                type INTEGER DEFAULT 0,
                isglobal INTEGER DEFAULT 0,
                is_publish INTEGER DEFAULT 1,
                is_featured INTEGER DEFAULT 0,
                is_pri INTEGER DEFAULT 0,
                global_collection_id TEXT,
                publish_date TEXT,
                create_time INTEGER,
                update_time INTEGER,
                created_at INTEGER NOT NULL,
                updated_at INTEGER NOT NULL
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_playlists_name ON playlists(specialname)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_playlists_user ON playlists(userid)");

        // 排行榜表（扩展）
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS ranks (
                rankid INTEGER PRIMARY KEY,
                rankname TEXT NOT NULL,
                intro TEXT,
                imgurl TEXT,
                img_9 TEXT,
                banner_9 TEXT,
                bannerurl TEXT,
                album_img_9 TEXT,
                share_bg TEXT,
                share_logo TEXT,
                type INTEGER DEFAULT 0,
                ranktype INTEGER DEFAULT 0,
                classify INTEGER DEFAULT 0,
                bangid INTEGER,
                rank_cid INTEGER,
                category_name TEXT,
                category_id INTEGER,
                update_frequency TEXT,
                update_frequency_type INTEGER DEFAULT 0,
                play_times INTEGER DEFAULT 0,
                issue INTEGER DEFAULT 0,
                is_timing INTEGER DEFAULT 0,
                haschildren INTEGER DEFAULT 0,
                zone TEXT,
                jump_url TEXT,
                created_at INTEGER NOT NULL,
                updated_at INTEGER NOT NULL
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_ranks_name ON ranks(rankname)");

        // 标签表（扩展）
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS tags (
                tagid INTEGER PRIMARY KEY,
                tagname TEXT NOT NULL,
                parentid INTEGER DEFAULT 0,
                sort INTEGER DEFAULT 0,
                hot INTEGER DEFAULT 0,
                publish INTEGER DEFAULT 1,
                imgurl TEXT,
                color TEXT,
                special_type INTEGER DEFAULT 0,
                created_at INTEGER NOT NULL,
                updated_at INTEGER NOT NULL
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_tags_parent ON tags(parentid)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_tags_name ON tags(tagname)");

        // 歌词元数据表（优化版）
        // 设计原则：
        // - 不存储可从 songs 表获取的数据（songname, artistname, duration）
        // - 不存储易变数据（nickname）
        // - 歌词内容存储在文件系统，数据库只存元数据和路径
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS lyrics (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                audio_id INTEGER NOT NULL,      -- 关联歌曲
                hash TEXT NOT NULL,             -- 歌曲 hash
                accesskey TEXT NOT NULL,        -- 访问密钥
                lyric_id TEXT,                  -- API 返回的歌词 ID
                language TEXT,                  -- 歌词语言
                krctype INTEGER DEFAULT 0,      -- 歌词类型
                contenttype INTEGER DEFAULT 0,  -- 内容类型
                content_format INTEGER DEFAULT 0, -- 内容格式
                product_from TEXT,              -- 来源
                file_path TEXT,                 -- 歌词文件路径
                lyric_version INTEGER DEFAULT 1, -- 歌词版本
                trans_type INTEGER DEFAULT 0,   -- 翻译类型
                score INTEGER DEFAULT 0,        -- 评分
                uid TEXT,                       -- 上传者 ID
                fmt TEXT,                       -- 格式（krc/lrc/txt）
                created_at INTEGER NOT NULL,
                updated_at INTEGER NOT NULL,
                UNIQUE(hash, accesskey)
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_lyrics_hash ON lyrics(hash)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_lyrics_audio ON lyrics(audio_id)");

        // ========== 关联表 ==========

        // 歌曲-歌手关联表（修改）- 添加 role 字段
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS song_singers (
                audio_id INTEGER NOT NULL,
                hash TEXT NOT NULL,
                singerid INTEGER NOT NULL,
                is_primary INTEGER DEFAULT 0,-- 是否为主要歌手
                role TEXT DEFAULT 'singer',
                created_at INTEGER NOT NULL,
                PRIMARY KEY (audio_id, hash, singerid, role)
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_song_singers_singer ON song_singers(singerid)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_song_singers_song ON song_singers(audio_id, hash)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_song_singers_role ON song_singers(role)");

        // 专辑-歌手关联表（新增）
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS album_singers (
                album_id INTEGER NOT NULL,
                singerid INTEGER NOT NULL,
                is_primary INTEGER DEFAULT 0,
                created_at INTEGER NOT NULL,
                PRIMARY KEY (album_id, singerid)
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_album_singers_singer ON album_singers(singerid)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_album_singers_album ON album_singers(album_id)");

        // 歌曲-标签关联表（新增）
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS song_tags (
                audio_id INTEGER NOT NULL,
                hash TEXT NOT NULL,
                tagid INTEGER NOT NULL,
                created_at INTEGER NOT NULL,
                PRIMARY KEY (audio_id, hash, tagid)
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_song_tags_tag ON song_tags(tagid)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_song_tags_song ON song_tags(audio_id, hash)");

        // 歌单-歌曲关联表
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS playlist_songs (
                specialid INTEGER NOT NULL,
                audio_id INTEGER,
                hash TEXT NOT NULL,
                position INTEGER DEFAULT 0,
                added_at INTEGER NOT NULL,
                PRIMARY KEY (specialid, hash)
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_playlist_songs_hash ON playlist_songs(hash)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_playlist_songs_song ON playlist_songs(audio_id, hash)");

        // 排行榜-歌曲关联表
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS rank_songs (
                rankid INTEGER NOT NULL,
                audio_id INTEGER,
                hash TEXT NOT NULL,
                rank_position INTEGER NOT NULL,
                score INTEGER DEFAULT 0,
                trend TEXT,
                snapshot_date TEXT NOT NULL,
                PRIMARY KEY (rankid, hash, snapshot_date)
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_rank_songs_hash ON rank_songs(hash)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_rank_songs_date ON rank_songs(snapshot_date)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_rank_songs_song ON rank_songs(audio_id, hash)");

        // 用户关注歌手表
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS user_follows (
                userid INTEGER NOT NULL,
                singerid INTEGER NOT NULL,
                addtime INTEGER,
                source INTEGER DEFAULT 0,
                created_at INTEGER NOT NULL,
                PRIMARY KEY (userid, singerid)
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_user_follows_singer ON user_follows(singerid)");

        // 每日推荐元数据表
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS everyday_recommend_meta (
                recommend_date TEXT PRIMARY KEY,
                creation_date TEXT,
                mid TEXT,
                bi_biz TEXT,
                sign TEXT,
                song_list_size INTEGER DEFAULT 0,
                sub_title TEXT,
                cover_img_url TEXT,
                client_playlist_flag INTEGER DEFAULT 0,
                is_guarantee_rec INTEGER DEFAULT 0,
                created_at INTEGER NOT NULL,
                updated_at INTEGER NOT NULL
            )
            """);

        // 每日推荐歌曲关联表
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS everyday_recommend (
                audio_id INTEGER,
                hash TEXT NOT NULL,
                recommend_date TEXT NOT NULL,
                position INTEGER DEFAULT 0,
                created_at INTEGER NOT NULL,
                PRIMARY KEY (hash, recommend_date)
            )
            """);
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_recommend_date ON everyday_recommend(recommend_date)");
        stmt.execute("CREATE INDEX IF NOT EXISTS idx_recommend_song ON everyday_recommend(audio_id, hash)");

        // 注意：cache_entries 表已移除
        // L2 缓存现在通过各个 Repository 直接访问专门的数据表
        // 不再使用通用的 KV 缓存

        log.info("Normalized database tables initialized successfully");
    }

    @PreDestroy
    public void destroy() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            log.info("SQLite HikariCP DataSource closed");
        }
    }
}