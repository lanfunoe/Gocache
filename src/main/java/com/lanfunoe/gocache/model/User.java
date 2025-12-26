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
    private String userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 背景图
     */
    private String bgPic;

    /**
     * 头像
     */
    private String pic;

    /**
     * 性别（1-男，2-女，0-未知）
     */
    private Integer gender;

    /**
     * 等级
     */
    private Integer pGrade;

    /**
     * 个人描述
     */
    private String descri;

    /**
     * 关注数
     */
    private Integer follows;

    /**
     * 粉丝数
     */
    private Integer fans;

    /**
     * 好友数
     */
    private Integer friends;

    /**
     * 访客数
     */
    private Integer hvisitors;

    /**
     * 时长
     */
    private Integer duration;

    /**
     * 注册时间
     */
    private Long rtime;

    /**
     * 额外信息（JSON格式）
     */
    private String extraInfo;
}
