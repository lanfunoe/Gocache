package com.lanfunoe.gocache.dto;

/**
 * 歌曲播放链接响应DTO
 */
public record SongUrlResponse(
    String extName,
    java.util.Map<String, Long> classmap,
    Integer status,
    Double volume,
    Long stdHashTime,
    java.util.List<String> backupUrl,
    java.util.List<String> url,
    String stdHash,
    TrackerThrough trackerThrough,
    TransParam transParam,
    Long fileHead,
    java.util.List<?> authThrough,
    Integer timeLength,
    Integer bitRate,
    Integer privStatus,
    Double volumePeak,
    Integer volumeGain,
    Integer q,
    String fileName,
    Long fileSize,
    String hash
) {
    public record TrackerThrough(
        Integer identityBlock,
        Integer cpyGrade,
        Integer musicpackAdvance,
        Integer allQualityFree,
        Integer cpyLevel
    ) {}

    public record TransParam(
        Integer displayRate,
        Integer display,
        String ogg128Hash,
        java.util.Map<String, Object> qualitymap,
        Integer payBlockTpl,
        String unionCover,
        String language,
        String hashMultitrack,
        Long cpyAttr0,
        java.util.Map<String, Long> ipmap,
        String ogg320Hash,
        java.util.Map<String, Long> classmap,
        Long ogg128Filesize,
        Long ogg320Filesize
    ) {}
}