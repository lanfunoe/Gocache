package com.lanfunoe.gocache.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

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
        List<SongItem> songList,
        String subTitle,
        String coverImgUrl
) {
    public record SongItem(
            Long filesizeFlac,
            String officialSongname,
            String oriAudioName,
            @JsonProperty("hash_192")
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
            @JsonProperty("is_file_head_320")
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
            List<Tag> tags,
            String rankLabel,
            @JsonProperty("rank_id")
            String rankId,
            Integer bitrate,
            Integer isMvFileHead,
            Integer hasAccompany,
            @JsonProperty("filesize_128")
            Long filesize128,
            String albumName,
            String ztcMark,
            Long climaxEndTime,
            Long songid,
            Integer qualityLevel,
            @JsonProperty("filesize_192")
            Long filesize192,
            String publishDate,
            Long fileSize,
            Integer hasAlbum,
            String extname,
            String type,
            @JsonProperty("filesize_320")
            Long filesize320,
            Integer level,
            Long timeLength,
            String recCopyWriteId,
            Integer oldCpy,
            String recLabelPrefix,
            @JsonProperty("hash_128")
            String hash128,
            @JsonProperty("hash_320")
            String hash320,
            Map<String, Object> relateGoods,
            String mixsongid,
            String hashOther,
            String sizableCover,
            Integer mvType,
            Long publishTime,
            Long filesizeApe,
            Integer recLabelType,
            List<SingerInfo> singerinfo,
            String hashApe,
            TransParam transParam,
            @JsonProperty("timelength_320")
            Long timelength320,
            String albumAudioRemark,
            String albumAudioId,
            Long filesizeOther,
            List<IpsTag> ipsTags,
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
        ) {
        }

        public record SingerInfo(
                String name,
                String isPublish,
                String id
        ) {
        }

        public record IpsTag(
                String name,
                String ipId,
                String pid
        ) {
        }

        public record TransParam(
                Integer cpyGrade,
                Map<String, Long> classmap,
                String language,
                Long cpyAttr0,
                Integer musicpackAdvance,
                @JsonProperty("ogg_128_filesize")
                Long ogg128Filesize,
                Integer displayRate,
                Integer cpyLevel,
                Integer payBlockTpl,
                Map<String, Object> qualitymap,
                String hashMultitrack,
                HashOffset hashOffset,
                Long cid,
                Integer display,
                @JsonProperty("ogg_320_hash")
                String ogg320Hash,
                Map<String, Long> ipmap,
                String appidBlock,
                @JsonProperty("ogg_128_hash")
                String ogg128Hash,
                String unionCover,
                @JsonProperty("ogg_320_filesize")
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
            ) {
            }
        }
    }
}