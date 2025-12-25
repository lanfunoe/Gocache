package com.lanfunoe.gocache.dto;

/**
 * 搜索歌词响应DTO
 */
public record SearchLyricResponse(
    Integer status,
    String info,
    Integer errcode,
    String errmsg,
    String keyword,
    String proposal,
    Integer hasCompleteRight,
    String companys,
    Integer ugc,
    Integer ugccount,
    Integer expire,
    java.util.List<Candidate> candidates,
    java.util.List<?> ugccandidates,
    java.util.List<Artist> artists,
    java.util.List<?> aiCandidates
) {
    public record Candidate(
        String id,
        String productFrom,
        String accesskey,
        Boolean canScore,
        String singer,
        String song,
        Long duration,
        String uid,
        String nickname,
        String origiuid,
        String transuid,
        String sounduid,
        String originame,
        String transname,
        String soundname,
        java.util.List<?> parinfo,
        java.util.List<?> parinfoExt,
        String language,
        Integer krctype,
        Integer hitlayer,
        Integer hitcasemask,
        Integer adjust,
        Integer score,
        Integer contenttype,
        Integer contentFormat,
        String downloadId
    ) {}

    public record Artist(
        Integer identity,
        Base base
    ) {
        public record Base(
            Long authorId,
            String authorName,
            Integer isPublish,
            String avatar,
            Integer identity,
            Integer type,
            String country,
            String birthday,
            String language
        ) {}
    }
}