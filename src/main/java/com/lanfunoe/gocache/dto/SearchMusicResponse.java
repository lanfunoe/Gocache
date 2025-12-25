package com.lanfunoe.gocache.dto;

/**
 * 搜索音乐响应DTO
 */
public record SearchMusicResponse(
    String correctiontip,
    Integer pagesize,
    Integer page,
    Integer correctiontype,
    String correctionrelate,
    Integer total,
    java.util.List<SearchSong> lists,
    Integer size,
    Integer allowerr,
    String algPath,
    java.util.List<?> secAggreV2,
    Integer correctionforce,
    Integer istag,
    Integer from,
    Integer istagresult,
    Integer isshareresult
) {
    public record SearchSong(
        String publishTime,
        Long audioid,
        Integer oldCpy,
        Integer publishAge,
        Integer bitflag,
        Integer payType,
        SongAccNode songAccNode,
        Integer accompany,
        String singerName,
        Integer showingFlag,
        String source,
        String albumAux,
        String image,
        SQ sq,
        HQ hq,
        Long m4aSize,
        Integer heatLevel,
        TransParam transParam,
        String uploaderContent,
        Long filesize,
        Integer isOriginal,
        String fileHash,
        Integer foldType,
        java.util.List<?> grp,
        Integer isPrepublish,
        String type,
        Integer bitrate,
        String extName,
        Integer topId,
        Integer albumPrivilege,
        String albumId,
        String albumName,
        java.util.List<MvData> mvdata,
        String otherName,
        Res res,
        Integer sourceId,
        String mixSongId,
        Integer failProcess,
        String suffix,
        Integer matchFlag,
        Long scid,
        java.util.List<Singer> singers,
        String auxiliary,
        Integer rankId,
        String publishDate,
        java.util.List<?> tagDetails,
        String tagContent,
        PrepublishInfo prepublishInfo,
        Integer ownerCount,
        String uploader,
        Integer duration,
        String oriSongName,
        String fileName,
        Integer recommendType
    ) {
        public record SongAccNode(
            Integer round,
            String query,
            Integer rewriteType,
            Integer source,
            Integer recallType,
            Integer matchLevel,
            Integer recallIntent
        ) {}

        public record SQ(
            Long fileSize,
            String hash,
            Integer privilege
        ) {}

        public record HQ(
            Long fileSize,
            String hash,
            Integer privilege
        ) {}

        public record TransParam(
            String unionCover,
            Long ogg320Filesize,
            String ogg320Hash,
            Long ogg128Filesize,
            Integer payBlockTpl,
            java.util.Map<String, Long> classmap,
            String ogg128Hash,
            java.util.Map<String, Long> ipmap,
            Long cid,
            String language,
            Long cpyAttr0,
            String hashMultitrack,
            java.util.Map<String, Object> qualitymap,
            Integer musicpackAdvance,
            Integer display,
            Integer displayRate
        ) {}

        public record MvData(
            Integer typ,
            String trk,
            String hash,
            String id
        ) {}

        public record Res(
            Long fileSize,
            Integer privilege,
            String hash,
            Integer bitRate,
            Integer timeLength
        ) {}

        public record Singer(
            String name,
            Long ipId,
            Long id
        ) {}

        public record PrepublishInfo(
            Integer reserveCount,
            String displayTime,
            Long id,
            String publishTime
        ) {}
    }
}