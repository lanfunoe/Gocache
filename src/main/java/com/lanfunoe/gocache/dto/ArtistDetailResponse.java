package com.lanfunoe.gocache.dto;

import java.util.List;

/**
 * 艺术家详情响应DTO
 */
public record ArtistDetailResponse(
    String birthday,
    Integer mvCount,
    String pinyinInitial,
    String authorName,
    String sizableAvatar,
    Integer isPublish,
    String authorId,
    Integer albumCount,
    Long fansnums,
    List<LongIntro> longIntro,
    String areaId,
    Integer songCount,
    String intro,
    Integer userStatus
) {
    public record LongIntro(
        String content,
        String title
    ) {}
}