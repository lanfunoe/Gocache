package com.lanfunoe.gocache.dto;

/**
 * 热门歌单响应DTO
 */
public record TopPlaylistResponse(
    Integer hasNext,
    String biBiz,
    String session,
    Integer algId,
    java.util.List<SpecialList> specialList,
    String OlexpIds,
    Integer showTime,
    Integer allClientPlaylistFlag,
    Integer refreshTime
) {
    public record SpecialList(
        Integer sync,
        String publishtime,
        Long specialid,
        Integer percount,
        ListInfoTransParam listInfoTransParam,
        Integer bzStatus,
        String singername,
        Integer from,
        String algPath,
        java.util.List<Tag> tags,
        Integer ugcTalentReview,
        Integer type,
        Integer slid,
        String flexibleCover,
        String nickname,
        String show,
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
        Long suid
    ) {
        public record ListInfoTransParam(
            Integer specialTag,
            Integer iden,
            Integer transFlag
        ) {}

        public record Tag(
            String tagName,
            Integer tagId
        ) {}

        public record TransParam(
            Integer specialTag
        ) {}
    }
}