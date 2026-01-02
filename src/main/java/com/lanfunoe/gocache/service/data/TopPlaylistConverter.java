package com.lanfunoe.gocache.service.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanfunoe.gocache.dto.TopPlaylistResponse;
import com.lanfunoe.gocache.model.Playlist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 热门歌单数据转换器
 * 负责TopPlaylistResponse和Playlist实体之间的转换
 * 仅保存能直接映射的字段，不使用extra_info
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TopPlaylistConverter {

    private final ObjectMapper objectMapper;

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

        // 标签转换为JSON数组//todo:
//        if (specialList.tags() != null && !specialList.tags().isEmpty()) {
//            try {
//                String tagsJson = objectMapper.writeValueAsString(specialList.tags());
//                playlist.setTags(tagsJson);
//            } catch (JsonProcessingException e) {
//                log.warn("Failed to serialize tags for playlist: {}", specialList.globalCollectionId(), e);
//                playlist.setTags("[]");
//            }
//        } else {
//            playlist.setTags("[]");
//        }

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
}