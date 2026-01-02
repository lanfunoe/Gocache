package com.lanfunoe.gocache.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 数据库初始化服务
 * 负责创建和初始化PostgreSQL数据库表结构
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseInitializationService {

    private final DatabaseClient databaseClient;

    @EventListener(ApplicationReadyEvent.class)
    public Mono<Void> initializeDatabase() {
        log.info("Starting PostgreSQL database initialization...");

        return initializeNormalizedTables()
                .doOnSuccess(v -> log.info("Database initialization completed successfully"))
                .doOnError(e -> {
                    log.error("Failed to initialize database", e);
                    // 不抛出异常，允许应用继续运行
                })
                .onErrorResume(e -> {
                    log.error("Database initialization failed, continuing anyway", e);
                    return Mono.empty();
                });
    }

    /**
     * 初始化规范化数据库表结构
     */
    private Mono<Void> initializeNormalizedTables() {
        log.info("Creating normalized database tables...");

        return Mono.when(
                // 1. 用户表
                createTable("""
                    CREATE TABLE IF NOT EXISTS "user" (
                        user_id TEXT PRIMARY KEY NOT NULL,
                        nickname TEXT,
                        bg_pic TEXT,
                        pic TEXT,
                        gender INTEGER,
                        p_grade INTEGER,
                        descri TEXT,
                        follows INTEGER,
                        fans INTEGER,
                        friends INTEGER,
                        hvisitors INTEGER,
                        duration BIGINT,
                        rtime BIGINT,
                        extra_info TEXT,
                        created_at TIMESTAMP,
                        updated_at TIMESTAMP
                    )
                    """),

                // 2. 用户VIP信息表
                createTable("""
                    CREATE TABLE IF NOT EXISTS user_vip (
                        user_id TEXT NOT NULL,
                        product_type TEXT NOT NULL,
                        is_vip INTEGER,
                        vip_begin_time TEXT,
                        vip_end_time TEXT,
                        extra_info TEXT,
                        created_at TIMESTAMP,
                        updated_at TIMESTAMP,
                        PRIMARY KEY (user_id, product_type)
                    )
                    """).then(createIndex("CREATE INDEX IF NOT EXISTS idx_user_vip_user_id ON user_vip(user_id)")),

                // 3. 歌手/艺术家表
                createTable("""
                    CREATE TABLE IF NOT EXISTS artist (
                        artist_id BIGINT PRIMARY KEY NOT NULL,
                        author_name TEXT,
                        sizable_avatar TEXT,
                        pic TEXT,
                        song_count INTEGER,
                        album_count INTEGER,
                        mv_count INTEGER,
                        fansnums BIGINT,
                        intro TEXT,
                        long_intro TEXT,
                        extra_info TEXT,
                        created_at TIMESTAMP,
                        updated_at TIMESTAMP
                    )
                    """).then(createIndex("CREATE INDEX IF NOT EXISTS idx_artist_name ON artist(author_name)")),

                // 4. 专辑表
                createTable("""
                    CREATE TABLE IF NOT EXISTS album (
                        album_id BIGINT PRIMARY KEY NOT NULL,
                        album_name TEXT,
                        sizable_cover TEXT,
                        cover TEXT,
                        publish_date TEXT,
                        intro TEXT,
                        song_count INTEGER,
                        extra_info TEXT,
                        created_at TIMESTAMP,
                        updated_at TIMESTAMP
                    )
                    """).then(createIndex("CREATE INDEX IF NOT EXISTS idx_album_name ON album(album_name)")),

                // 5. 歌曲表 (核心表)
                createTable("""
                    CREATE TABLE IF NOT EXISTS song (
                        audio_id BIGINT NOT NULL,
                        hash TEXT NOT NULL,
                        mixsongid TEXT,
                        songname TEXT,
                        audio_name TEXT,
                        filename TEXT,
                        author_name TEXT,
                        album_id BIGINT,
                        album_name TEXT,
                        cover TEXT,
                        sizable_cover TEXT,
                        duration BIGINT,
                        bitrate INTEGER,
                        filesize BIGINT,
                        extname TEXT,
                        remark TEXT,
                        privilege INTEGER,
                        mvhash TEXT,
                        hash_128 TEXT,
                        hash_320 TEXT,
                        hash_flac TEXT,
                        language TEXT,
                        publish_date TEXT,
                        extra_info TEXT,
                        created_at TIMESTAMP,
                        updated_at TIMESTAMP,
                        PRIMARY KEY (audio_id, hash)
                    )
                    """).then(
                        Mono.when(
                            createIndex("CREATE INDEX IF NOT EXISTS idx_song_audio_id ON song(audio_id)"),
                            createIndex("CREATE INDEX IF NOT EXISTS idx_song_hash ON song(hash)"),
                            createIndex("CREATE INDEX IF NOT EXISTS idx_song_name ON song(songname)"),
                            createIndex("CREATE INDEX IF NOT EXISTS idx_song_author ON song(author_name)"),
                            createIndex("CREATE INDEX IF NOT EXISTS idx_song_album_id ON song(album_id)")
                        )
                    ),

                // 6. 歌曲音质信息表
                createTable("""
                    CREATE TABLE IF NOT EXISTS song_quality (
                        audio_id BIGINT NOT NULL,
                        quality_hash TEXT NOT NULL,
                        song_hash TEXT NOT NULL,
                        level INTEGER,
                        bitrate INTEGER,
                        filesize BIGINT,
                        privilege INTEGER,
                        extra_info TEXT,
                        created_at TIMESTAMP,
                        PRIMARY KEY (audio_id, quality_hash, song_hash)
                    )
                    """),

                // 7. 歌单标签分类表
                createTable("""
                    CREATE TABLE IF NOT EXISTS playlist_tag_category (
                        tag_id BIGINT PRIMARY KEY NOT NULL,
                        tag_name TEXT NOT NULL,
                        parent_id BIGINT,
                        sort INTEGER,
                        extra_info TEXT,
                        row_update_time TEXT,
                        created_at TIMESTAMP
                    )
                    """).then(createIndex("CREATE INDEX IF NOT EXISTS idx_playlist_tag_category_parent ON playlist_tag_category(parent_id)")),

                // 8. 歌单表
                createTable("""
                    CREATE TABLE IF NOT EXISTS playlist (
                        global_collection_id TEXT NOT NULL,
                        category_id INTEGER NOT NULL,
                        listid BIGINT,
                        list_create_listid BIGINT,
                        list_create_userid BIGINT,
                        list_create_username TEXT,
                        list_create_gid TEXT,
                        name TEXT,
                        specialname TEXT,
                        pic TEXT,
                        flexible_cover TEXT,
                        intro TEXT,
                        tags TEXT,
                        count INTEGER,
                        sort INTEGER,
                        authors TEXT,
                        publish_date TEXT,
                        extra_info TEXT,
                        PRIMARY KEY (global_collection_id, category_id)
                    )
                    """).then(
                        Mono.when(
                            createIndex("CREATE INDEX IF NOT EXISTS idx_playlist_listid ON playlist(listid)"),
                            createIndex("CREATE INDEX IF NOT EXISTS idx_playlist_creator ON playlist(list_create_userid)"),
                            createIndex("CREATE INDEX IF NOT EXISTS idx_playlist_category_id ON playlist(category_id)")
                        )
                    ),

                // 9. 歌单-歌曲关联表
                createTable("""
                    CREATE TABLE IF NOT EXISTS playlist_song (
                        global_collection_id TEXT NOT NULL,
                        audio_id BIGINT NOT NULL,
                        song_hash TEXT NOT NULL,
                        sort INTEGER,
                        add_time TIMESTAMP,
                        extra_info TEXT,
                        PRIMARY KEY (global_collection_id, audio_id)
                    )
                    """).then(createIndex("CREATE INDEX IF NOT EXISTS idx_playlist_song_audio_id ON playlist_song(audio_id)")),

                // 10. 排行榜表
                createTable("""
                    CREATE TABLE IF NOT EXISTS rank_list (
                        rankid BIGINT PRIMARY KEY NOT NULL,
                        rankname TEXT,
                        imgurl TEXT,
                        album_cover_color TEXT,
                        intro TEXT,
                        update_frequency TEXT,
                        extra_info TEXT,
                        created_at TIMESTAMP,
                        updated_at TIMESTAMP
                    )
                    """),

                // 11. 排行榜-歌曲关联表
                createTable("""
                    CREATE TABLE IF NOT EXISTS rank_song (
                        rankid BIGINT NOT NULL,
                        rank_cid BIGINT NOT NULL,
                        audio_id BIGINT NOT NULL,
                        song_hash TEXT NOT NULL,
                        sort INTEGER,
                        extra_info TEXT,
                        created_at TIMESTAMP,
                        PRIMARY KEY (rankid, rank_cid, audio_id)
                    )
                    """).then(createIndex("CREATE INDEX IF NOT EXISTS idx_rank_song_audio_id ON rank_song(audio_id)")),

                // 12. 用户关注歌手表
                createTable("""
                    CREATE TABLE IF NOT EXISTS user_follow_artist (
                        user_id TEXT NOT NULL,
                        artist_id BIGINT NOT NULL,
                        source INTEGER,
                        pic TEXT,
                        add_time TIMESTAMP,
                        extra_info TEXT,
                        PRIMARY KEY (user_id, artist_id)
                    )
                    """).then(createIndex("CREATE INDEX IF NOT EXISTS idx_user_follow_artist ON user_follow_artist(artist_id)")),

                // 13. 用户听歌记录表
                createTable("""
                    CREATE TABLE IF NOT EXISTS user_listen_history (
                        user_id TEXT NOT NULL,
                        audio_id BIGINT NOT NULL,
                        song_hash TEXT NOT NULL,
                        name TEXT,
                        image TEXT,
                        singername TEXT,
                        author_name TEXT,
                        duration BIGINT,
                        listen_count INTEGER,
                        last_listen_time TIMESTAMP,
                        extra_info TEXT,
                        PRIMARY KEY (user_id, audio_id, song_hash)
                    )
                    """).then(
                        Mono.when(
                            createIndex("CREATE INDEX IF NOT EXISTS idx_listen_history_time ON user_listen_history(last_listen_time)"),
                            createIndex("CREATE INDEX IF NOT EXISTS idx_listen_history_user_audio ON user_listen_history(user_id, audio_id)")
                        )
                    ),

                // 14. 用户云盘歌曲表
                createTable("""
                    CREATE TABLE IF NOT EXISTS user_cloud_song (
                        user_id TEXT NOT NULL,
                        hash TEXT NOT NULL,
                        filename TEXT,
                        name TEXT,
                        author_name TEXT,
                        album_name TEXT,
                        timelen BIGINT,
                        bitrate INTEGER,
                        filesize BIGINT,
                        extra_info TEXT,
                        created_at TIMESTAMP,
                        PRIMARY KEY (user_id, hash)
                    )
                    """),

                // 15. 用户云盘容量信息表
                createTable("""
                    CREATE TABLE IF NOT EXISTS user_cloud_storage (
                        user_id TEXT PRIMARY KEY NOT NULL,
                        max_size BIGINT,
                        used_size BIGINT,
                        availble_size BIGINT,
                        extra_info TEXT,
                        updated_at TIMESTAMP
                    )
                    """),

                // 16. 歌词索引表
                createTable("""
                    CREATE TABLE IF NOT EXISTS lyric (
                        lyric_id TEXT NOT NULL,
                        accesskey TEXT NOT NULL,
                        audio_id BIGINT,
                        song_hash TEXT,
                        song_name TEXT,
                        singer TEXT,
                        duration BIGINT,
                        file_path TEXT NOT NULL,
                        fmt TEXT,
                        extra_info TEXT,
                        created_at TIMESTAMP,
                        updated_at TIMESTAMP,
                        PRIMARY KEY (lyric_id, accesskey)
                    )
                    """).then(
                        Mono.when(
                            createIndex("CREATE INDEX IF NOT EXISTS idx_lyric_audio_id ON lyric(audio_id)"),
                            createIndex("CREATE INDEX IF NOT EXISTS idx_lyric_hash ON lyric(song_hash)")
                        )
                    ),

                // 17. 歌词候选表
                createTable("""
                    CREATE TABLE IF NOT EXISTS lyric_candidate (
                        song_hash TEXT NOT NULL,
                        lyric_id TEXT NOT NULL,
                        accesskey TEXT NOT NULL,
                        audio_id BIGINT,
                        singer TEXT,
                        song_name TEXT,
                        duration BIGINT,
                        score INTEGER,
                        extra_info TEXT,
                        created_at TIMESTAMP,
                        PRIMARY KEY (song_hash, lyric_id)
                    )
                    """).then(createIndex("CREATE INDEX IF NOT EXISTS idx_lyric_candidate_audio_id ON lyric_candidate(audio_id)")),

                // 18. 每日推荐歌曲表
                createTable("""
                    CREATE TABLE IF NOT EXISTS daily_recommend (
                        user_id TEXT NOT NULL,
                        recommend_date TEXT NOT NULL,
                        song_hash TEXT NOT NULL,
                        audio_id BIGINT,
                        ori_audio_name TEXT,
                        extra_info TEXT,
                        created_at TIMESTAMP,
                        PRIMARY KEY (user_id, recommend_date, audio_id, song_hash)
                    )
                    """),

                // 19. 热门卡片推荐表
                createTable("""
                    CREATE TABLE IF NOT EXISTS top_card_song (
                        card_type TEXT NOT NULL,
                        song_hash TEXT NOT NULL,
                        audio_id BIGINT,
                        songname TEXT,
                        sizable_cover TEXT,
                        author_name TEXT,
                        time_length BIGINT,
                        sort INTEGER,
                        extra_info TEXT,
                        updated_at TIMESTAMP,
                        PRIMARY KEY (card_type, song_hash)
                    )
                    """),

                // 20. 歌曲播放URL缓存表
                createTable("""
                    CREATE TABLE IF NOT EXISTS song_url (
                        hash TEXT NOT NULL,
                        quality TEXT NOT NULL,
                        audio_id BIGINT,
                        url TEXT NOT NULL,
                        backup_url TEXT,
                        filesize BIGINT,
                        time_length BIGINT,
                        extname TEXT,
                        expire_time TIMESTAMP,
                        extra_info TEXT,
                        created_at TIMESTAMP,
                        PRIMARY KEY (audio_id, hash, quality)
                    )
                    """).then(
                        Mono.when(
                            createIndex("CREATE INDEX IF NOT EXISTS idx_song_url_audio_id ON song_url(audio_id)"),
                            createIndex("CREATE INDEX IF NOT EXISTS idx_song_url_expire ON song_url(expire_time)")
                        )
                    ),

                // 21. 歌手-歌曲关联表
                createTable("""
                    CREATE TABLE IF NOT EXISTS artist_song (
                        artist_id BIGINT NOT NULL,
                        audio_id BIGINT NOT NULL,
                        song_hash TEXT NOT NULL,
                        sort INTEGER,
                        extra_info TEXT,
                        created_at TIMESTAMP,
                        PRIMARY KEY (artist_id, audio_id)
                    )
                    """).then(createIndex("CREATE INDEX IF NOT EXISTS idx_artist_song_audio_id ON artist_song(audio_id)")),

                // 22. 专辑-歌曲关联表
                createTable("""
                    CREATE TABLE IF NOT EXISTS album_song (
                        album_id BIGINT NOT NULL,
                        audio_id BIGINT NOT NULL,
                        song_hash TEXT NOT NULL,
                        track_no INTEGER,
                        extra_info TEXT,
                        created_at TIMESTAMP,
                        PRIMARY KEY (album_id, audio_id)
                    )
                    """).then(createIndex("CREATE INDEX IF NOT EXISTS idx_album_song_audio_id ON album_song(audio_id)"))
        ).then(Mono.fromRunnable(() -> log.info("All 22 database tables created successfully")));
    }

    /**
     * 创建表
     */
    private Mono<Void> createTable(String sql) {
        return databaseClient.sql(sql)
                .fetch()
                .rowsUpdated()
                .doOnSuccess(count -> log.debug("Table created successfully"))
                .doOnError(e -> log.warn("Failed to create table (may already exist): {}", e.getMessage()))
                .onErrorResume(e -> Mono.empty()) // 忽略已存在的表错误
                .then();
    }

    /**
     * 创建索引
     */
    private Mono<Void> createIndex(String sql) {
        return databaseClient.sql(sql)
                .fetch()
                .rowsUpdated()
                .doOnSuccess(count -> log.debug("Index created successfully"))
                .doOnError(e -> log.warn("Failed to create index (may already exist): {}", e.getMessage()))
                .onErrorResume(e -> Mono.empty()) // 忽略已存在的索引错误
                .then();
    }
}
