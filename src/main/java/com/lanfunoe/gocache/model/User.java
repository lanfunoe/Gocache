package com.lanfunoe.gocache.model;

import lombok.Data;

import java.time.LocalDateTime;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;




/**
 * 用户实体
 */
@Table("user")
@Data
public class User {

    /**
     * 用户ID（主键）
     */
    @Id
    @Column("user_id")
    private Long userId;

    /**
     * 昵称
     */
    @Column("nickname")
    private String nickname;

    /**
     * 背景图
     */
    @Column("bg_pic")
    private String bgPic;

    /**
     * 头像
     */
    @Column("pic")
    private String pic;

    /**
     * 性别（1-男，2-女，0-未知）
     */
    @Column("gender")
    private Integer gender;

    /**
     * 等级
     */
    @Column("p_grade")
    private Integer pGrade;

    /**
     * 个人描述
     */
    @Column("descri")
    private String descri;

    /**
     * 关注数
     */
    @Column("follows")
    private Integer follows;

    /**
     * 粉丝数
     */
    @Column("fans")
    private Integer fans;

    /**
     * 好友数
     */
    @Column("friends")
    private Integer friends;

    /**
     * 访客数
     */
    @Column("hvisitors")
    private Integer hvisitors;

    /**
     * 时长
     */
    @Column("duration")
    private Integer duration;

    /**
     * 注册时间
     */
    @Column("rtime")
    private Long rtime;

    /**
     * 额外信息（JSON格式）
     */
    @Column("extra_info")
    private String extraInfo;

    /**
     * 创建时间
     */
    @Column("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
