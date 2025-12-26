package com.lanfunoe.gocache.dto;

/**
 * 歌单所有歌曲响应DTO
 */
public record PlaylistTrackAllResponse(
    Integer beginIdx,
    Integer pagesize,
    Integer count,
    java.util.Map<String, Object> popularization,
    String userid,
    java.util.List<Song> songs,
    ListInfo listInfo
) {
    public record Song(
        String hash,
        String brief,
        Long audioId,
        Integer mvtype,
        Long size,
        String publishDate,
        String name,
        Integer mvtrack,
        String bpmType,
        Long addMixsongid,
        String albumId,
        Integer bpm,
        String mvhash,
        String extname,
        String language,
        Long collecttime,
        Integer csong,
        String remark,
        Integer level,
        java.util.Map<String, Long> tagmap,
        Integer mediaOldCpy,
        java.util.List<RelateGood> relateGoods,
        java.util.List<Download> download,
        Integer rcflag,
        Integer feetype,
        Integer hasObbligato,
        Long timelen,
        Integer sort,
        TransParam transParam,
        String medistype,
        String userId,
        AlbumInfo albuminfo,
        Integer bitrate,
        String audioGroupId,
        Integer privilege,
        String cover,
        Long mixsongid,
        Integer fileid,
        Integer heat,
        java.util.List<SingerInfo> singerinfo
    ) {
        public record RelateGood(
            Long size,
            String hash,
            Integer level,
            Integer privilege,
            Integer bitrate
        ) {}

        public record Download(
            Integer status,
            String hash,
            Integer failProcess,
            Integer payType
        ) {}

        public record TransParam(
            Integer cpyGrade,
            String unionCover,
            Integer freeForAd,
            String language,
            Long cpyAttr0,
            Integer musicpackAdvance,
            Integer display,
            Integer displayRate,
            java.util.Map<String, Object> qualitymap,
            Long ogg320Filesize,
            Long cid,
            String ogg128Hash,
            Long ogg128Filesize,
            String ogg320Hash,
            java.util.Map<String, Long> ipmap,
            java.util.Map<String, Long> classmap,
            Integer payBlockTpl,
            Integer cpyLevel
        ) {}

        public record AlbumInfo(
            String name,
            String id,
            Integer publish
        ) {}

        public record SingerInfo(
            Long id,
            Integer publish,
            String name,
            String avatar,
            Integer type
        ) {}
    }

    public record ListInfo(
        java.util.List<?> abtags,
        String tags,
        Integer status,
        String create_user_pic,
        Integer isPri,
        Integer pubNew,
        Integer isDrop,
        String list_create_userid,
        Integer isPublish,
        java.util.List<MusiclibTag> musiclibTags,
        Integer pubType,
        Integer isFeatured,
        String publishDate,
        Integer collectTotal,
        Long specialid,
        Integer listVer,
        String intro,
        Integer type,
        Integer list_create_listid,
        Integer radioId,
        Integer source,
        TransParam transParam,
        Integer code,
        Integer isDef,
        String parentGlobalCollectionId,
        String soundQuality,
        Integer perCount,
        java.util.List<?> plist,
        Long createTime,
        Integer isPer,
        Integer isEdit,
        Long updateTime,
        Integer perNum,
        Integer count,
        Integer sort,
        Integer isMine,
        Integer listid,
        Integer musiclibId,
        Integer kqTalent,
        Integer create_user_gender,
        String pic,
        String list_create_username,
        String name,
        Integer isCustomPic,
        String globalCollectionId,
        Integer heat,
        String list_create_gid
    ) {
        public record MusiclibTag(
            String tagId,
            String parentId,
            String tagName
        ) {}

        public record TransParam(
            Integer iden
        ) {}
    }
}