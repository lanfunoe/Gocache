# 接口文档

---

## 目录

1. [用户模块](#用户模块)
2. [音乐库模块](#音乐库模块)
3. [搜索模块](#搜索模块)
4. [播放模块](#播放模块)
5. [推荐模块](#推荐模块)
6. [错误码说明](#错误码说明)

---

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
    "nickname": "TestUser",
    "k_nickname": "TestUser123",
    "fx_nickname": "TestUser456",
    "kq_talent": 0,
    "pic": "http://example.com/avatar1.jpg",
    "k_pic": "http://example.com/avatar2.jpg",
    "fx_pic": "http://example.com/avatar3.jpg",
    "gender": 1,
    "vip_type": 0,
    "m_type": 0,
    "y_type": 0,
    "descri": "",
    "follows": 2,
    "fans": 1,
    "visitors": 7,
    "constellation": -1,
    "city": "北京市",
    "province": "北京",
    "occupation": "",
    "userid": 123456789,
    "duration": 133878,
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

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| nickname | string | 用户昵称 |
| pic | string | 用户头像URL |
| gender | integer | 性别（1-男，2-女） |
| follows | integer | 关注数 |
| fans | integer | 粉丝数 |
| visitors | integer | 访客数 |
| city | string | 所在城市 |
| province | string | 所在省份 |
| userid | integer | 用户ID |
| vip_type | integer | VIP类型 |

---

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

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| total | integer | 关注总数 |
| lists | array | 关注列表 |
| nickname | string | 歌手名称 |
| singerid | integer | 歌手ID |
| pic | string | 歌手头像 |
| source_desc | string | 来源描述 |

---

### 1.3 获取用户听歌记录

**接口地址**: `/user/listen`
**请求方法**: `GET`
**接口描述**: 获取用户听歌历史记录

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| type | integer | 否 | 类型（1-最近播放） |

**响应示例**:
```json
{
  "data": {
    "data_date": 0,
    "servertime": 1609459200,
    "list": [{
      "hash": "ABCD1234567890EFGH1234567890ABCD",
      "duration": 285309,
      "type": "audio",
      "id": 123456789,
      "singername": "测试歌手",
      "name": "测试歌手 - 测试歌曲",
      "album_name": "测试专辑",
      "listen_count": 99
    }],
    "listen_total": 3953,
    "total_history": 3953,
    "total_week": 0
  },
  "status": 1,
  "error_code": 0
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| list | array | 听歌记录列表 |
| hash | string | 歌曲Hash |
| singername | string | 歌手名称 |
| name | string | 歌曲名称 |
| album_name | string | 专辑名称 |
| listen_count | integer | 播放次数 |
| listen_total | integer | 总播放次数 |

---

### 1.4 获取用户歌单

**接口地址**: `/user/playlist`
**请求方法**: `GET`
**接口描述**: 获取用户创建和收藏的歌单列表

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pagesize | integer | 否 | 每页数量（默认500） |
| t | integer | 否 | 时间戳 |

**响应示例**:
```json
{
  "data": {
    "total": 15,
    "list": [
      {
        "specialid": 100001,
        "specialname": "我喜欢的音乐",
        "imgurl": "http://example.com/playlist1.jpg",
        "playcount": "15234",
        "songcount": 234,
        "userid": 123456789,
        "nickname": "TestUser",
        "intro": "收藏我喜欢的所有音乐",
        "dateline": 1609459200,
        "isglobal": 0,
        "play_count": 15234,
        "share_count": 89,
        "isdel": 0,
        "ptype": 0
      }
    ]
  },
  "status": 1,
  "error_code": 0
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| total | integer | 歌单总数 |
| list | array | 歌单列表 |
| specialid | integer | 歌单ID |
| specialname | string | 歌单名称 |
| imgurl | string | 歌单封面URL |
| playcount | string | 播放次数 |
| songcount | integer | 歌曲数量 |
| intro | string | 歌单简介 |

---

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

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| is_vip | integer | 是否为VIP |
| vip_type | integer | VIP类型 |
| vip_begin_time | string | VIP开始时间 |
| vip_end_time | string | VIP结束时间 |
| busi_vip | array | 业务VIP列表 |
| svip_level | integer | SVIP等级 |
| svip_score | integer | SVIP积分 |

---

## 音乐库模块

### 2.1 获取歌单标签

**接口地址**: `/playlist/tags`
**请求方法**: `GET`
**接口描述**: 获取歌单分类标签列表

**请求参数**: 无

**响应示例**:
```json
{
  "data": {
    "tags": [
      {
        "tagid": 1001,
        "tagname": "华语",
        "parentid": 0,
        "special_type": 0,
        "hot": 1,
        "publish": 1,
        "imgurl": "",
        "color": "#FF6B6B"
      },
      {
        "tagid": 1002,
        "tagname": "欧美",
        "parentid": 0,
        "special_type": 0,
        "hot": 1,
        "publish": 1,
        "imgurl": "",
        "color": "#4ECDC4"
      }
    ],
    "total": 10
  },
  "status": 1,
  "error_code": 0
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| tags | array | 标签列表 |
| tagid | integer | 标签ID |
| tagname | string | 标签名称 |
| parentid | integer | 父级标签ID（0为顶级标签） |
| hot | integer | 是否热门标签 |
| publish | integer | 是否已发布 |
| color | string | 标签颜色 |

---

### 2.2 获取歌单所有歌曲

**接口地址**: `/playlist/track/all`
**请求方法**: `GET`
**接口描述**: 获取指定歌单的所有歌曲列表

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | integer | 是 | 歌单ID |
| page | integer | 否 | 页码（默认1） |
| pagesize | integer | 否 | 每页数量（默认50） |

**响应示例**:
```json
{
  "data": {
    "info": {
      "specialid": 100001,
      "specialname": "我喜欢的音乐",
      "imgurl": "http://example.com/playlist1.jpg",
      "nickname": "TestUser",
      "intro": "收藏我喜欢的所有音乐",
      "userid": 123456789,
      "playcount": 15234,
      "songcount": 234
    },
    "list": [
      {
        "hash": "EFGH1234567890IJKL1234567890EFGH",
        "songname": "测试歌曲",
        "singername": "测试歌手",
        "duration": 269,
        "album_img": "http://example.com/album1.jpg",
        "album_name": "测试专辑",
        "filename": "测试歌曲",
        "filesize": 10789440,
        "bitrate": 320,
        "audio_id": 123456789,
        "album_id": 1001,
        "play_count": 1000000,
        "collect_count": 50000,
        "share_count": 3000,
        "comment_count": 2000
      }
    ],
    "total": 234,
    "page": 1,
    "pagesize": 50,
    "pages": 5
  },
  "status": 1,
  "error_code": 0
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| info | object | 歌单信息 |
| list | array | 歌曲列表 |
| hash | string | 歌曲唯一标识 |
| songname | string | 歌曲名称 |
| singername | string | 歌手名称 |
| duration | integer | 歌曲时长（秒） |
| album_img | string | 专辑图片URL |
| play_count | integer | 播放次数 |
| collect_count | integer | 收藏次数 |

---

### 2.3 获取排行榜列表

**接口地址**: `/rank/list`
**请求方法**: `GET`
**接口描述**: 获取所有排行榜分类和列表

**请求参数**: 无

**响应示例**:
```json
{
  "data": {
    "rank": {
      "total": 25,
      "list": [
        {
          "rankid": 1001,
          "rankname": "热门排行榜",
          "intro": "热门歌曲排行榜，单周播放次数最多的歌曲",
          "imgurl": "http://example.com/rank1.jpg",
          "type": 0,
          "model": 0,
          "webview_url": "",
          "share_img": "",
          "music_type": 0,
          "bangid": 1001
        }
      ]
    },
    "category": [
      {
        "catname": "官方榜",
        "typeid": 1,
        "ranklist": [
          {
            "rankid": 1001,
            "rankname": "热门排行榜",
            "type": 0,
            "imgurl": "http://example.com/rank1.jpg"
          }
        ]
      }
    ]
  },
  "status": 1,
  "error_code": 0
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| rank | object | 排行榜完整列表 |
| category | array | 排行榜分类 |
| rankid | integer | 排行榜ID |
| rankname | string | 排行榜名称 |
| intro | string | 排行榜简介 |
| imgurl | string | 排行榜图片 |
| catname | string | 分类名称 |
| typeid | integer | 分类ID |

---

### 2.4 获取音频排行榜

**接口地址**: `/rank/audio`
**请求方法**: `GET`
**接口描述**: 获取指定排行榜的歌曲列表

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| rankid | integer | 是 | 排行榜ID |
| page | integer | 否 | 页码（默认1） |
| pagesize | integer | 否 | 每页数量（默认50） |

**响应示例**:
```json
{
  "data": {
    "rank": {
      "rankid": 1001,
      "rankname": "热门排行榜",
      "intro": "热门歌曲排行榜，单周播放次数最多的歌曲",
      "imgurl": "http://example.com/rank1.jpg",
      "type": 0,
      "bangid": 1001
    },
    "list": [
      {
        "rank": 1,
        "hash": "IJKL1234567890MNOP1234567890IJKL",
        "songname": "测试歌曲",
        "singername": "测试歌手",
        "duration": 269,
        "album_img": "http://example.com/album1.jpg",
        "album_name": "测试专辑",
        "filename": "测试歌曲",
        "filesize": 10789440,
        "bitrate": 320,
        "audio_id": 123456789,
        "album_id": 1001,
        "mvhash": "",
        "play_count": 5000000,
        "collect_count": 200000,
        "share_count": 10000,
        "comment_count": 8000,
        "score": 95,
        "trend": "up"
      }
    ],
    "total": 500,
    "page": 1,
    "pagesize": 50,
    "pages": 10
  },
  "status": 1,
  "error_code": 0
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| rank | object | 排行榜信息 |
| list | array | 歌曲列表 |
| rank | integer | 歌曲排名 |
| hash | string | 歌曲唯一标识 |
| songname | string | 歌曲名称 |
| singername | string | 歌手名称 |
| duration | integer | 歌曲时长（秒） |
| play_count | integer | 播放次数 |
| collect_count | integer | 收藏次数 |
| score | integer | 歌曲评分 |
| trend | string | 排名趋势（up-上升，down-下降，same-持平） |

---

### 2.5 获取热门歌单

**接口地址**: `/top/playlist`
**请求方法**: `GET`
**接口描述**: 获取热门歌单列表

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| category_id | integer | 否 | 分类ID（默认0） |

**响应示例**:
```json
{
  "data": {
    "list": [
      {
        "specialid": 100002,
        "specialname": "测试歌单",
        "imgurl": "http://example.com/playlist2.jpg",
        "playcount": "5201314",
        "songcount": 100,
        "userid": 123456789,
        "nickname": "TestUser",
        "intro": "精选高品质音乐，陪伴你的每一个时刻"
      }
    ],
    "total": 100
  },
  "status": 1,
  "error_code": 0
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| list | array | 歌单列表 |
| specialid | integer | 歌单ID |
| specialname | string | 歌单名称 |
| imgurl | string | 歌单封面 |
| playcount | string | 播放次数 |
| songcount | integer | 歌曲数量 |
| intro | string | 歌单简介 |

---

## 搜索模块

### 3.1 搜索音乐

**接口地址**: `/search`
**请求方法**: `GET`
**接口描述**: 搜索歌曲、歌手、专辑、歌单

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| keywords | string | 是 | 搜索关键词 |
| page | integer | 否 | 页码（默认1） |
| pagesize | integer | 否 | 每页数量（默认30） |
| type | string | 否 | 搜索类型（song-歌曲，author-歌手，album-专辑，special-歌单） |

**响应示例**:
```json
{
  "data": {
    "list": [
      {
        "SongName": "测试歌曲",
        "SingerName": "测试歌手",
        "Hash": "MNOP1234567890QRST1234567890MNOP",
        "Duration": 269,
        "AlbumImg": "http://example.com/album1.jpg",
        "AlbumName": "测试专辑",
        "SongIndex": 1
      }
    ],
    "total": 472,
    "pages": 16,
    "currPage": 1,
    "pageSize": 30
  },
  "status": 1,
  "error_code": 0
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| list | array | 搜索结果列表 |
| SongName | string | 歌曲名称 |
| SingerName | string | 歌手名称 |
| Hash | string | 歌曲Hash值 |
| Duration | integer | 时长（秒） |
| AlbumImg | string | 专辑图片 |
| AlbumName | string | 专辑名称 |
| total | integer | 总结果数 |
| currPage | integer | 当前页码 |

---

### 3.2 搜索歌词

**接口地址**: `/search/lyric`
**请求方法**: `GET`
**接口描述**: 根据歌词片段搜索歌曲

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| hash | string | 是 | 歌曲Hash值 |

**响应示例**:
```json
{
  "candidates": [
    {
      "id": 123456789,
      "accesskey": "ABCD1234567890EFGH1234567890ABCD",
      "transcript": "测试歌词内容 这是一段示例歌词...",
      "duration": 269,
      "songname": "测试歌曲",
      "artistname": "测试歌手"
    }
  ],
  "status": 1,
  "error_code": 0
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| candidates | array | 候选歌曲列表 |
| id | integer | 歌词ID |
| accesskey | string | 访问密钥 |
| transcript | string | 歌词片段 |
| songname | string | 歌曲名称 |
| artistname | string | 艺术家名称 |

---

## 播放模块

### 4.1 获取歌曲播放链接

**接口地址**: `/song/url`
**请求方法**: `GET`
**接口描述**: 获取歌曲的播放URL

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| hash | string | 是 | 歌曲Hash值 |
| album_id | integer | 是 | 专辑ID |
| quality | string | 否 | 音质（128/320/999） |

**响应示例**:
```json
{
  "data": {
    "url": "http://example.com/music/202512180026/abcd1234567890...",
    "hash": "QRST1234567890UVWX1234567890QRST",
    "time": 269,
    "size": 8192000,
    "bitrate": 320,
    "filetype": "mp3",
    "audio_id": 123456789
  },
  "status": 1,
  "error_code": 0
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| url | string | 播放链接 |
| time | integer | 时长（秒） |
| size | integer | 文件大小 |
| bitrate | integer | 比特率 |
| filetype | string | 文件类型 |
| audio_id | integer | 音频ID |

---

### 4.2 获取歌曲高潮部分

**接口地址**: `/song/climax`
**请求方法**: `GET`
**接口描述**: 获取歌曲的高潮片段时间点

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| hash | string | 是 | 歌曲Hash值 |

**响应示例**:
```json
{
  "data": {
    "hash": "UVWX1234567890YZAB1234567890UVWX",
    "climax_start": 85,
    "climax_end": 125,
    "climax_duration": 40,
    "confidence": 0.95
  },
  "status": 1,
  "error_code": 0
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| hash | string | 歌曲Hash |
| climax_start | integer | 高潮开始时间（秒） |
| climax_end | integer | 高潮结束时间（秒） |
| climax_duration | integer | 高潮时长（秒） |
| confidence | float | 置信度 |

---

### 4.3 获取歌词

**接口地址**: `/lyric`
**请求方法**: `GET`
**接口描述**: 获取歌曲的歌词内容

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | integer | 是 | 歌词ID |
| accesskey | string | 是 | 访问密钥 |
| fmt | string | 否 | 格式（krc/lrc/txt） |
| decode | boolean | 否 | 是否解码（true/false） |

**响应示例**:
```json
{
  "data": {
    "content": "[ar:测试歌手]\n[ti:测试歌曲]\n[al:测试专辑]\n[by:]\n[offset:0]\n\n[00:00.00]这是第一句歌词\n[00:02.00]这是第二句歌词\n[00:04.00]这是第三句歌词\n[00:06.00]这是第四句歌词\n...",
    "lyric_version": 1,
    "trans_user": "",
    "trans_type": 0
  },
  "status": 1,
  "error_code": 0
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| content | string | 歌词内容 |
| lyric_version | integer | 歌词版本 |
| trans_user | string | 翻译用户 |
| trans_type | integer | 翻译类型 |

---

### 4.4 获取艺术家详情

**接口地址**: `/artist/detail`
**请求方法**: `GET`
**接口描述**: 获取歌手详细信息

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | integer | 是 | 歌手ID |

**响应示例**:
```json
{
  "data": {
    "singerid": 1001,
    "singername": "测试歌手",
    "avatar": "http://example.com/singer/avatar1.jpg",
    "gender": 1,
    "country": "中国",
    "province": "北京",
    "city": "",
    "company": "测试唱片公司",
    "intro": "测试歌手简介，这是一段示例文字...",
    "fans_count": 1000000,
    "song_count": 500,
    "album_count": 20,
    "mv_count": 100
  },
  "status": 1,
  "error_code": 0
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| singerid | integer | 歌手ID |
| singername | string | 歌手名称 |
| avatar | string | 头像URL |
| gender | integer | 性别 |
| country | string | 国家 |
| province | string | 省份 |
| company | string | 唱片公司 |
| intro | string | 歌手简介 |
| fans_count | integer | 粉丝数量 |
| song_count | integer | 歌曲数量 |
| album_count | integer | 专辑数量 |

---

### 4.5 获取艺术家音频

**接口地址**: `/artist/audios`
**请求方法**: `GET`
**接口描述**: 获取歌手的歌曲列表

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | integer | 是 | 歌手ID |
| sort | string | 否 | 排序方式（hot-热门，time-最新） |
| page | integer | 否 | 页码（默认1） |
| pagesize | integer | 否 | 每页数量（默认60） |

**响应示例**:
```json
{
  "data": {
    "list": [
      {
        "hash": "CDEF1234567890GHIJ1234567890CDEF",
        "songname": "测试歌曲",
        "singername": "测试歌手",
        "duration": 269,
        "album_img": "http://example.com/album2.jpg",
        "album_name": "测试专辑",
        "play_count": 1000000,
        "filesize": 8192000
      }
    ],
    "total": 500,
    "pages": 10,
    "currPage": 1
  },
  "status": 1,
  "error_code": 0
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| list | array | 歌曲列表 |
| hash | string | 歌曲Hash |
| songname | string | 歌曲名称 |
| album_name | string | 专辑名称 |
| play_count | integer | 播放次数 |
| filesize | integer | 文件大小 |
| total | integer | 总歌曲数 |
| currPage | integer | 当前页码 |

---

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
    "list": [
      {
        "hash": "KLMN1234567890OPQR1234567890KLMN",
        "songname": "测试歌曲",
        "singername": "测试歌手",
        "duration": 269,
        "album_img": "http://example.com/album3.jpg",
        "album_name": "测试专辑",
        "play_url": "http://example.com/music/202512180026/abcd1234567890...",
        "filesize": 0,
        "bitrate": 320,
        "audio_id": 123456789,
        "album_id": 1001
      }
    ],
    "total": 30
  },
  "status": 1,
  "error_code": 0
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| list | array | 推荐歌曲列表 |
| hash | string | 歌曲唯一标识 |
| songname | string | 歌曲名称 |
| singername | string | 歌手名称 |
| duration | integer | 歌曲时长（秒） |
| album_img | string | 专辑图片URL |
| play_url | string | 播放链接 |
| bitrate | integer | 比特率 |
| total | integer | 推荐总数 |

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 0 | 成功 |
| 20010 | 数据不存在 |
| 500 | 服务器内部错误 |
| 502 | 网关错误 |

---

## 注意事项

1. **数据格式**: 数组类型的响应在本文档中只展示第一个元素，实际返回数据可能包含更多内容
2. **图片URL**: 图片URL中的 `{size}` 需要替换为实际尺寸（如480、320等）

---
