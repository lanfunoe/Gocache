package com.lanfunoe.gocache.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 每日推荐元数据实体类
 * 存储每日推荐的元数据信息，歌曲列表通过关联表获取
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EverydayRecommend extends BaseEntity {

    /**
     * 推荐日期 (yyyyMMdd 格式)，作为主键
     */
    @JsonIgnore
    private String recommendDate;

    /**
     * API 返回的创建日期
     */
    @JsonProperty("creation_date")
    private String creationDate;

    /**
     * 唯一标识
     */
    @JsonProperty("mid")
    private String mid;

    /**
     * 业务标识
     */
    @JsonProperty("bi_biz")
    private String biBiz;

    /**
     * 签名
     */
    @JsonProperty("sign")
    private String sign;

    /**
     * 歌曲数量
     */
    @JsonProperty("song_list_size")
    private Integer songListSize;

    /**
     * 副标题
     */
    @JsonProperty("sub_title")
    private String subTitle;

    /**
     * 封面图片 URL
     */
    @JsonProperty("cover_img_url")
    private String coverImgUrl;

    /**
     * 客户端歌单标志
     */
    @JsonProperty("client_playlist_flag")
    private Integer clientPlaylistFlag;

    /**
     * 是否保底推荐
     */
    @JsonProperty("is_guarantee_rec")
    private Integer isGuaranteeRec;

    /**
     * 歌曲列表（非持久化字段，从关联表加载）
     */
    @JsonProperty("song_list")
    private transient List<Song> songList;
}
