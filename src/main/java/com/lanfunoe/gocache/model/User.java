package com.lanfunoe.gocache.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    /**
     * 用户ID（主键）
     */
    private Long userid;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像URL
     */
    private String pic;

    /**
     * 性别（1-男，2-女，0-未知）
     */
    private Integer gender;

    /**
     * 城市
     */
    private String city;

    /**
     * 省份
     */
    private String province;

    /**
     * 关注数
     */
    private Integer follows;

    /**
     * 粉丝数
     */
    private Integer fans;

    /**
     * VIP类型
     */
    private Integer vipType;

    /**
     * SVIP等级
     */
    private Integer svipLevel;
}
