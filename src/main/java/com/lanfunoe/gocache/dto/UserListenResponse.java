package com.lanfunoe.gocache.dto;

/**
 * 用户听歌记录响应DTO
 */
public record UserListenResponse(
    Long dataDate,
    Long servertime,
    java.util.List<ListenSong> list,
    java.util.List<?> hideMixsongids,
    Integer listenTotal,
    Integer totalHistory,
    Integer totalWeek
) {
    public record ListenSong(
        String hash,
        java.util.List<Integer> imgsize,
        String image320,
        Integer bitrate,
        Long filesize320,
        Long filesize128,
        String hash320,
        Integer privilegeSq,
        Integer failProcess,
        Integer payType,
        java.util.List<Integer> imgsizeSq,
        String image,
        Long duration,
        String type,
        Long filesizeSq,
        String hashSq,
        Long id,
        Integer oldCpy,
        String singername,
        Integer privilege320,
        Integer privilege128,
        String imageSq,
        java.util.List<Integer> imgsize320,
        String hash128,
        java.util.List<Integer> imgsize128,
        Integer privilege,
        String extname,
        Long filesize,
        Long albumAudioId,
        String image128,
        Integer listenCount,
        String name,
        String albumName,
        TransParam transParam
    ) {
        public record TransParam(
            String ogg128Hash,
            java.util.Map<String, Long> classmap,
            String language,
            Long cpyAttr0,
            Integer musicpackAdvance,
            Integer display,
            Integer displayRate,
            String unionCover,
            Long ogg320Filesize,
            java.util.Map<String, Long> qualitymap,
            String ogg320Hash,
            Long ogg128Filesize,
            Long cid,
            Integer cpyGrade,
            String appidBlock,
            java.util.Map<String, Long> ipmap,
            HashOffset hashOffset,
            String hashMultitrack,
            Integer payBlockTpl,
            Integer cpyLevel
        ) {
            public record HashOffset(
                String clipHash,
                Long startByte,
                Long endMs,
                Long endByte,
                Integer fileType,
                Long startMs,
                String offsetHash
            ) {}
        }
    }
}