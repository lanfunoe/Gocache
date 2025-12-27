package com.lanfunoe.gocache.repository;

import com.lanfunoe.gocache.model.Song;
import com.lanfunoe.gocache.model.SongHashId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 歌曲 Repository 接口
 * 使用复合主键 (audio_id, hash)
 */
public interface SongRepository extends ReactiveRepository<Song, SongHashId> {

    /**
     * 根据 Hash 查找歌曲（可能返回多个，因为不同 audio_id 可能有相同 hash）
     *
     * @param hash 歌曲Hash
     * @return 歌曲列表
     */
    Flux<Song> findByHash(String hash);



    /**
     * 根据专辑ID查找歌曲
     *
     * @param albumId 专辑ID
     * @return 歌曲列表
     */
    Flux<Song> findByAlbumId(Long albumId);

    /**
     * 根据歌手ID查找歌曲
     *
     * @param singerId 歌手ID
     * @return 歌曲列表
     */
    Flux<Song> findBySingerId(Long singerId);

    /**
     * 根据歌单ID查找歌曲
     *
     * @param playlistId 歌单ID
     * @return 歌曲列表
     */
    Flux<Song> findByPlaylistId(Long playlistId);

    /**
     * 根据排行榜ID查找歌曲
     *
     * @param rankId       排行榜ID
     * @param snapshotDate 快照日期
     * @return 歌曲列表
     */
    Flux<Song> findByRankId(Long rankId, String snapshotDate);

    /**
     * 根据歌曲名称搜索
     *
     * @param keyword 关键词
     * @return 歌曲列表
     */
    Flux<Song> searchByName(String keyword);

    /**
     * 保存歌曲及其关联关系
     *
     * @param song      歌曲实体
     * @param singerIds 歌手ID列表
     * @return 完成信号
     */
    Mono<Void> saveSongWithSingers(Song song, List<Long> singerIds);

    /**
     * 保存歌单-歌曲关联
     *
     * @param playlistId 歌单ID
     * @param songIds    歌曲复合主键列表
     * @return 完成信号
     */
    Mono<Void> savePlaylistSongs(Long playlistId, List<SongHashId> songIds);

    /**
     * 保存排行榜-歌曲关联
     *
     * @param rankId       排行榜ID
     * @param songIds      歌曲复合主键列表
     * @param snapshotDate 快照日期
     * @return 完成信号
     */
    Mono<Void> saveRankSongs(Long rankId, List<SongHashId> songIds, String snapshotDate);

    /**
     * 保存每日推荐
     *
     * @param userId        用户ID
     * @param songIds       歌曲复合主键列表
     * @param recommendDate 推荐日期
     * @return 完成信号
     */
    Mono<Void> saveEverydayRecommend(String userId, List<SongHashId> songIds, String recommendDate);

    /**
     * 获取每日推荐歌曲
     *
     * @param recommendDate 推荐日期
     * @return 歌曲列表
     */
    Flux<Song> findEverydayRecommend(String recommendDate);
}
