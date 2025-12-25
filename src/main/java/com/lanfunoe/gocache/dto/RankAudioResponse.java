package com.lanfunoe.gocache.dto;

/**
 * 音频排行榜响应DTO
 */
public record RankAudioResponse(
    Integer total,
    java.util.List<SongList> songlist
) {
    public record SongList(
        java.util.List<?> mvdata,
        java.util.List<Author> authors,
        String authorName,
        Copyright copyright,
        Long audioId,
        VideoInfo videoInfo,
        java.util.Map<String, Object> musical,
        java.util.List<?> remarks,
        TransParam transParam,
        Integer isPublish,
        UserDownload userDownload,
        String songname,
        Integer hasObbligato,
        PrivilegeDownload privilegeDownload,
        String zone,
        Long albumAudioId,
        AudioInfo audioInfo,
        GoodsInfo goodsInfo,
        Long albumId,
        AlbumInfo albumInfo,
        TransObj transObj,
        Business business,
        Deprecated deprecated,
        Integer rankCid
    ) {
        public record Author(
            String sizableAvatar,
            Integer isPublish,
            Long authorId,
            String authorName
        ) {}

        public record Copyright(
            Integer oldHide
        ) {}

        public record VideoInfo(
            Long videoId,
            Integer videoTrack,
            Long videoTimelength,
            Long videoFilesize,
            String videoHash
        ) {}

        public record TransParam(
            String ogg128Hash,
            java.util.Map<String, Long> classmap,
            String language,
            Long cpyAttr0,
            Integer musicpackAdvance,
            Long ogg128Filesize,
            Integer displayRate,
            java.util.Map<String, Object> qualitymap,
            String unionCover,
            Long ogg320Filesize,
            Long cid,
            Integer payBlockTpl,
            Integer display,
            String ogg320Hash,
            HashOffset hashOffset,
            java.util.Map<String, Long> ipmap,
            Integer cpyGrade,
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

        public record UserDownload(
            Integer status320,
            Integer status128,
            Integer statusFlac,
            Integer statusHigh,
            Integer statusSuper
        ) {}

        public record PrivilegeDownload(
            Integer failProcess128,
            Integer failProcess320,
            Integer failProcessHigh,
            Integer privilegeFlac,
            Integer failProcess,
            Integer failProcessSuper,
            Integer failProcessFlac,
            Integer privilege,
            Integer privilegeHigh,
            Integer privilege320,
            Integer privilegeSuper,
            Integer privilege128
        ) {}

        public record AudioInfo(
            Long filesizeFlac,
            Integer bitrate,
            Long durationFlac,
            Long duration128,
            Long duration320,
            String hash320,
            Integer bitrateHigh,
            Long durationHigh,
            String hashFlac,
            Long filesize128,
            Long filesizeSuper,
            String hash128,
            String extnameSuper,
            Integer bitrateSuper,
            Integer bitrateFlac,
            Long durationSuper,
            String hashSuper,
            String hashHigh,
            String extname,
            Long filesize320,
            Long filesizeHigh
        ) {}

        public record GoodsInfo(
            String albumSaleUrl
        ) {}

        public record AlbumInfo(
            String sizableCover,
            String albumName
        ) {}

        public record TransObj(
            Integer rankShowSort
        ) {}

        public record Business(
            Integer lastSort,
            String buyCount,
            Integer level,
            Integer originalIndex,
            Integer rankCount,
            String rankIdPublishDate,
            String extern,
            String filename,
            Integer sort,
            Integer maxSort,
            Integer exclusive,
            String issue,
            String recommendReason,
            String albumAudioRemark,
            Integer isRecentYear,
            Long offset,
            String addtime,
            String rankId,
            String parentId,
            Integer lastOriginalIndex
        ) {}

        public record Deprecated(
            String hash,
            Integer pkgPriceHigh,
            Integer status,
            Integer bitrate,
            Integer price128,
            String type320,
            Integer payType320,
            Integer payType128,
            String cdUrl,
            Integer payTypeFlac,
            Integer priceFlac,
            String type,
            Long id,
            String typeSuper,
            Integer oldCpy,
            String typeHigh,
            Integer pkgPriceSuper,
            String type128,
            Integer pkgPrice320,
            String topicRemark,
            Integer payTypeHigh,
            Integer price,
            Integer price320,
            Integer priceSuper,
            Integer priceHigh,
            Long duration,
            String typeFlac,
            Long cid,
            Integer pkgPrice,
            Long filesize,
            String extname,
            Integer pkgPriceFlac,
            Integer payType,
            Integer payTypeSuper,
            Integer pkgPrice128
        ) {}
    }
}