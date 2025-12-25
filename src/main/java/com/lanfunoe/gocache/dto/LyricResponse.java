package com.lanfunoe.gocache.dto;

/**
 * 歌词响应DTO
 */
public record LyricResponse(
    Integer status,
    String info,
    Integer errorCode,
    String fmt,
    Integer contenttype,
    String source,
    String charset,
    String content,
    String id,
    String decodeContent
) {}