package com.lanfunoe.gocache.dto;

import java.util.List;

/**
 * 歌单标签响应DTO
 */
public record TagResponse(
    String parentId,
    String sort,
    String tagId,
    String tagName,
    List<SonTag> son
) {
    public record SonTag(
        String parentId,
        String tagId,
        String tagName,
        String sort
    ) {}
}
