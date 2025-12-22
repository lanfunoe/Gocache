# 接口文档

***

## 目录

1. [用户模块](#%E7%94%A8%E6%88%B7%E6%A8%A1%E5%9D%97)
2. [音乐库模块](#%E9%9F%B3%E4%B9%90%E5%BA%93%E6%A8%A1%E5%9D%97)
3. [搜索模块](#%E6%90%9C%E7%B4%A2%E6%A8%A1%E5%9D%97)
4. [播放模块](#%E6%92%AD%E6%94%BE%E6%A8%A1%E5%9D%97)
5. [推荐模块](#%E6%8E%A8%E8%8D%90%E6%A8%A1%E5%9D%97)
6. [错误码说明](#%E9%94%99%E8%AF%AF%E7%A0%81%E8%AF%B4%E6%98%8E)

***

## 用户模块

### 1.1 获取用户详情

**接口地址**: `/user/detail`  
**请求方法**: `GET`  
**接口描述**: 获取当前登录用户的详细信息

**请求参数**: 无

**响应示例**:

```json
{
  "data": {
    "nickname": "示例用户",
    "k_nickname": "示例用户12345",
    "fx_nickname": "示例用户678",
    "kq_talent": 0,
    "pic": "http://example.com/kugouicon/165/xxxxxxxx/xxxxxxxxxxxxxxxxxxxxxx.jpg",
    "k_pic": "http://example.com/kugouicon/165/xxxxxxxx/xxxxxxxxxxxxxxxxxxxxxx.jpg",
    "fx_pic": "http://example.com/kugouicon/165/xxxxxxxx/xxxxxxxxxxxxxxxxxxxxxx.jpg",
    "gender": 1,
    "vip_type": 0,
    "m_type": 0,
    "y_type": 0,
    "descri": "",
    "follows": 10,
    "fans": 5,
    "visitors": 100,
    "constellation": -1,
    "medal": {
      "ktv": {
        "type3": "C0",
        "type2": "B0",
        "type1": "A0"
      },
      "fx": {
        "vipLevel": 0,
        "richLevel": 0,
        "starLevel": 0
      }
    },
    "star_status": 0,
    "star_id": 0,
    "birthday": "",
    "city": "",
    "province": "",
    "occupation": "",
    "bg_pic": "",
    "relation": 0,
    "auth_info": "",
    "auth_info_singer": "",
    "auth_info_talent": "",
    "tme_star_status": 0,
    "biz_status": 0,
    "p_grade": 10,
    "friends": 0,
    "face_auth": 0,
    "avatar_review": 0,
    "servertime": 1700000000,
    "bookvip_valid": 0,
    "iden": 0,
    "is_star": -1,
    "knock_cnt": 0,
    "knock": [],
    "real_auth": 0,
    "risk_symbol": 0,
    "user_like": 0,
    "user_is_like": 0,
    "user_likeid": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
    "top_number": 0,
    "top_version": "0",
    "main_short_case": "",
    "main_long_case": "",
    "guest_short_case": "",
    "singer_status": 0,
    "bc_code": "",
    "arttoy_avatar": "",
    "visitor_visible": 1,
    "config_val": 2047,
    "config_val1": 32767,
    "kuqun_visible": 1,
    "user_type": 0,
    "user_y_type": 0,
    "su_vip_begin_time": "",
    "su_vip_end_time": "",
    "su_vip_clearday": "",
    "su_vip_y_endtime": "",
    "logintime": 1700000000,
    "loc": "",
    "comment_visible": 1,
    "student_visible": 1,
    "followlist_visible": 0,
    "fanslist_visible": 1,
    "info_visible": 1,
    "follow_visible": 1,
    "listen_visible": 1,
    "album_visible": 1,
    "pictorial_visible": 1,
    "radio_visible": 1,
    "sound_visible": 1,
    "applet_visible": 1,
    "selflist_visible": 1,
    "collectlist_visible": 1,
    "lvideo_visible": 1,
    "svideo_visible": 1,
    "mv_visible": 1,
    "ksong_visible": 1,
    "box_visible": 1,
    "nft_visible": 1,
    "musical_visible": 1,
    "live_visible": 0,
    "timbre_visible": 1,
    "assets_visible": 1,
    "online_visible": 1,
    "lting_visible": 1,
    "listenmusic_visible": 1,
    "likemusic_visible": 1,
    "kuelf_visible": 1,
    "share_visible": 1,
    "musicstation_visible": 1,
    "yaicreation_visible": 1,
    "ylikestory_visible": 1,
    "ychannel_visible": 1,
    "ypublishstory_visible": 1,
    "myplayer_visible": 0,
    "usermedal_visible": 1,
    "singletrack_visible": 1,
    "faxingka_visible": 1,
    "ai_song_visible": 1,
    "mcard_visible": 1,
    "hvisitors": 100,
    "nvisitors": 0,
    "rtime": 1500000000,
    "hobby": "",
    "actor_status": 0,
    "remark": "",
    "duration": 100000,
    "svip_level": 0,
    "svip_score": 0,
    "visible": 1,
    "k_star": 0,
    "singvip_valid": 0
  },
  "error_code": 0,
  "status": 1
}
```

***

### 1.2 获取用户关注列表

**接口地址**: `/user/follow`  
**请求方法**: `GET`  
**接口描述**: 获取用户关注的歌手列表

**请求参数**: 无

**响应示例**:

```json
{
  "data": {
    "total": 2,
    "list_ver": 68,
    "lists": [
      {
        "state": 0,
        "source": 7,
        "jumptype": 1,
        "status": 0,
        "userid": 0,
        "iden_type": 1,
        "nickname": "测试歌手",
        "singerid": 1001,
        "addtime": 1609459200,
        "pic": "http://example.com/singer1.jpg",
        "identity": 1135,
        "source_desc": "歌手",
        "score": 0
      }
    ]
  },
  "status": 1,
  "error_code": 0
}
```

***

### 1.3 获取用户听歌记录

**接口地址**: `/user/listen`  
**请求方法**: `GET`  
**接口描述**: 获取用户听歌历史记录

**请求参数**:

| 参数名 | 类型    | 必填 | 说明               |
| ------ | ------- | ---- | ------------------ |
| type   | integer | 否   | 类型（1-最近播放） |

**响应示例**:

```json
{
  "data": {
    "data_date": 0,
    "servertime": 1609459200,
    "list": [
      {
        "hash": "ABCD1234567890EFGH1234567890xxxx",
        "imgsize": [480, 400, 240, 150, 135, 120, 110, 100, 93, 64],
        "image_320": "http://example.com/stdmusic/{size}/20210101/example.jpg",
        "bitrate": 128,
        "filesize_320": 11413610,
        "filesize_128": 4565558,
        "hash_320": "EFGH1234567890IJKL1234567890EFGH",
        "privilege_sq": 10,
        "fail_process": 4,
        "pay_type": 3,
        "imgsize_sq": [480, 400, 240, 150, 135, 120, 110, 100, 93, 64],
        "image": "http://example.com/stdmusic/{size}/20210101/example.jpg",
        "duration": 285309,
        "type": "audio",
        "filesize_sq": 29345290,
        "hash_sq": "IJKL1234567890MNOP1234567890IJKL",
        "id": 12345678,
        "old_cpy": 0,
        "singername": "示例歌手",
        "privilege_320": 10,
        "privilege_128": 10,
        "image_sq": "http://example.com/stdmusic/{size}/20210101/example.jpg",
        "trans_param": {
          "ogg_128_hash": "MNOP1234567890QRST1234567890xxxx",
          "classmap": {"attr0": 234881111},
          "language": "国语",
          "cpy_attr0": 58731111,
          "musicpack_advance": 1,
          "display": 0,
          "display_rate": 0,
          "union_cover": "http://example.com/stdmusic/{size}/20210101/example.jpg",
          "ogg_320_filesize": 11090190,
          "qualitymap": {"bits": "1b4007ffff73fc035", "attr0": 2000661111, "attr1": 1744891111},
          "ogg_320_hash": "QRST1234567890UVWX12345678901111",
          "ogg_128_filesize": 3256111,
          "cid": 12345678,
          "cpy_grade": 5,
          "appid_block": "1234",
          "ipmap": {"attr0": 17213421111},
          "hash_offset": {
            "clip_hash": "UVWX1234567890Y111134567890UVWX",
            "start_byte": 0,
            "end_ms": 60000,
            "end_byte": 960113,
            "file_type": 0,
            "start_ms": 0,
            "offset_hash": "YZAB1234567890CDEF12345678901111"
          },
          "hash_multitrack": "CDEF1234567890GHIJ12345678901111",
          "pay_block_tpl": 1,
          "cpy_level": 1
        },
        "imgsize_320": [480, 400, 240, 150, 135, 120, 110, 100, 93, 64],
        "hash_128": "ABCD1234567890EFGH1234567890ABCD",
        "imgsize_128": [480, 400, 240, 150, 135, 120, 110, 100, 93, 64],
        "privilege": 10,
        "extname": "mp3",
        "filesize": 4561111,
        "album_audio_id": 12345678,
        "image_128": "http://example.com/stdmusic/{size}/20210101/example.jpg",
        "listen_count": 99,
        "name": "示例歌手 - 示例歌曲",
        "album_name": "示例专辑"
      }
    ],
    "hide_mixsongids": [],
    "listen_total": 1000,
    "total_history": 1000,
    "total_week": 0
  },
  "status": 1,
  "error_code": 0
}
```

***

### 1.4 获取用户歌单

**接口地址**: `/user/playlist`  
**请求方法**: `GET`  
**接口描述**: 获取用户创建和收藏的歌单列表

**请求参数**:

| 参数名   | 类型    | 必填 | 说明                |
| -------- | ------- | ---- | ------------------- |
| pagesize | integer | 否   | 每页数量（默认500） |

**响应示例**:

```json
{
  "data": {
    "info": [
      {
        "tags": "",
        "status": 1,
        "create_user_pic": "http://example.com/kugouicon/165/xxxxxxxx/xxxxxxxxxxxxxxxxxxxxxx.jpg",
        "per_num": 0,
        "pub_new": 0,
        "is_drop": 0,
        "list_create_userid": 123456789,
        "is_publish": 1,
        "musiclib_tags": [],
        "pub_time": 0,
        "pub_type": 0,
        "incr_sync": 1,
        "list_ver": 118,
        "intro": "",
        "type": 0,
        "list_create_listid": 1,
        "radio_id": 0,
        "source": 1,
        "is_del": 0,
        "is_mine": 0,
        "per_count": 0,
        "m_count": 0,
        "create_time": 1467981111,
        "kq_talent": 0,
        "is_edit": 1,
        "update_time": 1700000111,
        "is_def": 1,
        "sound_quality": "",
        "sort": 2,
        "trans_param": {"iden": 0},
        "list_create_gid": "collection_3_123456789_1_0",
        "global_collection_id": "collection_3_123456789_1_0",
        "is_per": 0,
        "is_pri": 0,
        "pic": "",
        "list_create_username": "示例用户",
        "is_featured": 0,
        "is_custom_pic": 0,
        "listid": 1,
        "name": "默认收藏",
        "count": 0
      }
    ],
    "phone_flag": 0,
    "total_ver": 1702,
    "userid": 123456789,
    "album_count": 0,
    "list_count": 1,
    "collect_count": 0
  },
  "status": 1,
  "error_code": 0
}
```

***

### 1.5 获取用户VIP信息

**接口地址**: `/user/vip/detail`  
**请求方法**: `GET`  
**接口描述**: 获取用户VIP会员信息

**请求参数**: 无

**响应示例**:

```json
{
  "data": {
    "is_vip": 0,
    "roam_type": 0,
    "m_reset_time": "",
    "m_y_endtime": "",
    "vip_clearday": "",
    "vip_type": 0,
    "vip_begin_time": "2020-12-24 20:12:33",
    "roam_begin_time": "",
    "vip_end_time": "2021-04-03 20:12:33",
    "userid": 123456789,
    "vip_y_endtime": "",
    "m_clearday": "",
    "svip_level": 0,
    "svip_score": 0,
    "su_vip_clearday": "",
    "su_vip_end_time": "",
    "su_vip_y_endtime": "",
    "su_vip_begin_time": "",
    "busi_vip": [
      {
        "is_vip": 1,
        "purchased_ios_type": 0,
        "purchased_type": 0,
        "is_paid_vip": 0,
        "vip_clearday": "2025-12-17 17:17:42",
        "latest_product_id": "",
        "product_type": "svip",
        "vip_begin_time": "2025-12-17 17:17:42",
        "y_type": 0,
        "vip_end_time": "2025-12-18 11:17:42",
        "userid": 123456789,
        "vip_limit_quota": {
          "total": 500
        },
        "paid_vip_expire_time": "",
        "busi_type": "concept"
      }
    ],
    "m_begin_time": "2020-12-24 20:12:33",
    "user_y_type": 0,
    "user_type": 0,
    "y_type": 0,
    "m_end_time": "2021-04-03 20:12:33",
    "roam_end_time": "",
    "m_is_old": 0,
    "m_type": 0
  },
  "status": 1,
  "error_code": 0
}
```

***

## 音乐库模块

### 2.1 获取歌单标签

**接口地址**: `/playlist/tags`  
**请求方法**: `GET`  
**接口描述**: 获取歌单分类标签列表

**请求参数**: 无

**响应示例**:

```json
{
    "data": [
        {
            "parent_id": "0",
            "sort": "1",
            "tag_id": "5",
            "tag_name": "场景",
            "son": [
                {
                    "parent_id": "5",
                    "tag_id": "587",
                    "tag_name": "学习",
                    "sort": "1"
                }
            ]
        }
    ],
    "status": 1,
    "error_code": 0
}
```

***

### 2.2 获取歌单所有歌曲

**接口地址**: `/playlist/track/all`  
**请求方法**: `GET`  
**接口描述**: 获取指定歌单的所有歌曲列表

**请求参数**:

| 参数名   | 类型    | 必填 | 说明               |
| -------- | ------- | ---- | ------------------ |
| id       | integer | 是   | 歌单ID             |
| page     | integer | 否   | 页码（默认1）      |
| pagesize | integer | 否   | 每页数量（默认50） |

**响应示例**:

```json
{
    "data": {
        "begin_idx": 0,
        "pagesize": 250,
        "count": 78,
        "popularization": {},
        "userid": 12345566,
        "songs": [
            {
                "hash": "ABCD1234567890EFGH1234567890ABCD",
                "brief": "",
                "audio_id": 123456789,
                "mvtype": 0,
                "size": 3466751,
                "publish_date": "2024-09-05",
                "name": "示例歌手 - 示例歌曲",
                "mvtrack": 0,
                "bpm_type": "2",
                "add_mixsongid": 670581111,
                "album_id": "103332458",
                "bpm": 81,
                "mvhash": "",
                "extname": "mp3",
                "language": "纯音乐",
                "collecttime": 1744951111,
                "csong": 0,
                "remark": "示例备注",
                "level": 1,
                "tagmap": {
                    "genre0": 4191111
                },
                "media_old_cpy": 0,
                "relate_goods": [
                    {
                        "size": 3461111,
                        "hash": "EFGH1234567890IJKL1231111190EFGH",
                        "level": 2,
                        "privilege": 8,
                        "bitrate": 128
                    }
                ],
                "download": [
                    {
                        "status": 0,
                        "hash": "IJKL1234567890MNOP12345671111JKL",
                        "fail_process": 4,
                        "pay_type": 3
                    }
                ],
                "rcflag": 0,
                "feetype": 0,
                "has_obbligato": 1,
                "timelen": 216633,
                "sort": 0,
                "trans_param": {
                    "cpy_grade": 20,
                    "union_cover": "http://example.com/stdmusic/{size}/20240905/example.jpg",
                    "free_for_ad": 32,
                    "language": "纯音乐",
                    "cpy_attr0": 8192,
                    "musicpack_advance": 0,
                    "display": 0,
                    "display_rate": 0,
                    "qualitymap": {
                        "bits": "3400000011008034",
                        "attr0": 285245492,
                        "attr1": 1744830464
                    },
                    "ogg_320_filesize": 7545542,
                    "cid": 421335767,
                    "ogg_128_hash": "MNOP1234567890QRST123456711111OP",
                    "ogg_128_filesize": 2307720,
                    "ogg_320_hash": "OPQR1234567890STUV12311111890OPQR",
                    "ipmap": {
                        "attr0": 536881111
                    },
                    "classmap": {
                        "attr0": 234881111
                    },
                    "pay_block_tpl": 1,
                    "cpy_level": 1
                },
                "medistype": "audio",
                "user_id": 0,
                "albuminfo": {
                    "name": "示例专辑",
                    "id": 103332458,
                    "publish": 1
                },
                "bitrate": 128,
                "audio_group_id": "447041111",
                "privilege": 8,
                "cover": "http://example.com/stdmusic/{size}/20240905/example.jpg",
                "mixsongid": 670582161,
                "fileid": 321,
                "heat": 0,
                "singerinfo": [
                    {
                        "id": 13717067,
                        "publish": 1,
                        "name": "示例歌手",
                        "avatar": "http://example.com/avatar/{size}/20240901/example.jpg",
                        "type": 0
                    }
                ]
            }
        ],
        "list_info": {
            "abtags": [],
            "tags": "学习,工作,治愈",
            "status": 1,
            "create_user_pic": "http://example.com/kugouicon/165/20240905/example.jpg",
            "is_pri": 0,
            "pub_new": 1,
            "is_drop": 0,
            "list_create_userid": 844151111,
            "is_publish": 1,
            "musiclib_tags": [
                {
                    "tag_id": 587,
                    "parent_id": 5,
                    "tag_name": "学习"
                }
            ],
            "pub_type": 2,
            "is_featured": 1,
            "publish_date": "2021-06-04",
            "collect_total": 0,
            "specialid": 3867688,
            "list_ver": 99,
            "intro": "当生活节奏过快，按下暂停键，听这些治愈旋律流淌。在这方音乐天地里，彻底放空，寻回内心的宁静与自在。",
            "type": 0,
            "list_create_listid": 163,
            "radio_id": 0,
            "source": 1,
            "trans_param": {
                "iden": 0
            },
            "code": 1,
            "is_def": 0,
            "parent_global_collection_id": "collection_3_84411111_163_0",
            "sound_quality": "",
            "per_count": 0,
            "plist": [],
            "create_time": 1622801111,
            "is_per": 0,
            "is_edit": 1,
            "update_time": 1766305156,
            "per_num": 0,
            "count": 78,
            "sort": 421,
            "is_mine": 0,
            "listid": 163,
            "musiclib_id": 0,
            "kq_talent": 1,
            "create_user_gender": 0,
            "pic": "http://example.com/custom/{size}/example.png",
            "list_create_username": "示例用户",
            "name": "示例歌单名称",
            "is_custom_pic": 1,
            "global_collection_id": "collection_3_8441111137_163_0",
            "heat": 0,
            "list_create_gid": "collection_3_844111137_163_0"
        }
    },
    "status": 1,
    "error_code": 0
}
```

***

### 2.3 获取排行榜列表

**接口地址**: `/rank/list`  
**请求方法**: `GET`  
**接口描述**: 获取所有排行榜分类和列表

**请求参数**: 无

**响应示例**:

```json
{
    "data": {
        "timestamp": 1766393226,
        "total": 21,
        "show_line": 2,
        "theme": {
            "classify_list": [],
            "bg_image": "",
            "font": {
                "nt": "",
                "st": "",
                "line": "",
                "bold_line": ""
            }
        },
        "info": [
            {
                "children": [],
                "base_img": "",
                "rankname": "TOP500",
                "new_cycle": 1800,
                "banner_9": "http://example.com/mcommon/{size}/20220112/example_banner.png",
                "album_img_9": "http://example.com/stdmusic/{size}/20250319/example_album.jpg",
                "table_plaque": "http://example.com/mcommon/{size}/20240311/example_plaque.png",
                "update_frequency_type": 1,
                "play_times": 15110206,
                "img_9": "http://example.com/mcommon/{size}/20241219/example_img.png",
                "is_city_rank": 0,
                "classify": 1,
                "haschildren": 0,
                "songinfo": [
                    {
                        "album_audio_id": 131021111,
                        "trans_param": {
                            "pay_block_tpl": 1,
                            "union_cover": "http://example.com/stdmusic/{size}/20250319/20250319232204311111.jpg",
                            "language": "国语",
                            "cpy_attr0": 58720256,
                            "musicpack_advance": 0,
                            "display": 0,
                            "display_rate": 0,
                            "qualitymap": {
                                "bits": "1b410807fbf311111",
                                "attr0": 1061142645,
                                "attr1": 1746993407
                            },
                            "cpy_level": 1,
                            "cpy_grade": 25,
                            "cid": 533002703,
                            "ogg_128_filesize": 2743882,
                            "classmap": {
                                "attr0": 234881032
                            },
                            "hash_multitrack": "7C5F5D5B1464C23408187EDC4B111116",
                            "ipmap": {
                                "attr0": 1107300352
                            },
                            "ogg_320_hash": "28E0A2C1853E4AE71E443677BF1111155",
                            "ogg_128_hash": "B50EAF1A1C69CB9C6414DC8BFE01111A",
                            "ogg_320_filesize": 10058267
                        },
                        "name": "落空",
                        "author": "印子月",
                        "songname": "印子月 - 落空"
                    }
                ],
                "rank_cid": 103108,
                "share_bg": "http://example.com/mcommon/{size}/20240505/20240505213952649412.png",
                "id": 2,
                "jump_url": "",
                "album_cover_color": "#64493d",
                "share_logo": "http://example.com/mcommon/{size}/20240311/20240311161212168226.png",
                "bannerurl": "http://example.com/mcommonbanner/{size}/20181019/20181019122517263545.jpg",
                "zone": "tx6_gz_kmr",
                "show_play_count": 1,
                "isvol": 1,
                "rank_id_publish_date": "2025-12-22 08:30:01",
                "issue": 356,
                "img_cover": "http://example.com/mcommon/{size}/20241219/20241219164226731176.png",
                "custom_type": 0,
                "intro": "数据来源：全曲库歌曲\r\n排序方式：按歌曲喜爱用户数的总量排序\r\n更新频率：每天",
                "rankid": 8888,
                "update_frequency": "每天",
                "banner7url": "http://example.com/mcommon/{size}/20241219/20241219164215715999.png",
                "show_play_button": 0,
                "video_ending": "http://example.com/mcommon/{size}/20240313/20240313142811662716.jpg",
                "jump_title": "",
                "is_timing": 1,
                "count_down": 1800,
                "extra": {
                    "resp": {
                        "scheduled_release_conf": {
                            "scheduled_release_time": "10:00:00",
                            "latest_rank_cid": 103108,
                            "latest_rank_cid_publish_date": "2025-12-22 08:30:01"
                        },
                        "five_year_total": 242,
                        "new_total": 10,
                        "enjoy_total": 0,
                        "recent_year_total": 187,
                        "follow_total": 0,
                        "all_total": 500,
                        "vip_total": 0,
                        "rank_tag": [
                            {
                                "desc": "有10首上新",
                                "type": 3
                            }
                        ]
                    }
                },
                "ranktype": 2,
                "imgurl": "http://example.com/mcommon/{size}/20241219/20241219164209671111.png"
            }
        ]
    },
    "errcode": 0,
    "status": 1,
    "error": ""
}
```

***

### 2.4 获取音频排行榜

**接口地址**: `/rank/audio`  
**请求方法**: `GET`  
**接口描述**: 获取指定排行榜的歌曲列表

**请求参数**:

| 参数名   | 类型    | 必填 | 说明               |
| -------- | ------- | ---- | ------------------ |
| rankid   | integer | 是   | 排行榜ID           |
| page     | integer | 否   | 页码（默认1）      |
| pagesize | integer | 否   | 每页数量（默认50） |

**响应示例**:

```json
{
    "total": 100,
    "error_code": 0,
    "data": {
        "total": 100,
        "songlist": [
            {
                "mvdata": [],
                "authors": [
                    {
                        "sizable_avatar": "http://example.com/uploadpic/softhead/{size}/20230101/example_avatar.jpg",
                        "is_publish": 1,
                        "author_id": 12345,
                        "author_name": "示例歌手"
                    }
                ],
                "author_name": "示例歌手",
                "copyright": {
                    "old_hide": 1
                },
                "audio_id": 123456789,
                "video_info": {
                    "video_id": 0,
                    "video_track": 0,
                    "video_timelength": 0,
                    "video_filesize": 0,
                    "video_hash": ""
                },
                "musical": {},
                "remarks": [],
                "trans_param": {
                    "ogg_128_hash": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                    "classmap": {
                        "attr0": 234881032
                    },
                    "language": "国语",
                    "cpy_attr0": 33569920,
                    "musicpack_advance": 1,
                    "ogg_128_filesize": 2818270,
                    "display_rate": 0,
                    "qualitymap": {
                        "bits": "xxxxxxxxxxxxxxxxx",
                        "attr0": 1058308212,
                        "attr1": 134414336
                    },
                    "union_cover": "http://example.com/stdmusic/{size}/20230101/example_union_cover.jpg",
                    "ogg_320_filesize": 8992851,
                    "cid": 123456789,
                    "pay_block_tpl": 1,
                    "display": 0,
                    "ogg_320_hash": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                    "hash_offset": {
                        "clip_hash": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                        "start_byte": 0,
                        "end_ms": 60000,
                        "end_byte": 960129,
                        "file_type": 0,
                        "start_ms": 0,
                        "offset_hash": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
                    },
                    "ipmap": {
                        "attr0": 262144
                    },
                    "cpy_grade": 5,
                    "cpy_level": 1
                },
                "is_publish": 1,
                "user_download": {
                    "status_320": 0,
                    "status_128": 0,
                    "status_flac": 0,
                    "status_high": 0,
                    "status_super": 1
                },
                "songname": "示例歌曲",
                "has_obbligato": 1,
                "privilege_download": {
                    "fail_process_128": 4,
                    "fail_process_320": 4,
                    "fail_process_high": 4,
                    "privilege_flac": 10,
                    "fail_process": 4,
                    "fail_process_super": 0,
                    "fail_process_flac": 4,
                    "privilege": 10,
                    "privilege_high": 10,
                    "privilege_320": 10,
                    "privilege_super": 0,
                    "privilege_128": 10
                },
                "zone": "example_zone",
                "album_audio_id": 123456789,
                "audio_info": {
                    "filesize_flac": 23180053,
                    "bitrate": 128,
                    "duration_flac": 260000,
                    "duration_128": 260000,
                    "duration_320": 260000,
                    "hash_320": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                    "bitrate_high": 1497,
                    "duration_high": 260000,
                    "hash_flac": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                    "filesize_128": 4169348,
                    "filesize_super": 0,
                    "hash_128": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                    "extname_super": "",
                    "bitrate_super": 0,
                    "bitrate_flac": 711,
                    "duration_super": 0,
                    "hash_super": "",
                    "hash_high": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                    "extname": "mp3",
                    "filesize_320": 10423063,
                    "filesize_high": 48785309
                },
                "goods_info": {
                    "album_sale_url": ""
                },
                "album_id": 123456789,
                "album_info": {
                    "sizable_cover": "http://example.com/stdmusic/{size}/20230101/example_album_cover.jpg",
                    "album_name": "示例专辑"
                },
                "trans_obj": {
                    "rank_show_sort": 1
                },
                "business": {
                    "last_sort": 0,
                    "buy_count": "0",
                    "level": 2,
                    "original_index": 1,
                    "rank_count": 0,
                    "rank_id_publish_date": "2023-01-01 00:00:00",
                    "extern": "",
                    "filename": "示例歌手 - 示例歌曲",
                    "sort": 1,
                    "max_sort": 0,
                    "exclusive": 0,
                    "issue": "100",
                    "recommend_reason": "",
                    "album_audio_remark": "",
                    "is_recent_year": 1,
                    "offset": 0,
                    "addtime": "2023-01-01 00:00:00",
                    "rank_id": "123456",
                    "parent_id": "12345",
                    "last_original_index": 0
                },
                "deprecated": {
                    "hash": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                    "pkg_price_high": 1,
                    "status": 0,
                    "bitrate": 128,
                    "price_128": 200,
                    "type_320": "audio",
                    "pay_type_320": 3,
                    "pay_type_128": 3,
                    "cd_url": "",
                    "pay_type_flac": 3,
                    "price_flac": 200,
                    "type": "audio",
                    "id": 123456789,
                    "type_super": "",
                    "old_cpy": 0,
                    "type_high": "audio",
                    "pkg_price_super": 0,
                    "type_128": "audio",
                    "pkg_price_320": 1,
                    "topic_remark": "",
                    "pay_type_high": 3,
                    "price": 200,
                    "price_320": 200,
                    "price_super": 0,
                    "price_high": 200,
                    "duration": 260000,
                    "type_flac": "audio",
                    "cid": 123456789,
                    "pkg_price": 1,
                    "filesize": 4169348,
                    "extname": "mp3",
                    "pkg_price_flac": 1,
                    "pay_type": 3,
                    "pay_type_super": 0,
                    "pkg_price_128": 1
                },
                "rank_cid": 123456
            }
        ]
    },
    "extra": {
        "resp": {
            "scheduled_release_conf": {
                "scheduled_release_time": "00:00:00",
                "latest_rank_cid": 123456,
                "latest_rank_cid_publish_date": "2023-01-01 00:00:00"
            },
            "five_year_total": 50,
            "new_total": 5,
            "enjoy_total": 0,
            "recent_year_total": 50,
            "follow_total": 0,
            "all_total": 100,
            "vip_total": 0,
            "rank_tag": [
                {
                    "desc": "示例标签描述",
                    "type": 3
                }
            ]
        }
    },
    "status": 1,
    "errmsg": ""
}
```

***

### 2.5 获取热门歌单

**接口地址**: `/top/playlist`  
**请求方法**: `GET`  
**接口描述**: 获取热门歌单列表

**请求参数**:

| 参数名      | 类型    | 必填 | 说明            |
| ----------- | ------- | ---- | --------------- |
| category_id | integer | 否   | 分类ID（默认0） |

**响应示例**:

```json
{
  "data" : {
    "has_next" : 1,
    "bi_biz" : "rcmd_spr",
    "session" : "1766394276_0",
    "alg_id" : 2,
    "special_list" : [ {
      "sync" : 0,
      "publishtime" : "2025-02-05 00:00:00",
      "specialid" : 8302578,
      "percount" : 0,
      "list_info_trans_param" : {
        "special_tag" : 0,
        "iden" : 8,
        "trans_flag" : 1
      },
      "bz_status" : 0,
      "singername" : "群星",
      "from" : 0,
      "alg_path" : "recall:scid_source",
      "tags" : [ {
        "tag_name" : "经典",
        "tag_id" : 12
      } ],
      "ugc_talent_review" : 0,
      "type" : 15,
      "slid" : 258,
      "flexible_cover" : "http://example.com/custom/{size}/20250205/20250205152212151111.jpg",
      "nickname" : "ShinoYuichi",
      "show" : "",
      "collectType" : 0,
      "collectcount" : 5959,
      "trans_param" : {
        "special_tag" : 0
      },
      "report_info" : "s=15,d=scid推荐,t=1766394276,z=0.100,c=23187380,a=recall*scid_source,h=0.33289045,p=0.499335675",
      "specialname" : "2025版全时代百强动漫曲目",
      "imgurl" : "http://example.com/custom/{size}/20250205/20250205152212151111.jpg",
      "play_count" : 1033673,
      "pic" : "http://example.com/kugouicon/165/20200731/20200731234154226414.jpg",
      "from_hash" : "",
      "from_tag" : 0,
      "global_collection_id" : "collection_3_987580976_258_0",
      "intro" : "2025年ACG年度百强歌单，冬之花属于整活插入，搬运曲目纯分享用【不合作🤗】（违权侵删）\n究竟入围了哪些熟悉的歌曲呢？\n首先介绍年度霸权《葬送的芙莉莲》系列的《勇者》🐮🍺\n其次是去年的顶流《我推的孩子》系列的《Idol》\n创作了以上两首神曲的【YOASOBI】乐队还入围了《动物狂想曲》系列的《怪物》。\n来自动画电影《烟花》的《打上花火》依然高居不下，为其作词的【米津玄师】在《链锯人》的《Kick Back》MV里又被宫本浩次创飞了...\n【泽野弘之】在《进击的巨人》系列入围了5首由他作词作曲的歌曲（巨人入围9首他占4首，call your name一首歌的男女声版都入围了），包括【巨人】在内，这位大佬入围了11首分别是：aLIEz、Call of silence、ninelie、βίος、Barricades、Call your name（Gemie version）、Call your name、Avid、gravityWall、Perfect Time、Zero Eclipse，实在是太有实力了！😯\n希望大家能够喜欢，感谢大家收听和支持！",
      "suid" : 987580976
    } ],
    "OlexpIds" : "92,94,836,2106,2118,2122,2136,2860,2862,3088,3090,3380,3382,3386,3388,3554,3556,4270,4272,4383,4461,5661,5663,5665,5667,5669,5671,5825,5827,5829,5831,5851,5853,5855,5857,5859,5861,5863,5865,5887,5889,5891,5895,5897,6053,6055,6153,6155,6315,6317,7489,7491,7495,7497,7561,7563,7627,7629,7665,7667,8715,8717,10639,10641,11677",
    "show_time" : 3600,
    "all_client_playlist_flag" : 0,
    "refresh_time" : 3600
  },
  "status" : 1,
  "error_code" : 0
}
```

***

## 搜索模块

### 3.1 搜索音乐

**接口地址**: `/search`  
**请求方法**: `GET`  
**接口描述**: 搜索歌曲、歌手、专辑、歌单

**请求参数**:

| 参数名   | 类型    | 必填 | 说明                                                         |
| -------- | ------- | ---- | ------------------------------------------------------------ |
| keywords | string  | 是   | 搜索关键词                                                   |
| page     | integer | 否   | 页码（默认1）                                                |
| pagesize | integer | 否   | 每页数量（默认30）                                           |
| type     | string  | 否   | 搜索类型（song-歌曲，author-歌手，album-专辑，special-歌单） |

**响应示例**:

```json
{
  "error_msg" : "",
  "data" : {
    "correctiontip" : "",
    "pagesize" : 30,
    "page" : 1,
    "correctiontype" : 0,
    "correctionrelate" : "",
    "total" : 480,
    "lists" : [ {
      "PublishTime" : "",
      "Audioid" : 194543380,
      "OldCpy" : 1,
      "PublishAge" : 255,
      "bitflag" : 1,
      "PayType" : 0,
      "SongAccNode" : {
        "round" : 1,
        "query" : "1",
        "rewrite_type" : 0,
        "source" : 0,
        "recall_type" : 0,
        "match_level" : 0,
        "recall_intent" : 0
      },
      "Accompany" : 1,
      "SingerName" : "MOB CHOIR",
      "ShowingFlag" : 0,
      "Source" : "",
      "AlbumAux" : "",
      "Image" : "http://example.com/stdmusic/{size}/20221010/20221010110110659335.jpg",
      "SQ" : {
        "FileSize" : 31058290,
        "Hash" : "C1D20399088664C7FCEFE965D911111D",
        "Privilege" : 0
      },
      "HQ" : {
        "FileSize" : 9187553,
        "Hash" : "B2E39F73BE7927AFC2A8537DF8111177",
        "Privilege" : 0
      },
      "M4aSize" : 0,
      "HeatLevel" : 2,
      "trans_param" : {
        "union_cover" : "http://example.com/stdmusic/{size}/20221010/20221010110110659335.jpg",
        "ogg_320_filesize" : 9633406,
        "ogg_320_hash" : "69ED8AC871D22DD1167E3F882C21B6A4",
        "ogg_128_filesize" : 2662542,
        "pay_block_tpl" : 1,
        "classmap" : {
          "attr0" : 234881032
        },
        "ogg_128_hash" : "C70B5B06B8DA5E37A65B1A606E22566B",
        "ipmap" : {
          "attr0" : 2148544516
        },
        "cid" : -1,
        "language" : "日语",
        "cpy_attr0" : 50331648,
        "hash_multitrack" : "0E4A1ABBB315B0E220D528976F760113",
        "qualitymap" : {
          "bits" : "1fc0080003f3fc075",
          "attr0" : 1061142645,
          "attr1" : 2013331456
        },
        "musicpack_advance" : 0,
        "display" : 0,
        "display_rate" : 0
      },
      "UploaderContent" : "",
      "FileSize" : 3674925,
      "IsOriginal" : 1,
      "FileHash" : "E82B6D486E90E101705967CD6C72B9A5",
      "FoldType" : 0,
      "Grp" : [ ],
      "isPrepublish" : 0,
      "Type" : "audio",
      "Bitrate" : 128,
      "ExtName" : "mp3",
      "TopID" : 0,
      "AlbumPrivilege" : 0,
      "AlbumID" : "61962526",
      "AlbumName" : "1",
      "mvdata" : [ {
        "typ" : 0,
        "trk" : "3",
        "hash" : "550BCAAEC66D549DA50ECA89B952A422",
        "id" : "9485091"
      } ],
      "OtherName" : "",
      "Res" : {
        "FileSize" : 132944100,
        "Privilege" : 0,
        "Hash" : "0AEFB2396048A87F6E9D4FF4309DAD50",
        "BitRate" : 4615,
        "TimeLength" : 229
      },
      "SourceID" : 0,
      "MixSongID" : "448118750",
      "FailProcess" : 0,
      "Suffix" : "",
      "MatchFlag" : 8,
      "Scid" : 194543380,
      "Singers" : [ {
        "name" : "MOB CHOIR",
        "ip_id" : 0,
        "id" : 560646
      } ],
      "Auxiliary" : "《モブサイコ100 III》动画OP",
      "RankId" : 0,
      "PublishDate" : "2022-10-05",
      "TagDetails" : [ ],
      "TagContent" : "",
      "PrepublishInfo" : {
        "ReserveCount" : 0,
        "DisplayTime" : "",
        "Id" : 0,
        "PublishTime" : ""
      },
      "OwnerCount" : 4258,
      "Uploader" : "",
      "Duration" : 229,
      "OriSongName" : "1",
      "FileName" : "MOB CHOIR - 1",
      "recommend_type" : 0
    } ],
    "size" : 30,
    "allowerr" : 0,
    "AlgPath" : "",
    "sec_aggre_v2" : [ ],
    "correctionforce" : 0,
    "istag" : 0,
    "from" : 0,
    "istagresult" : 0,
    "isshareresult" : 0
  },
  "status" : 1,
  "error_code" : 0
}
```

***

### 3.2 搜索歌词

**接口地址**: `/search/lyric`  
**请求方法**: `GET`  
**接口描述**: 根据歌词片段搜索歌曲

**请求参数**:

| 参数名 | 类型   | 必填 | 说明       |
| ------ | ------ | ---- | ---------- |
| hash   | string | 是   | 歌曲Hash值 |

**响应示例**:

```json
{
  "status" : 200,
  "info" : "OK",
  "errcode" : 200,
  "errmsg" : "OK",
  "keyword" : "",
  "proposal" : "136652935",
  "has_complete_right" : 0,
  "companys" : "",
  "ugc" : 0,
  "ugccount" : 0,
  "expire" : 7200,
  "candidates" : [ {
    "id" : "136652935",
    "product_from" : "官方推荐歌词",
    "accesskey" : "CE21D768BF1CA90109BCA5EA25EC1111",
    "can_score" : false,
    "singer" : "周杰伦",
    "song" : "兰亭序",
    "duration" : 254107,
    "uid" : "486953864",
    "nickname" : "热心用户",
    "origiuid" : "0",
    "transuid" : "0",
    "sounduid" : "0",
    "originame" : "",
    "transname" : "",
    "soundname" : "",
    "parinfo" : [ ],
    "parinfoExt" : [ ],
    "language" : "",
    "krctype" : 1,
    "hitlayer" : 1,
    "hitcasemask" : 2,
    "adjust" : 0,
    "score" : 60,
    "contenttype" : 0,
    "content_format" : 1,
    "download_id" : "136652935"
  } ],
  "ugccandidates" : [ ],
  "artists" : [ {
    "identity" : 1,
    "base" : {
      "author_id" : 3520,
      "author_name" : "周杰伦",
      "is_publish" : 1,
      "avatar" : "http://example.com/uploadpic/softhead/{size}/20241112/20241112153406328180.jpg",
      "identity" : 1135,
      "type" : 1,
      "country" : "中国",
      "birthday" : "1979-01-18",
      "language" : "华语"
    }
  } ],
  "ai_candidates" : [ ]
}
```

***

## 播放模块

### 4.1 获取歌曲播放链接

**接口地址**: `/song/url`  
**请求方法**: `GET`  
**接口描述**: 获取歌曲的播放URL

**请求参数**:

| 参数名   | 类型    | 必填 | 说明                |
| -------- | ------- | ---- | ------------------- |
| hash     | string  | 是   | 歌曲Hash值          |
| album_id | integer | 是   | 专辑ID              |
| quality  | string  | 否   | 音质（128/320/999） |

**响应示例**:

```json
{
  "extName" : "flac",
  "classmap" : {
    "attr0" : 234881032
  },
  "status" : 1,
  "volume" : -10.2,
  "std_hash_time" : 253988,
  "backupUrl" : [ "http://example.com/202512221813/385b215ec64b338510f4249f03fb1f22/v3/e898ef97752a72e4f5b69fbd87b0b939/yp/full/ap3116_us892072716_mi336d5ebc5436534e61d16e63ddfca327_pi411_mx0_quhigh_ct430300_s3674903678.flac" ],
  "url" : [ "http://example.com/202512221813/7a58e2714a8220fab423dc0d7334e0b3/v3/e898ef97752a72e4f5b69fbd87b0b939/yp/full/ap3116_us892072716_mi336d5ebc5436534e61d16e63ddfca327_pi411_mx0_quhigh_ct430300_s3674903678.flac" ],
  "std_hash" : "4EC10B1B7B9BEB2F0ADB89BBEC11114B",
  "tracker_through" : {
    "identity_block" : 0,
    "cpy_grade" : 5,
    "musicpack_advance" : 1,
    "all_quality_free" : 0,
    "cpy_level" : 1
  },
  "trans_param" : {
    "display_rate" : 0,
    "display" : 0,
    "ogg_128_hash" : "55FAF85E7B9D7A79160A4534611112D6",
    "qualitymap" : {
      "bits" : "1b401fff1111fc075",
      "attr0" : 2134884469,
      "attr1" : 1745092607
    },
    "pay_block_tpl" : 1,
    "union_cover" : "http://example.com/stdmusic/{size}/20241118/20241118161111508429.jpg",
    "language" : "国语",
    "hash_multitrack" : "427A4A64441BB1E1D875E11114CF2B49",
    "cpy_attr0" : 58727552,
    "ipmap" : {
      "attr0" : 2200197664768
    },
    "ogg_320_hash" : "F6D206DA2B28F8BAC357DD6701111227",
    "classmap" : {
      "attr0" : 234881032
    },
    "ogg_128_filesize" : 2845150,
    "ogg_320_filesize" : 9886068
  },
  "fileHead" : 0,
  "auth_through" : [ ],
  "timeLength" : 253,
  "bitRate" : 0,
  "priv_status" : 1,
  "volume_peak" : 0.3,
  "volume_gain" : 0,
  "q" : 0,
  "fileName" : "True",
  "fileSize" : 28893185,
  "hash" : "E898EF97752A72E4F5B11FBD87B0B939"
}
```

***

### 4.2 获取歌曲高潮部分

**接口地址**: `/song/climax`  
**请求方法**: `GET`  
**接口描述**: 获取歌曲的高潮片段时间点

**请求参数**:

| 参数名 | 类型   | 必填 | 说明       |
| ------ | ------ | ---- | ---------- |
| hash   | string | 是   | 歌曲Hash值 |

**响应示例**:

```json
{
    "status": 1,
    "error_code": 0,
    "errcode": 0,
    "errmsg": "",
    "data": [
        {
            "start_time": "11200",
            "end_time": "102110",
            "timelength": "26400",
            "author_name": "周伦",
            "hash": "F5423B0C66717101744ED11FA9B9222C",
            "audio_id": "161145",
            "audio_name": "兰亭"
        }
    ]
}
```

***

### 4.3 获取歌词

**接口地址**: `/lyric`  
**请求方法**: `GET`  
**接口描述**: 获取歌曲的歌词内容

**请求参数**:

| 参数名    | 类型    | 必填 | 说明                   |
| --------- | ------- | ---- | ---------------------- |
| id        | integer | 是   | 歌词ID                 |
| accesskey | string  | 是   | 访问密钥               |
| fmt       | string  | 否   | 格式（krc/lrc/txt）    |
| decode    | boolean | 否   | 是否解码（true/false） |

**响应示例**:

```json
{
    "status": 200,
    "info": "OK",
    "error_code": 0,
    "fmt": "krc",
    "contenttype": 0,
    "_source": "bss",
    "charset": "",
    "content": "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",
    "id": "136652935",
    "decodeContent": "﻿[id:$00000000]\r\n[ar:周伦]\r\n[ti:兰序]\r\n[by:]\r\n[hash:4ec10b1b7b9beb2f0adb1111ec5f664b]\r\n[al:魔杰座]\r\n[sign:]\r\n[qq:]\r\n[total:0]\r\n[offset:0]\r\n[language:eyJjb250ZW50IjpbXSwidmVyc2lvbiI6MX0=]\r\n[0,6510]<0,930,0>周<930,930,0>杰<1860,930,0>伦 <2790,930,0>- <3720,930,0>兰<4650,930,0>亭<5580,930,0>序\r\n[6510,6520]<0,1304,0>词<1304,1304,0>：<2608,1304,0>方<3912,1304,0>文<5216,1304,0>山\r\n[13030,6520]<0,1304,0>曲<1304,1304,0>：<2608,1304,0>周<3912,1304,0>杰<5216,1304,0>伦\r\n[19550,6510]<0,1085,0>编<1085,1085,0>曲<2170,1085,0>：<3255,1085,0>钟<4340,1085,0>兴<5425,1085,0>民\r\n[26071,6328]<0,296,0>兰<296,416,0>亭<712,432,0>临<1144,720,0>帖<1864,0,0> <1864,424,0>行<2288,488,0>书<2776,384,0>如<3160,360,0>行<3520,416,0>云<3936,384,0>流<4320,560,0>水\r\n[32399,6429]<0,416,0>月<416,376,0>下<792,368,0>门<1160,825,0>推<1985,0,0> <1985,343,0>心<2328,392,0>细<2720,369,0>如<3089,431,0>你<3520,392,0>脚<3912,440,0>步<4352,631,0>碎\r\n[38828,3680]<0,496,0>忙<496,464,0>不<960,712,0>迭<1672,0,0> <1672,337,0>千<2009,367,0>年<2376,392,0>碑<2768,456,0>易<3224,456,0>拓\r\n[42508,3544]<0,392,0>却<392,400,0>难<792,377,0>拓<1169,455,0>你<1624,384,0>的<2008,688,0>美\r\n[46052,5248]<0,424,0>真<424,480,0>迹<904,944,0>绝<1848,0,0> <1848,272,0>真<2120,352,0>心<2472,400,0>能<2872,584,0>给<3456,1120,0>谁\r\n[51300,6336]<0,424,0>牧<424,368,0>笛<792,376,0>横<1168,736,0>吹<1904,0,0> <1904,376,0>黄<2280,376,0>酒<2656,432,0>小<3088,376,0>菜<3464,376,0>又<3840,472,0>几<4312,2024,0>碟\r\n[57636,6360]<0,360,0>夕<360,384,0>阳<744,416,0>余<1160,872,0>晖<2032,0,0> <2032,304,0>如<2336,368,0>你<2704,392,0>的<3096,408,0>羞<3504,392,0>怯<3896,528,0>似<4424,1288,0>醉\r\n[63996,1968]<0,416,0>摹<416,336,0>本<752,392,0>易<1144,408,0>写\r\n[65964,5360]<0,376,0>而<376,320,0>墨<696,432,0>香<1128,328,0>不<1456,456,0>退<1912,544,0>与<2456,312,0>你<2768,328,0>同<3096,416,0>留<3512,504,0>余<4016,624,0>味\r\n[71324,5352]<0,496,0>一<496,336,0>行<832,456,0>朱<1288,392,0>砂<1680,0,0> <1680,369,0>到<2049,391,0>底<2440,432,0>圈<2872,408,0>了<3280,1688,0>谁\r\n[76676,6192]<0,352,0>无<352,368,0>关<720,392,0>风<1112,1040,0>月<2152,0,0> <2152,440,0>我<2592,416,0>题<3008,408,0>序<3416,440,0>等<3856,536,0>你<4392,1002,0>回\r\n[82868,6344]<0,424,0>悬<424,384,0>笔<808,392,0>一<1200,1136,0>绝<2336,0,0> <2336,385,0>那<2721,359,0>岸<3080,392,0>边<3472,440,0>浪<3912,400,0>千<4312,1688,0>叠\r\n[89212,6280]<0,416,0>情<416,384,0>字<800,448,0>何<1248,1144,0>解<2392,0,0> <2392,344,0>怎<2736,416,0>落<3152,440,0>笔<3592,432,0>都<4024,536,0>不<4560,1200,0>对\r\n[95492,7999]<0,384,0>而<384,416,0>我<800,400,0>独<1200,1168,0>缺<2368,0,0> <2368,376,0>你<2744,400,0>一<3144,440,0>生<3584,432,0>的<4016,385,0>了<4401,2351,0>解\r\n[103491,3119]<0,303,0>无<303,352,0>关<655,360,0>风<1015,224,0>月<1239,0,0> <1239,168,0>我<1407,216,0>题<1623,248,0>序<1871,248,0>等<2119,280,0>你<2399,424,0>回\r\n[106610,3152]<0,312,0>悬<312,336,0>笔<648,328,0>一<976,288,0>绝<1264,0,0> <1264,216,0>那<1480,176,0>岸<1656,256,0>边<1912,248,0>浪<2160,272,0>千<2432,480,0>叠\r\n[109762,3192]<0,248,0>情<248,216,0>字<464,304,0>何<768,488,0>解<1256,0,0> <1256,208,0>怎<1464,176,0>落<1640,249,0>笔<1889,207,0>都<2096,312,0>不<2408,520,0>对\r\n[112954,3019]<0,248,0>而<248,200,0>我<448,256,0>独<704,368,0>缺<1072,0,0> <1072,240,0>你<1312,176,0>一<1488,336,0>生<1824,200,0>的<2024,320,0>了<2344,536,0>解\r\n[115973,3231]<0,335,0>无<335,392,0>关<727,280,0>风<1007,264,0>月<1271,0,0> <1271,200,0>我<1471,248,0>题<1719,272,0>序<1991,224,0>等<2215,280,0>你<2495,464,0>回\r\n[119204,3248]<0,352,0>悬<352,360,0>笔<712,328,0>一<1040,369,0>绝<1409,0,0> <1409,191,0>那<1600,176,0>岸<1776,216,0>边<1992,208,0>浪<2200,280,0>千<2480,480,0>叠\r\n[122452,3097]<0,248,0>情<248,208,0>字<456,296,0>何<752,416,0>解<1168,0,0> <1168,232,0>怎<1400,184,0>落<1584,232,0>笔<1816,184,0>都<2000,296,0>不<2296,488,0>对\r\n[125549,1676]<0,335,0>独<335,224,0>缺<559,0,0> <559,184,0>你<743,200,0>一<943,216,0>生<1159,320,0>了<1479,197,0>解\r\n[127225,5976]<0,272,0>弹<272,352,0>指<624,458,0>岁<1082,782,0>月<1864,0,0> <1864,392,0>倾<2256,360,0>城<2616,336,0>顷<2952,425,0>刻<3377,439,0>间<3816,408,0>湮<4224,1064,0>灭\r\n[133201,6745]<0,456,0>青<456,456,0>石<912,488,0>板<1400,928,0>街<2328,0,0> <2328,296,0>回<2624,296,0>眸<2920,344,0>一<3264,480,0>笑<3744,600,0>你<4344,488,0>婉<4832,1168,0>约\r\n[139946,1799]<0,487,0>恨<487,376,0>了<863,440,0>没\r\n[141745,5408]<0,297,0>你<297,335,0>摇<632,408,0>头<1040,362,0>轻<1402,390,0>叹<1792,416,0>谁<2208,392,0>让<2600,400,0>你<3000,401,0>蹙<3401,447,0>着<3848,1040,0>眉\r\n[147153,5488]<0,552,0>而<552,385,0>深<937,879,0>闺<1816,0,0> <1816,296,0>徒<2112,344,0>留<2456,400,0>胭<2856,720,0>脂<3576,1008,0>味\r\n[152641,6144]<0,288,0>人<288,304,0>雁<592,360,0>南<952,736,0>飞<1688,0,0> <1688,344,0>转<2032,336,0>身<2368,432,0>一<2800,424,0>瞥<3224,480,0>你<3704,368,0>噙<4072,1064,0>泪\r\n[158785,6336]<0,304,0>掬<304,360,0>一<664,425,0>把<1089,775,0>月<1864,0,0> <1864,296,0>手<2160,376,0>揽<2536,408,0>回<2944,440,0>忆<3384,385,0>怎<3769,471,0>么<4240,2080,0>睡\r\n[165121,1936]<0,272,0>又<272,384,0>怎<656,344,0>么<1000,696,0>会\r\n[167057,5384]<0,264,0>心<264,360,0>事<624,352,0>密<976,376,0>缝<1352,440,0>绣<1792,408,0>花<2200,504,0>鞋<2704,336,0>针<3040,481,0>针<3521,479,0>怨<4000,712,0>怼\r\n[172441,5385]<0,400,0>若<400,384,0>花<784,424,0>怨<1208,440,0>蝶<1648,0,0> <1648,432,0>你<2080,384,0>会<2464,432,0>怨<2896,408,0>着<3304,1744,0>谁\r\n[177826,6172]<0,279,0>无<279,320,0>关<599,368,0>风<967,1032,0>月<1999,0,0> <1999,424,0>我<2423,432,0>题<2855,392,0>序<3247,441,0>等<3688,695,0>你<4383,1240,0>回\r\n[183998,6355]<0,360,0>悬<360,353,0>笔<713,431,0>一<1144,1288,0>绝<2432,0,0> <2432,264,0>那<2696,352,0>岸<3048,409,0>边<3457,455,0>浪<3912,384,0>千<4296,1736,0>叠\r\n[190353,6261]<0,333,0>情<333,400,0>字<733,432,0>何<1165,1064,0>解<2229,0,0> <2229,376,0>怎<2605,402,0>落<3007,430,0>笔<3437,488,0>都<3925,776,0>不<4701,1120,0>对\r\n[196614,6820]<0,376,0>而<376,377,0>我<753,455,0>独<1208,1200,0>缺<2408,0,0> <2408,320,0>你<2728,384,0>一<3112,352,0>生<3464,520,0>的<3984,480,0>了<4464,1512,0>解\r\n[203434,7376]<0,664,0>无<664,776,0>关<1440,784,0>风<2224,1192,0>月<3416,0,0> <3416,352,0>我<3768,377,0>题<4145,399,0>序<4544,512,0>等<5056,560,0>你<5616,1456,0>回\r\n[210810,6408]<0,392,0>手<392,392,0>书<784,416,0>无<1200,936,0>愧<2136,0,0> <2136,528,0>无<2664,415,0>惧<3079,409,0>人<3488,408,0>间<3896,464,0>是<4360,1528,0>非\r\n[217218,6192]<0,376,0>雨<376,328,0>打<704,456,0>蕉<1160,1152,0>叶<2312,0,0> <2312,440,0>又<2752,336,0>潇<3088,335,0>潇<3423,697,0>了<4120,424,0>几<4544,1072,0>夜\r\n[223410,7960]<0,392,0>我<392,384,0>等<776,457,0>春<1233,1215,0>雷<2448,0,0> <2448,320,0>来<2768,368,0>提<3136,416,0>醒<3552,600,0>你<4152,496,0>爱<4648,3312,0>谁\r\n"
}
```

***

### 4.4 获取艺术家详情

**接口地址**: `/artist/detail`  
**请求方法**: `GET`  
**接口描述**: 获取歌手详细信息

**请求参数**:

| 参数名 | 类型    | 必填 | 说明   |
| ------ | ------- | ---- | ------ |
| id     | integer | 是   | 歌手ID |

**响应示例**:

```json
{
    "data": {
        "birthday": "1997-08-24",
        "mv_count": 404,
        "pinyin_initial": "ALAN WALKER",
        "author_name": "Alan Walker",
        "sizable_avatar": "http://example.com/uploadpic/softhead/{size}/20230712/20230712122820571163.jpg",
        "is_publish": 1,
        "author_id": "178240",
        "album_count": 195,
        "fansnums": 6246778,
        "long_intro": [
            {
                "content": "1111111111，1997年8月24日出生于英国英格兰北安普敦郡，挪威DJ、音乐制作人。\n2014年11月，在网络平台上推出个人电音作品《Fade》而出道。2015年12月，通过索尼音乐发行个人第一首正式单曲《Faded》，该首歌曲的MV在网络平台上的点击量突破了12亿。他的成就远不至此：欧洲十个国家单曲榜冠军，英国单曲榜前十位，澳大利亚单曲榜亚军，也就在最近，更是跻身美国公告牌大热单曲榜。全球Shazam榜连续7周冠军，欧洲电台连续6周冠军。成长于数字化盛行的年代，Walker早期对电脑产生极大的兴趣爱好，之后更是转攻编程和平面设计。2012年他在网友的帮助和支持下开始做音乐，并迅速在游戏玩家中盛行起来，他们继而将Walker的音乐放在自己网络平台原创的视频中。这些歌曲迅速风靡全球并受到唱片公司的青睐，使他一跃成为当今最具潜力的年轻DJ之一。2016年6月，发行人声伴唱电音单曲《Sing Me To Sleep》。同年11月6日，获得MTV欧洲音乐奖“最佳挪威艺人”奖。2017年2月22日，《Faded》入围第37届全英音乐奖“年度英国单曲”奖。同年5月，发布首支由男声伴唱的电音作品《Tired》。2018年，发行首张录音室专辑《Different World》。2019年3月21日，发布《和平精英》盛夏推广曲《On My Way》。2021年1月29日，推出与ISÁK合作的歌曲《Sorry》。2023年7月7日，发行单曲《Land Of The Heroes》；11月10日发行专辑《Walkerworld》。",
                "title": "简介"
            },
            {
                "content": "********************************安普敦郡\n出生日期：1997年8月24日\n星座：处女座\n血型：O型\n身高：178cm\n职业：DJ、音乐制作人、唱片制作人、电子音乐人\n经纪公司：MER Musikk\n代表作品：Fade、Faded、Sing Me To Sleep、Alone、Tired、The Spectre\n主要成就：MTV欧洲音乐奖“最佳挪威艺人”奖、第37届全英音乐奖“年度英国单曲”奖提名、欧洲十个国家单曲榜冠军全球Shazam榜连续7周冠军",
                "title": "基本资料"
            },
            {
                "content": "****************************************《Fade》，随后该作品被NCS的音乐合集《NCS：Uplifting》收录。\n2015年，艾兰·沃克推出《Spectre》和《Force》两首作品；同年，他离开NCS，与索尼音乐旗下的MER Musikk签约；12月4日，通过索尼音乐发行第一首正式单曲《Faded》，该首歌曲以《Fade》为基础，加入了挪威女歌手艾斯琳·索尔海姆的演唱部分，该首歌曲的MV在网络平台上的点击量突破了10亿，并取得了超过400万的全球实体销量，还成为欧洲十个国家单曲榜冠军，荷兰DJ提雅斯多对该首歌进行了重混。\n2016年2月12日，艾兰·沃克受汉斯·季默等电影原声制作人的启发创作了《Fade》的管弦乐版本；2月27日，艾兰·沃克在挪威奥斯陆举行的第19届冬季极限运动会（Winter X Games）上表演了《Faded》等十五首歌曲，这是他的首次现场演出；6月3日，发行电音单曲《Sing Me To Sleep》，该首歌曲也是由艾斯琳·索尔海姆演唱，其MV在中国香港进行了拍摄；9月，在中国MTA天漠音乐节表演了《Faded》《Sing Me To Sleep》《Hymn For The Weekend（Remix）》等曲目，这是他的中国首演；11月5日，参加深圳丛林电子音乐节的演出；11月6日，获得MTV欧洲音乐奖“最佳挪威艺人”奖；12月2日，推出由挪威女歌手诺米·鲍（Noonie Bao）献唱的电音作品《Alone》，并同时释出歌曲的MV。\n2017年1月5日，获得第12届台湾KKBOX数位音乐风云榜“年度西洋歌手”和“年度西洋歌曲”奖；1月7日，来到中国上海MYST酒吧进行了演出；2月22日，《Faded》入围第37届全英音乐奖“年度英国单曲”奖；4月，推出与DJ K-391联合制作的EDM作品《Ignite》；5月，推出EDM曲目《Tired》，该首歌曲由爱尔兰创作歌手加文·詹姆斯（Gavin James）献声；9月，发行电音单曲《The Spectre》，该首歌曲在《Spectre》基础上进行了重新制作，并加入了人声；11月，在北京水立方参加英雄联盟音乐狂欢节的演出；12月，发行日本地区独享EP《Alan Walker Hits》。\n2018年1月，担任腾讯音乐娱乐集团与索尼音乐娱乐共同成立的国际电音厂牌Liquid State的全球合作伙伴；4月，发行日本地区独享EP《Faded Japan EP》；5月，发行由Julie Bergan、胜利献唱的单曲《Ignite》；7月，发行由Au、Ra、Tomine Harket演唱的单曲《Darkside》；8月31日，发行合唱单曲《Sheep》。9月，发行由Sophia Somajo演唱的单曲《Diamond Heart》；9月8日，参加韩国EDM音乐节“2018 SPECTRUM Dance Music Festival”。10月21日，参加综艺节目《天天向上》。12月1日，发行合唱单曲《Faded (即刻电音版)》。12月14日，发行个人首张录音室专辑《Different World》。\n2019年3月21日，发布《和平精英》盛夏推广曲《On My Way》；6月21日，发行由Pedro Capo、Farruko演唱的单曲《Calma》；7月25日，与A$AP Rocky发行单曲《Live Fast》。12月，获得2019TMEA腾讯音乐娱乐盛典年度最佳海外艺人的荣誉。\n2020年3月6日，联手挪威电音制作人K-391，及改变自的原曲制作人Ahrix，推出新单曲《End of Time》（时光尽头）。\n2021年1月29日，推出与ISÁK合作的歌曲《Sorry》。\n2023年7月7日，发行单曲《Land Of The Heroes》。11月10日发行专辑《Walkerworld》。12月31日，参加《2023最美的夜晚会》，演唱歌曲《友谊地久天长》。\n2024年2月2日，参加《2024央视网络春晚》演唱歌曲《我的家乡最闪耀4.0》。",
                "title": "演艺经历"
            },
            {
                "content": "热*********************************热门专辑：\n1.Different World\n2.Alone\n3.Fade",
                "title": "主要作品"
            },
            {
                "content": "全英**********************）\nMTV欧洲音乐奖\n▪2017 24届 最佳挪威艺人（获奖）\n波兰埃斯卡音乐奖\n▪2016-08-27 第15届 最佳国际热门歌曲 《Faded》（获奖）\n戛纳国际创意节\n▪2016-08-04 戛纳狮子奖 《Faded》（获奖）\nKKBOX数位音乐风云榜\n▪2017-01-05 第12届 年度西洋歌手（获奖）\n▪2017-01-05 第12届 年度西洋歌曲 《Faded》（获奖）\nTMEA腾讯音乐娱乐盛典\n▪2019 年度最佳海外艺人（获奖）",
                "title": "荣誉记录"
            }
        ],
        "area_id": "4",
        "song_count": 779,
        "intro": "Alan***********************************月24日出生于英国英格兰北安普敦郡，挪威DJ、音乐制作人。\n2014年11月，在网络平台上推出个人电音作品《Fade》而出道。2015年12月，通过索尼音乐发行个人第一首正式单曲《Faded》，该首歌曲的MV在网络平台上的点击量突破了12亿。他的成就远不至此：欧洲十个国家单曲榜冠军，英国单曲榜前十位，澳大利亚单曲榜亚军，也就在最近，更是跻身美国公告牌大热单曲榜。全球Shazam榜连续7周冠军，欧洲电台连续6周冠军。成长于数字化盛行的年代，Walker早期对电脑产生极大的兴趣爱好，之后更是转攻编程和平面设计。2012年他在网友的帮助和支持下开始做音乐，并迅速在游戏玩家中盛行起来，他们继而将Walker的音乐放在自己网络平台原创的视频中。这些歌曲迅速风靡全球并受到唱片公司的青睐，使他一跃成为当今最具潜力的年轻DJ之一。2016年6月，发行人声伴唱电音单曲《Sing Me To Sleep》。同年11月6日，获得MTV欧洲音乐奖“最佳挪威艺人”奖。2017年2月22日，《Faded》入围第37届全英音乐奖“年度英国单曲”奖。同年5月，发布首支由男声伴唱的电音作品《Tired》。2018年，发行首张录音室专辑《Different World》。2019年3月21日，发布《和平精英》盛夏推广曲《On My Way》。2021年1月29日，推出与ISÁK合作的歌曲《Sorry》。2023年7月7日，发行单曲《Land Of The Heroes》；11月10日发行专辑《Walkerworld》。",
        "user_status": 4
    },
    "msg": "",
    "status": 1,
    "error_code": 0
}
```

***

### 4.5 获取艺术家音频

**接口地址**: `/artist/audios`  
**请求方法**: `GET`  
**接口描述**: 获取歌手的歌曲列表

**请求参数**:

| 参数名   | 类型    | 必填 | 说明                            |
| -------- | ------- | ---- | ------------------------------- |
| id       | integer | 是   | 歌手ID                          |
| sort     | string  | 否   | 排序方式（hot-热门，time-最新） |
| page     | integer | 否   | 页码（默认1）                   |
| pagesize | integer | 否   | 每页数量（默认60）              |

**响应示例**:

```json
{
  "total" : 779,
  "error_code" : 0,
  "data" : [ {
    "video_track" : 0,
    "filesize_flac" : 0,
    "old_hide_flac" : 0,
    "playcount" : 0,
    "status" : 0,
    "video_file_head" : 1,
    "filesize_320" : 0,
    "rp_id" : 126769780,
    "bitrate_flac" : 0,
    "old_hide" : 1,
    "trans_param" : {
      "pay_block_tpl" : 1,
      "qualitymap" : {
        "bits" : "4",
        "attr0" : 4,
        "attr1" : 0
      },
      "union_cover" : "http://example.com/uploadpic/softhead/{size}/20241216/20241216154032567770.jpg",
      "songname_suffix" : "(10秒片段)",
      "cid" : -1,
      "language" : "英语",
      "cpy_attr0" : 2,
      "ipmap" : {
        "attr0" : 137455730688
      },
      "classmap" : {
        "attr0" : 100663304
      },
      "musicpack_advance" : 0,
      "display" : 0,
      "display_rate" : 0
    },
    "pay_type_128" : 0,
    "remarks" : [ ],
    "all_privs_super" : { },
    "video_hash" : "",
    "is_original" : 0,
    "version" : 16,
    "topic_url_high" : "",
    "identity" : 1,
    "album_id" : 0,
    "old_cpy_320" : 1,
    "privilege_high" : 0,
    "pay_type" : 0,
    "all_privs_128" : {
      "3" : {
        "fail_process" : 0,
        "privilege" : 5,
        "status" : 0
      },
      "4" : {
        "fail_process" : 0,
        "privilege" : 5,
        "status" : 0
      },
      "5" : {
        "fail_process" : 0,
        "privilege" : 5,
        "status" : 0
      },
      "6" : {
        "fail_process" : 0,
        "privilege" : 5,
        "status" : 0
      },
      "7" : {
        "fail_process" : 0,
        "privilege" : 5,
        "status" : 0
      }
    },
    "topic_url_flac" : "",
    "rp_publish" : 1,
    "pkg_price_super" : 0,
    "pkg_price_high" : 0,
    "pkg_price_flac" : 0,
    "video_filesize" : 0,
    "price_high" : 0,
    "tmp_privilege" : 1,
    "pkg_price" : 0,
    "status_320" : 1,
    "album_audio_id" : 346904035,
    "pay_type_super" : 0,
    "old_hide_320" : 0,
    "pkg_price_320" : 0,
    "topic_remark" : "",
    "pay_type_high" : 0,
    "privilege_128" : 5,
    "timelength_320" : 0,
    "price_flac" : 0,
    "fail_process_128" : 0,
    "audio_name" : "Ignite (10秒片段)",
    "timelength_flac" : 0,
    "price_320" : 0,
    "pay_type_flac" : 0,
    "topic_url" : "",
    "cid" : -1,
    "audio_file_head" : 100,
    "filesize_super" : 0,
    "topic_url_super" : "",
    "old_cpy_super" : 1,
    "all_privs_high" : { },
    "all_privs" : {
      "3" : {
        "fail_process" : 0,
        "privilege" : 5,
        "status" : 0
      },
      "4" : {
        "fail_process" : 0,
        "privilege" : 5,
        "status" : 0
      },
      "5" : {
        "fail_process" : 0,
        "privilege" : 5,
        "status" : 0
      },
      "6" : {
        "fail_process" : 0,
        "privilege" : 5,
        "status" : 0
      },
      "7" : {
        "fail_process" : 0,
        "privilege" : 5,
        "status" : 0
      }
    },
    "topic_url_320" : "",
    "mixsong_type" : 0,
    "rp_type_320" : "",
    "hash" : "CFDBD19A8C6656384E72E6D0111161D0",
    "author_name" : "K-*、Alan *****、Julie *、胜利",
    "all_privs_flac" : { },
    "fail_process_high" : 0,
    "status_flac" : 1,
    "audio_id" : 126769780,
    "status_128" : 0,
    "price_128" : 0,
    "rp_type_flac" : "",
    "pay_type_320" : 0,
    "all_privs_320" : { },
    "timelength_high" : 0,
    "filesize_128" : 176565,
    "rp_type_super" : "",
    "album_name" : "",
    "rp_type_high" : "",
    "topic_url_128" : "",
    "rp_type_128" : "audio",
    "songid" : 26838488,
    "rp_type" : "audio",
    "fail_process_super" : 0,
    "extname_super" : "",
    "status_high" : 1,
    "publish_date" : "2018-05-11",
    "hash_320" : "",
    "bitrate_super" : 0,
    "hash_high" : "",
    "extname" : "mp3",
    "fail_process_320" : 0,
    "fail_process" : 0,
    "old_cpy_high" : 1,
    "timelength_super" : 0,
    "old_cpy_flac" : 1,
    "level" : 2,
    "old_hide_super" : 0,
    "privilege_flac" : 0,
    "old_cpy" : 1,
    "status_super" : 1,
    "old_cpy_128" : 1,
    "old_hide_128" : 1,
    "video_timelength" : 0,
    "bitrate" : 128,
    "cd_url" : "",
    "privilege_320" : 0,
    "pkg_price_128" : 0,
    "price" : 0,
    "filesize_high" : 0,
    "filesize" : 176565,
    "timelength_128" : 10997,
    "price_super" : 0,
    "has_obbligato" : 1,
    "hash_128" : "CFDBD19A8C6656384E1111D0A17261D0",
    "album_audio_remark" : "",
    "bitrate_high" : 0,
    "fail_process_flac" : 0,
    "hash_flac" : "",
    "old_hide_high" : 0,
    "hash_super" : "",
    "timelength" : 10997,
    "privilege" : 5,
    "privilege_super" : 0,
    "video_id" : 0
  } ],
  "extra" : {
    "page_total" : 779,
    "group" : 0
  },
  "errcode" : 0,
  "status" : 1,
  "errmsg" : ""
}
```

***

## 推荐模块

### 5.1 每日推荐

**接口地址**: `/everyday/recommend`  
**请求方法**: `GET`  
**接口描述**: 获取每日推荐歌曲列表

**请求参数**: 无

**响应示例**:

```json
{
  "data": {
    "creation_date": "20250101",
    "mid": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
    "bi_biz": "rcmd_evd",
    "sign": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
    "song_list_size": 30,
    "OlexpIds": "30",
    "client_playlist_flag": 0,
    "is_guarantee_rec": 0,
    "song_list": [
      {
        "filesize_flac": 25773714,
        "official_songname": "",
        "ori_audio_name": "示例歌曲",
        "hash_192": "",
        "hash_flac": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
        "songname": "示例歌曲",
        "music_trac": 1,
        "is_original": 2,
        "pay_type": 3,
        "song_type": "1",
        "album_id": "123456",
        "remark": "",
        "language": "国语",
        "is_file_head_320": 100,
        "alg_path": "t=xxxxxxxxxx;b=7;recall_v2_alg=citid=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
        "is_file_head": 100,
        "filename": "示例歌手 - 示例歌曲",
        "cid": 0,
        "scid": 123456,
        "suffix_audio_name": "",
        "rec_copy_write": "圈友推荐",
        "mv_hash": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
        "hash": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
        "author_name": "示例歌手",
        "tags": [
          {
            "tag_id": "84",
            "parent_id": "7",
            "tag_name": "国语"
          }
        ],
        "rank_label": "示例标签",
        "bitrate": 128,
        "is_mv_file_head": 1,
        "has_accompany": 0,
        "filesize_128": 3621002,
        "album_name": "示例专辑",
        "ztc_mark": "t_xxxx_x_xxxxxx_xxxxxxxx_xx",
        "climax_end_time": 82300,
        "songid": 12345678,
        "quality_level": 3,
        "filesize_192": 0,
        "publish_date": "2020-01-01",
        "file_size": 3621002,
        "has_album": 0,
        "extname": "mp3",
        "type": "audio",
        "filesize_320": 9052173,
        "level": 2,
        "time_length": 226,
        "rec_copy_write_id": "3",
        "old_cpy": 0,
        "rec_label_prefix": "M_PHARSE_",
        "hash_128": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
        "hash_320": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
        "relate_goods": {},
        "mixsongid": "12345678",
        "hash_other": "",
        "sizable_cover": "http://example.com/stdmusic/{size}/xxxxxxxx/xxxxxxxxxxxxxxxxxxxxxx.jpg",
        "mv_type": 0,
        "publish_time": 0,
        "filesize_ape": 0,
        "rec_label_type": 7,
        "singerinfo": [
          {
            "name": "示例歌手",
            "is_publish": "1",
            "id": "12345"
          }
        ],
        "hash_ape": "",
        "trans_param": {
          "cpy_grade": 5,
          "classmap": {
            "attr0": 234881032
          },
          "language": "国语",
          "cpy_attr0": 58735744,
          "musicpack_advance": 1,
          "ogg_128_filesize": 2476430,
          "display_rate": 0,
          "cpy_level": 1,
          "pay_block_tpl": 1,
          "qualitymap": {
            "bits": "xxxxxxxxxxxxxxxxx",
            "attr0": 2000666677,
            "attr1": 1746993151
          },
          "hash_multitrack": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
          "hash_offset": {
            "clip_hash": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
            "start_byte": 0,
            "end_ms": 60000,
            "end_byte": 960145,
            "file_type": 0,
            "start_ms": 0,
            "offset_hash": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
          },
          "cid": 1234567,
          "display": 0,
          "ogg_320_hash": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
          "ipmap": {
            "attr0": 2200130564096
          },
          "appid_block": "xxxx,xxxx",
          "ogg_128_hash": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
          "union_cover": "http://example.com/stdmusic/{size}/xxxxxxxx/xxxxxxxxxxxxxxxxxxxxxx.jpg",
          "ogg_320_filesize": 8885445
        },
        "timelength_320": 226,
        "album_audio_remark": "",
        "album_audio_id": "12345678",
        "filesize_other": 0,
        "ips_tags": [
          {
            "name": "昨日超万人播放",
            "ip_id": "12345",
            "pid": "12345"
          }
        ],
        "privilege": 10,
        "fail_process": 4,
        "climax_start_time": 49500,
        "climax_timelength": 32800,
        "is_publish": 1,
        "rec_sub_copy_write": "示例推荐语"
      }
    ],
    "sub_title": "",
    "cover_img_url": "http://example.com/commendpic/xxxxxxxx/xxxxxxxxxxxxxxxxxxxxxx.jpg"
  },
  "status": 1,
  "error_code": 0
}
```

***

## 错误码说明

| 错误码 | 说明           |
| ------ | -------------- |
| 0      | 成功           |
| 20010  | 数据不存在     |
| 500    | 服务器内部错误 |
| 502    | 网关错误       |

***

## 注意事项

**数据格式**: 数组类型的响应在本文档中只展示第一个元素，实际返回数据可能包含更多内容

***

