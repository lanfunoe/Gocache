-- =============================================
-- 1. 用户表
-- =============================================
CREATE TABLE IF NOT EXISTS user (
                                    user_id INTEGER PRIMARY KEY NOT NULL,
                                    nickname TEXT,
                                    bg_pic TEXT,
                                    pic TEXT,
                                    gender INTEGER DEFAULT 0,
                                    p_grade INTEGER DEFAULT 0,
                                    descri TEXT,
                                    follows INTEGER DEFAULT 0,
                                    fans INTEGER DEFAULT 0,
                                    friends INTEGER DEFAULT 0,
                                    hvisitors INTEGER DEFAULT 0,
                                    duration INTEGER DEFAULT 0,
                                    rtime INTEGER,
                                    extra_info TEXT,
                                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 2. 用户VIP信息表
-- =============================================
CREATE TABLE IF NOT EXISTS user_vip (
                                        user_id INTEGER NOT NULL,
                                        product_type TEXT NOT NULL,
                                        is_vip INTEGER DEFAULT 0,
                                        vip_begin_time TEXT,
                                        vip_end_time TEXT,
                                        extra_info TEXT,
                                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                        PRIMARY KEY (user_id, product_type)
);

CREATE INDEX idx_user_vip_user_id ON user_vip(user_id);

-- =============================================
-- 3. 歌手/艺术家表
-- =============================================
CREATE TABLE IF NOT EXISTS artist (
                                      artist_id INTEGER PRIMARY KEY NOT NULL,
                                      author_name TEXT,
                                      sizable_avatar TEXT,
                                      pic TEXT,
                                      song_count INTEGER DEFAULT 0,
                                      album_count INTEGER DEFAULT 0,
                                      mv_count INTEGER DEFAULT 0,
                                      fansnums INTEGER DEFAULT 0,
                                      intro TEXT,
                                      long_intro TEXT,
                                      extra_info TEXT,
                                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_artist_name ON artist(author_name);

-- =============================================
-- 4. 专辑表
-- =============================================
CREATE TABLE IF NOT EXISTS album (
                                     album_id INTEGER PRIMARY KEY NOT NULL,
                                     album_name TEXT,
                                     sizable_cover TEXT,
                                     cover TEXT,
                                     publish_date TEXT,
                                     intro TEXT,
                                     song_count INTEGER DEFAULT 0,
                                     extra_info TEXT,
                                     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_album_name ON album(album_name);

-- =============================================
-- 5. 歌曲表 (核心表)
-- 使用 audio_id + hash 组合主键
-- =============================================
CREATE TABLE IF NOT EXISTS song (
                                    audio_id INTEGER NOT NULL,
                                    hash TEXT NOT NULL,
                                    mixsongid TEXT,
                                    name TEXT,
                                    songname TEXT,
                                    audio_name TEXT,
                                    filename TEXT,
                                    author_name TEXT,
                                    album_id INTEGER,
                                    album_name TEXT,
                                    cover TEXT,
                                    sizable_cover TEXT,
                                    duration INTEGER DEFAULT 0,
                                    bitrate INTEGER DEFAULT 128,
                                    filesize INTEGER DEFAULT 0,
                                    extname TEXT DEFAULT 'mp3',
                                    remark TEXT,
                                    privilege INTEGER DEFAULT 0,
                                    mvhash TEXT,
                                    hash_128 TEXT,
                                    hash_320 TEXT,
                                    hash_flac TEXT,
                                    language TEXT,
                                    publish_date TEXT,
                                    extra_info TEXT,
                                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                    PRIMARY KEY (audio_id, hash)
);

CREATE INDEX idx_song_audio_id ON song(audio_id);
CREATE INDEX idx_song_hash ON song(hash);
CREATE INDEX idx_song_name ON song(name);
CREATE INDEX idx_song_author ON song(author_name);
CREATE INDEX idx_song_album_id ON song(album_id);

-- =============================================
-- 6. 歌曲音质信息表 (relate_goods)
-- =============================================
CREATE TABLE IF NOT EXISTS song_quality (
                                            audio_id INTEGER NOT NULL,
                                            quality_hash TEXT NOT NULL,
                                            song_hash TEXT NOT NULL,
                                            level INTEGER DEFAULT 0,
                                            bitrate INTEGER DEFAULT 0,
                                            filesize INTEGER DEFAULT 0,
                                            privilege INTEGER DEFAULT 0,
                                            extra_info TEXT,
                                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                            PRIMARY KEY (audio_id, quality_hash)
);

CREATE INDEX idx_song_quality_hash ON song_quality(song_hash);

-- =============================================
-- 7. 歌单标签分类表
-- =============================================
CREATE TABLE IF NOT EXISTS playlist_tag_category (
                                                     tag_id INTEGER PRIMARY KEY NOT NULL,
                                                     tag_name TEXT NOT NULL,
                                                     parent_id INTEGER DEFAULT 0,
                                                     sort INTEGER DEFAULT 0,
                                                     extra_info TEXT,
                                                     created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_playlist_tag_category_parent ON playlist_tag_category(parent_id);

-- =============================================
-- 8. 歌单表
-- =============================================
CREATE TABLE IF NOT EXISTS playlist (
                                        global_collection_id TEXT PRIMARY KEY NOT NULL,
                                        listid INTEGER,
                                        list_create_listid INTEGER,
                                        list_create_userid INTEGER,
                                        list_create_username TEXT,
                                        list_create_gid TEXT,
                                        name TEXT,
                                        specialname TEXT,
                                        pic TEXT,
                                        flexible_cover TEXT,
                                        intro TEXT,
                                        tags TEXT,
                                        count INTEGER DEFAULT 0,
                                        sort INTEGER DEFAULT 0,
                                        authors TEXT,
                                        publish_date TEXT,
                                        extra_info TEXT,
                                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_playlist_listid ON playlist(listid);
CREATE INDEX idx_playlist_creator ON playlist(list_create_userid);

-- =============================================
-- 9. 歌单-歌曲关联表
-- =============================================
CREATE TABLE IF NOT EXISTS playlist_song (
                                             global_collection_id TEXT NOT NULL,
                                             audio_id INTEGER NOT NULL,
                                             song_hash TEXT NOT NULL,
                                             sort INTEGER DEFAULT 0,
                                             add_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                                             extra_info TEXT,
                                             PRIMARY KEY (global_collection_id, audio_id)
);

CREATE INDEX idx_playlist_song_audio_id ON playlist_song(audio_id);

-- =============================================
-- 10. 排行榜表
-- =============================================
CREATE TABLE IF NOT EXISTS rank_list (
                                         rankid INTEGER PRIMARY KEY NOT NULL,
                                         rankname TEXT,
                                         imgurl TEXT,
                                         album_cover_color TEXT,
                                         intro TEXT,
                                         update_frequency TEXT,
                                         extra_info TEXT,
                                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                         updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 11. 排行榜-歌曲关联表
-- =============================================
CREATE TABLE IF NOT EXISTS rank_song (
                                         rankid INTEGER NOT NULL,
                                         rank_cid INTEGER NOT NULL,
                                         audio_id INTEGER NOT NULL,
                                         song_hash TEXT NOT NULL,
                                         sort INTEGER DEFAULT 0,
                                         extra_info TEXT,
                                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                         PRIMARY KEY (rankid, rank_cid, audio_id)
);

CREATE INDEX idx_rank_song_audio_id ON rank_song(audio_id);

-- =============================================
-- 12. 用户关注歌手表
-- =============================================
CREATE TABLE IF NOT EXISTS user_follow_artist (
                                                  user_id INTEGER NOT NULL,
                                                  artist_id INTEGER NOT NULL,
                                                  source INTEGER DEFAULT 0,
                                                  pic TEXT,
                                                  add_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                                                  extra_info TEXT,
                                                  PRIMARY KEY (user_id, artist_id)
);

CREATE INDEX idx_user_follow_artist ON user_follow_artist(artist_id);

-- =============================================
-- 13. 用户听歌记录表
-- =============================================
CREATE TABLE IF NOT EXISTS user_listen_history (
                                                   user_id INTEGER NOT NULL,
                                                   audio_id INTEGER NOT NULL,
                                                   song_hash TEXT NOT NULL,
                                                   name TEXT,
                                                   image TEXT,
                                                   singername TEXT,
                                                   author_name TEXT,
                                                   duration INTEGER DEFAULT 0,
                                                   listen_count INTEGER DEFAULT 1,
                                                   last_listen_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                                                   extra_info TEXT,
                                                   PRIMARY KEY (user_id, audio_id)
);

CREATE INDEX idx_listen_history_time ON user_listen_history(last_listen_time);

-- =============================================
-- 14. 用户云盘歌曲表
-- =============================================
CREATE TABLE IF NOT EXISTS user_cloud_song (
                                               user_id INTEGER NOT NULL,
                                               hash TEXT NOT NULL,
                                               filename TEXT,
                                               name TEXT,
                                               author_name TEXT,
                                               album_name TEXT,
                                               timelen INTEGER DEFAULT 0,
                                               bitrate INTEGER DEFAULT 0,
                                               filesize INTEGER DEFAULT 0,
                                               extra_info TEXT,
                                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                               PRIMARY KEY (user_id, hash)
);

-- =============================================
-- 15. 用户云盘容量信息表
-- =============================================
CREATE TABLE IF NOT EXISTS user_cloud_storage (
                                                  user_id INTEGER PRIMARY KEY NOT NULL,
                                                  max_size INTEGER DEFAULT 0,
                                                  used_size INTEGER DEFAULT 0,
                                                  availble_size INTEGER DEFAULT 0,
                                                  extra_info TEXT,
                                                  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 16. 歌词索引表
-- 存储歌词文件的本地路径引用
-- =============================================
CREATE TABLE IF NOT EXISTS lyric (
                                     lyric_id TEXT NOT NULL,
                                     accesskey TEXT NOT NULL,
                                     audio_id INTEGER,
                                     song_hash TEXT,
                                     song_name TEXT,
                                     singer TEXT,
                                     duration INTEGER DEFAULT 0,
                                     file_path TEXT NOT NULL,
                                     fmt TEXT DEFAULT 'lrc',
                                     extra_info TEXT,
                                     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     PRIMARY KEY (lyric_id, accesskey)
);

CREATE INDEX idx_lyric_audio_id ON lyric(audio_id);
CREATE INDEX idx_lyric_hash ON lyric(song_hash);

-- =============================================
-- 17. 歌词候选表 (搜索歌词返回的候选列表)
-- =============================================
CREATE TABLE IF NOT EXISTS lyric_candidate (
                                               song_hash TEXT NOT NULL,
                                               lyric_id TEXT NOT NULL,
                                               accesskey TEXT NOT NULL,
                                               audio_id INTEGER,
                                               singer TEXT,
                                               song_name TEXT,
                                               duration INTEGER DEFAULT 0,
                                               score INTEGER DEFAULT 0,
                                               extra_info TEXT,
                                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                               PRIMARY KEY (song_hash, lyric_id)
);

CREATE INDEX idx_lyric_candidate_audio_id ON lyric_candidate(audio_id);

-- =============================================
-- 18. 每日推荐歌曲表
-- =============================================
CREATE TABLE IF NOT EXISTS daily_recommend (
                                               user_id INTEGER NOT NULL,
                                               recommend_date TEXT NOT NULL,
                                               song_hash TEXT NOT NULL,
                                               audio_id INTEGER,
                                               ori_audio_name TEXT,
                                               songname TEXT,
                                               sizable_cover TEXT,
                                               author_name TEXT,
                                               time_length INTEGER DEFAULT 0,
                                               filename TEXT,
                                               extra_info TEXT,
                                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                               PRIMARY KEY (user_id, recommend_date, song_hash)
);

CREATE INDEX idx_daily_recommend_date ON daily_recommend(recommend_date);

-- =============================================
-- 19. 热门卡片推荐表 (top/card)
-- =============================================
CREATE TABLE IF NOT EXISTS top_card_song (
                                             card_type TEXT NOT NULL,
                                             song_hash TEXT NOT NULL,
                                             audio_id INTEGER,
                                             songname TEXT,
                                             sizable_cover TEXT,
                                             author_name TEXT,
                                             time_length INTEGER DEFAULT 0,
                                             sort INTEGER DEFAULT 0,
                                             extra_info TEXT,
                                             updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                             PRIMARY KEY (card_type, song_hash)
);

-- =============================================
-- 20. 歌曲播放URL缓存表
-- =============================================
CREATE TABLE IF NOT EXISTS song_url_cache (
                                              hash TEXT NOT NULL,
                                              quality TEXT NOT NULL DEFAULT '128',
                                              audio_id INTEGER,
                                              url TEXT NOT NULL,
                                              backup_url TEXT,
                                              filesize INTEGER DEFAULT 0,
                                              time_length INTEGER DEFAULT 0,
                                              extname TEXT,
                                              expire_time DATETIME,
                                              extra_info TEXT,
                                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                              PRIMARY KEY (hash, quality)
);

CREATE INDEX idx_song_url_audio_id ON song_url_cache(audio_id);
CREATE INDEX idx_song_url_expire ON song_url_cache(expire_time);

-- =============================================
-- 21. 歌手-歌曲关联表
-- =============================================
CREATE TABLE IF NOT EXISTS artist_song (
                                           artist_id INTEGER NOT NULL,
                                           audio_id INTEGER NOT NULL,
                                           song_hash TEXT NOT NULL,
                                           sort INTEGER DEFAULT 0,
                                           extra_info TEXT,
                                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                           PRIMARY KEY (artist_id, audio_id)
);

CREATE INDEX idx_artist_song_audio_id ON artist_song(audio_id);

-- =============================================
-- 22. 专辑-歌曲关联表
-- =============================================
CREATE TABLE IF NOT EXISTS album_song (
                                          album_id INTEGER NOT NULL,
                                          audio_id INTEGER NOT NULL,
                                          song_hash TEXT NOT NULL,
                                          track_no INTEGER DEFAULT 0,
                                          extra_info TEXT,
                                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                          PRIMARY KEY (album_id, audio_id)
);

CREATE INDEX idx_album_song_audio_id ON album_song(audio_id);
