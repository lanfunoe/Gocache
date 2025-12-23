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
     * 酷狗昵称
     */
    private String kNickname;

    /**
     * 繁星昵称
     */
    private String fxNickname;

    /**
     * 用户头像URL
     */
    private String pic;

    /**
     * 酷狗头像
     */
    private String kPic;

    /**
     * 繁星头像
     */
    private String fxPic;

    /**
     * 背景图
     */
    private String bgPic;

    /**
     * 性别（1-男，2-女，0-未知）
     */
    private Integer gender;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 城市
     */
    private String city;

    /**
     * 省份
     */
    private String province;

    /**
     * 职业
     */
    private String occupation;

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
     * 访客数
     */
    private Integer visitors;

    /**
     * 好友数
     */
    private Integer friends;

    /**
     * VIP类型
     */
    private Integer vipType;

    /**
     * 音乐VIP类型
     */
    private Integer mType;

    /**
     * 年费VIP类型
     */
    private Integer yType;

    /**
     * SVIP等级
     */
    private Integer svipLevel;

    /**
     * SVIP积分
     */
    private Integer svipScore;

    /**
     * 等级
     */
    private Integer pGrade;

    /**
     * 星座
     */
    private Integer constellation;

    /**
     * 是否明星
     */
    private Integer isStar;

    /**
     * 明星ID
     */
    private Long starId;

    /**
     * 明星状态
     */
    private Integer starStatus;

    /**
     * 歌手状态
     */
    private Integer singerStatus;

    /**
     * 认证信息
     */
    private String authInfo;

    /**
     * 歌手认证
     */
    private String authInfoSinger;

    /**
     * 最后登录时间
     */
    private Long logintime;

    /**
     * 注册时间
     */
    private Long rtime;
}
