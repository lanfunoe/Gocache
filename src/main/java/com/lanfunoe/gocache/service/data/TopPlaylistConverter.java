package com.lanfunoe.gocache.service.data;

import com.lanfunoe.gocache.dto.TopPlaylistResponse;
import com.lanfunoe.gocache.model.Playlist;
import com.lanfunoe.gocache.model.PlaylistTag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 热门歌单数据转换器
 * 负责TopPlaylistResponse和Playlist实体之间的转换
 * 仅保存能直接映射的字段，不使用extra_info
 */
@Slf4j
@Component
public class TopPlaylistConverter {

    /**
     * 将 TopPlaylistResponse.SpecialList 转换为 Playlist 实体
     *
     * @param specialList API返回的歌单项
     * @param categoryId 分类ID
     * @return Playlist实体
     */
    public Playlist toPlaylist(TopPlaylistResponse.SpecialList specialList, Integer categoryId) {
        Playlist playlist = new Playlist();

        // 主键字段
        playlist.setGlobalCollectionId(specialList.globalCollectionId());
        playlist.setCategoryId(categoryId != null ? categoryId : 0);

        // 直接映射的字段
        playlist.setListid(specialList.specialid() != null ? specialList.specialid().intValue() : null);
        playlist.setListCreateUsername(specialList.nickname());
        playlist.setListCreateUserid(specialList.suid());
        playlist.setSpecialname(specialList.specialname());
        playlist.setPic(specialList.pic());
        playlist.setFlexibleCover(specialList.flexibleCover());
        playlist.setIntro(specialList.intro());
        playlist.setCount(specialList.collectcount() != null ? specialList.collectcount().intValue() : null);
        // sort字段在SpecialList中不存在,跳过

        // 发布日期
        playlist.setPublishDate(specialList.publishtime());

        return playlist;
    }

    /**
     * 批量转换为Playlist实体列表
     *
     * @param specialList 歌单项列表
     * @param categoryId 分类ID
     * @return Playlist实体列表
     */
    public List<Playlist> toPlaylistList(List<TopPlaylistResponse.SpecialList> specialList, Integer categoryId) {
        if (specialList == null || specialList.isEmpty()) {
            return List.of();
        }

        return specialList.stream()
                .map(item -> toPlaylist(item, categoryId))
                .collect(Collectors.toList());
    }

    /**
     * 将 TopPlaylistResponse.SpecialList 转换为 PlaylistTag 实体列表
     *
     * @param specialList API返回的歌单项
     * @return PlaylistTag实体列表
     */
    public List<PlaylistTag> toPlaylistTagList(TopPlaylistResponse.SpecialList specialList) {
        if (specialList.tags() == null || specialList.tags().isEmpty()) {
            return List.of();
        }

        List<PlaylistTag> playlistTags = new ArrayList<>();
        String globalCollectionId = specialList.globalCollectionId();

        for (TopPlaylistResponse.SpecialList.PlaylistTagCategory tag : specialList.tags()) {
            if (tag.tagId() != null) {
                PlaylistTag playlistTag = new PlaylistTag();
                playlistTag.setGlobalCollectionId(globalCollectionId);
                playlistTag.setTagId(tag.tagId().longValue());
                playlistTags.add(playlistTag);
            }
        }

        return playlistTags;
    }

    /**
     * 批量转换为PlaylistTag实体列表
     *
     * @param specialList 歌单项列表
     * @return PlaylistTag实体列表
     */
    public List<PlaylistTag> toPlaylistTagList(List<TopPlaylistResponse.SpecialList> specialList) {
        if (specialList == null || specialList.isEmpty()) {
            return List.of();
        }

        return specialList.stream()
                .flatMap(item -> toPlaylistTagList(item).stream())
                .collect(Collectors.toList());
    }
}