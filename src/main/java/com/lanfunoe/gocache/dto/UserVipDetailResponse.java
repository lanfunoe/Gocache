package com.lanfunoe.gocache.dto;

/**
 * 用户VIP信息响应DTO
 */
public record UserVipDetailResponse(
    Integer isVip,
    Integer roamType,
    String mResetTime,
    String mYEndtime,
    String vipClearday,
    Integer vipType,
    String vipBeginTime,
    String roamBeginTime,
    String vipEndTime,
    Long userid,
    String vipYEndtime,
    String mClearday,
    Integer svipLevel,
    Integer svipScore,
    String suVipClearday,
    String suVipEndTime,
    String suVipYEndtime,
    String suVipBeginTime,
    java.util.List<BusiVip> busiVip,
    String mBeginTime,
    Integer userYType,
    Integer userType,
    Integer yType,
    String mEndTime,
    String roamEndTime,
    Integer mIsOld,
    Integer mType
) {
    public record BusiVip(
        Integer isVip,
        Integer purchasedIosType,
        Integer purchasedType,
        Integer isPaidVip,
        String vipClearday,
        String latestProductId,
        String productType,
        String vipBeginTime,
        Integer yType,
        String vipEndTime,
        Long userid,
        VipLimitQuota vipLimitQuota,
        String paidVipExpireTime,
        String busiType
    ) {
        public record VipLimitQuota(Integer total) {}
    }
}