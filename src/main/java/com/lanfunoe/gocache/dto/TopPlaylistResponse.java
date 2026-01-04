package com.lanfunoe.gocache.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 热门歌单响应DTO
 */
public record TopPlaylistResponse(
    Integer hasNext,
    String biBiz,
    String session,
    Integer algId,
    java.util.List<SpecialList> specialList,
    @JsonProperty("OlexpIds")
    String OlexpIds,
    Integer showTime,
    Integer allClientPlaylistFlag,
    Integer refreshTime
) {
    public record SpecialList(
        Integer sync,
        String publishtime,
        Integer specialid,
        Integer percount,
        ListInfoTransParam listInfoTransParam,
        Integer bzStatus,
        String singername,
        Integer from,
        String algPath,
        List<PlaylistTagCategory> tags,
        Integer ugcTalentReview,
        Integer type,
        Integer slid,
        String flexibleCover,
        String nickname,
        String show,
        @JsonProperty("collectType")
        Integer collectType,
        Long collectcount,
        TransParam transParam,
        String reportInfo,
        String specialname,
        String imgurl,
        Long playCount,
        String pic,
        String fromHash,
        Integer fromTag,
        String globalCollectionId,
        String intro,
        Long suid,
        Object abtags,
        Object ztcInfo
    ) {
        public record ListInfoTransParam(
            Integer specialTag,
            Integer iden,
            Integer transFlag,
            Object skin
        ) {}

        public record PlaylistTagCategory(
            String tagName,
            Integer tagId
        ) {}

        public record TransParam(
            Integer specialTag
        ) {}
    }
}