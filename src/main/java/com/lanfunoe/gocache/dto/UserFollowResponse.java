package com.lanfunoe.gocache.dto;

/**
 * 用户关注列表响应DTO
 */
public record UserFollowResponse(
    Integer total,
    Integer listVer,
    java.util.List<FollowItem> lists
) {
    public record FollowItem(
        Integer state,
        Integer source,
        Integer jumptype,
        Integer status,
        Long userid,
        Integer idenType,
        String nickname,
        Long singerid,
        Long addtime,
        String pic,
        Integer identity,
        String sourceDesc,
        Integer score
    ) {}
}