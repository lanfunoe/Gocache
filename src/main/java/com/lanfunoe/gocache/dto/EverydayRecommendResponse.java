package com.lanfunoe.gocache.dto;

/**
 * 每日推荐响应DTO
 */
public record EverydayRecommendResponse(
    String creationDate,
    String mid,
    String biBiz,
    String sign,
    Integer songListSize,
    String OlexpIds,
    Integer clientPlaylistFlag,
    Integer isGuaranteeRec,
    java.util.List<SongItem> songList,
    String subTitle,
    String coverImgUrl
) {
    public record SongItem(
        Long filesizeFlac,
        String officialSongname,
        String oriAudioName,
        String hash192,
        String hashFlac,
        String songname,
        Integer musicTrac,
        Integer isOriginal,
        Integer payType,
        String songType,
        String albumId,
        String remark,
        String language,
        Integer isFileHead320,
        String algPath,
        Integer isFileHead,
        String filename,
        Long cid,
        Long scid,
        String suffixAudioName,
        String recCopyWrite,
        String mvHash,
        String hash,
        String authorName,
        java.util.List<Tag> tags,
        String rankLabel,
        Integer bitrate,
        Integer isMvFileHead,
        Integer hasAccompany,
        Long filesize128,
        String albumName,
        String ztcMark,
        Long climaxEndTime,
        Long songid,
        Integer qualityLevel,
        Long filesize192,
        String publishDate,
        Long fileSize,
        Integer hasAlbum,
        String extname,
        String type,
        Long filesize320,
        Integer level,
        Long timeLength,
        String recCopyWriteId,
        Integer oldCpy,
        String recLabelPrefix,
        String hash128,
        String hash320,
        java.util.Map<String, Object> relateGoods,
        String mixsongid,
        String hashOther,
        String sizableCover,
        Integer mvType,
        Long publishTime,
        Long filesizeApe,
        Integer recLabelType,
        java.util.List<SingerInfo> singerinfo,
        String hashApe,
        TransParam transParam,
        Long timelength320,
        String albumAudioRemark,
        String albumAudioId,
        Long filesizeOther,
        java.util.List<IpsTag> ipsTags,
        Integer privilege,
        Integer failProcess,
        Long climaxStartTime,
        Long climaxTimelength,
        Integer isPublish,
        String recSubCopyWrite
    ) {
        public record Tag(
            String tagId,
            String parentId,
            String tagName
        ) {}

        public record SingerInfo(
            String name,
            String isPublish,
            String id
        ) {}

        public record IpsTag(
            String name,
            String ipId,
            String pid
        ) {}

        public record TransParam(
            Integer cpyGrade,
            java.util.Map<String, Long> classmap,
            String language,
            Long cpyAttr0,
            Integer musicpackAdvance,
            Long ogg128Filesize,
            Integer displayRate,
            Integer cpyLevel,
            Integer payBlockTpl,
            java.util.Map<String, Object> qualitymap,
            String hashMultitrack,
            HashOffset hashOffset,
            Long cid,
            Integer display,
            String ogg320Hash,
            java.util.Map<String, Long> ipmap,
            String appidBlock,
            String ogg128Hash,
            String unionCover,
            Long ogg320Filesize
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