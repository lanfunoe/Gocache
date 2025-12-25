package com.lanfunoe.gocache.dto;

/**
 * 歌曲高潮部分响应DTO
 */
public record SongClimaxResponse(
    java.util.List<ClimaxItem> data
) {
    public record ClimaxItem(
        String startTime,
        String endTime,
        String timelength,
        String authorName,
        String hash,
        String audioId,
        String audioName
    ) {}
}