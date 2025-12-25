package com.lanfunoe.gocache.dto;

/**
 * 用户歌单响应DTO
 */
public record UserPlaylistResponse(
    java.util.List<PlaylistInfo> info,
    Integer phoneFlag,
    Integer totalVer,
    Long userid,
    Integer albumCount,
    Integer listCount,
    Integer collectCount
) {
    public record PlaylistInfo(
        String tags,
        Integer status,
        String create_user_pic,
        Integer per_num,
        Integer pub_new,
        Integer is_drop,
        Long list_create_userid,
        Integer is_publish,
        java.util.List<?> musiclib_tags,
        Long pub_time,
        Integer pub_type,
        Integer incr_sync,
        Integer list_ver,
        String intro,
        Integer type,
        Integer list_create_listid,
        Integer radio_id,
        Integer source,
        Integer is_del,
        Integer is_mine,
        Integer per_count,
        Integer m_count,
        Long create_time,
        Integer kq_talent,
        Integer is_edit,
        Long update_time,
        Integer is_def,
        String sound_quality,
        Integer sort,
        TransParam trans_param,
        String list_create_gid,
        String global_collection_id,
        Integer is_per,
        Integer is_pri,
        String pic,
        String list_create_username,
        Integer is_featured,
        Integer is_custom_pic,
        Integer listid,
        String name,
        Integer count
    ) {
        public record TransParam(Integer iden) {}
    }
}