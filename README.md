# Gocache

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)](https://maven.apache.org/)

> 🚀 Gocache 是对 [KuGouMusicApi](https://github.com/MakcRe/KuGouMusicApi) 的 Java Spring Boot 实现，提供了完整的酷狗音乐平台 API 访问能力。项目采用响应式编程范式，并计划内置多级缓存机制以提升响应速度。



## ✨ 特性

- 🎯 **完全兼容** - 与原 Node.js 版本 API 100% 兼容
- ⚡ **响应式编程** - 基于 Spring WebFlux，支持高并发
- 🔐 **多种认证方式** - 支持二维码、手机号等多种登录方式
- 🎵 **丰富的音乐功能** - 搜索、播放列表、歌词、排行榜等
- 📊 **分布式追踪** - 集成 Micrometer Tracing，便于问题排查
- 🎨 **优雅的架构** - 清晰的分层设计，易于扩展和维护
- **三层缓存架构**（实现）
  - L1: Caffeine 内存缓存（热点数据快速访问）
  - L2: SQLite 持久化缓存（歌词、歌手信息等元数据）
  - L3: 本地文件系统（歌曲、图片等媒体文件）
- **媒体文件本地存储**
    - 歌曲/图片异步下载和本地缓存
    - LRU 淘汰策略，可配置存储空间上限
    - 支持 Range 请求（断点续传）
- **缓存管理 API**
    - 缓存统计和监控
    - 存储空间动态配置
    - 手动清理和 LRU 淘汰

## 🗄️ 缓存架构

### 三层缓存设计

```
L1: Caffeine (In-Memory)  →  L2: SQLite (Persistent)  →  L3: Local Files (Media)
```

### 缓存策略

| 数据类型 | 缓存层级 | TTL | 存储位置 |
|---------|---------|-----|---------|
| 歌词 | L1 + L2 | 永久 (LRU) | SQLite |
| 图片 | L3 | 永久 | 本地文件 |
| 歌曲 | L3 | 永久 (LRU) | 本地文件 |
| 歌手信息 | L1 + L2 | 7 天 | SQLite |
| 分类信息 | L1 + L2 | 24 小时 | SQLite |
| 歌单详情 | L1 | 2 小时 | 仅内存 |
| 热搜 | L1 | 30 分钟 | 仅内存 |
| 搜索结果 | L1 | 5 分钟 | 仅内存 |
| 用户信息 | L1 | 1 小时 | 仅内存 |

## 📋 计划中的功能

- [ ] 🚀 响应缓存，提升接口响应速度
- [ ] 📈 性能监控指标
- [ ] 迁移剩余 api


## 🚀 快速开始

### 环境要求

- Java 21+
- Maven 3.6+

### 安装运行

```bash
# 克隆项目
git clone https://github.com/lanfunoe/Gocache.git
cd Gocache

# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

服务启动后访问 `http://localhost:6521`


## 📊 API 实现状态

### ✅ 已迁移且验证的功能
<details>
<summary>点击展开完整列表</summary>

1. /user/detail - 获取用户详情
2. /user/follow - 获取用户关注列表
3. /user/listen - 获取用户听歌记录
4. /user/playlist - 获取用户歌单
5. /user/vip/detail - 获取用户VIP信息
6. /artist/audios - 获取艺术家音频
7. /artist/detail - 获取艺术家详情
8. /lyric - 获取歌词
9. /everyday/recommend - 每日推荐
10. /search - 搜索音乐
11. /search/lyric - 搜索歌词
12. /top/playlist - 热门歌单
13. /song/climax - 歌曲高潮部分
14. /song/url - 歌曲播放链接
15. /login/qr/check - 检查二维码登录
16. /login/qr/create - 创建二维码
17. /login/qr/key - 获取二维码密钥
18. /youth/day/vip - 青春版日VIP
19. /youth/vip - 青春版VIP
20. /playlist/tags - 歌单标签
21. /playlist/track/all - 歌单所有歌曲
22. /rank/list - 排行榜列表
23. /rank/audio - 音频排行榜

</details>

### 🚧 待迁移的功能
<details>
<summary>点击展开完整列表</summary>

| 分类 | API |
|------|-----|
| **AI/推荐** | /ai/recommend |
| **专辑** | /album, /album/detail, /album/shop, /album/songs |
| **艺术家** | /artist/albums, /artist/follow, /artist/follow/newsongs, /artist/honour, /artist/lists, /artist/unfollow, /artist/videos |
| **音频** | /audio, /audio/accompany/matching, /audio/ktv/total, /audio/related |
| **刷新** | /brush |
| **验证码** | /captcha/sent |
| **评论** | /comment/album, /comment/count, /comment/floor, /comment/music, /comment/music/classify, /comment/music/hotword, /comment/playlist |
| **每日** | /everyday/friend, /everyday/history, /everyday/style/recommend |
| **收藏** | /favorite/count |
| **FM** | /fm/class, /fm/image, /fm/recommend, /fm/songs |
| **图片** | /images, /images/audio |
| **IP** | /ip, /ip/dateil, /ip/playlist, /ip/zone, /ip/zone/home |
| **KMR** | /kmr/audio/mv, /krm/audio |
| **最近** | /lastest/songs/listen |
| **登录** | /login, /login/cellphone, /login/openplat, /login/token, /login/wx/check, /login/wx/create |
| **长音频** | /longaudio/album/audios, /longaudio/album/detail, /longaudio/daily/recommend, /longaudio/rank/recommend, /longaudio/vip/recommend, /longaudio/week/recommend |
| **PC电台** | /pc/diantai |
| **私人FM** | /personal/fm |
| **播放历史** | /playhistory/upload |
| **歌单** | /playlist/add, /playlist/del, /playlist/detail, /playlist/effect, /playlist/similar, /playlist/track/all/new, /playlist/tracks/add, /playlist/tracks/del |
| **特权** | /privilege/lite |
| **排行榜** | /rank/info, /rank/top, /rank/vol |
| **推荐** | /recommend/songs |
| **注册** | /register/dev |
| **场景** | /scene/audio/list, /scene/collection/list, /scene/lists, /scene/lists/v2, /scene/module, /scene/module/info, /scene/music, /scene/video/list |
| **搜索** | /search/complex, /search/default, /search/hot, /search/mixed, /search/suggest |
| **服务器** | /server/now |
| **歌单集** | /sheet/collection, /sheet/collection/detail, /sheet/detail, /sheet/hot, /sheet/list |
| **歌手** | /singer/list |
| **歌曲** | /song/ranking, /song/ranking/filter, /song/url/new |
| **主题** | /theme/music, /theme/music/detail, /theme/playlist, /theme/playlist/track |
| **榜单** | /top/album, /top/card, /top/ip, /top/song |
| **用户** | /user/cloud, /user/cloud/url, /user/history, /user/video/collect, /user/video/love |
| **视频** | /video/detail, /video/privilege, /video/url |
| **青春版** | /youth/channel/all, /youth/channel/amway, /youth/channel/detail, /youth/channel/similar, /youth/channel/song, /youth/channel/song/detail, /youth/channel/sub, /youth/day/vip/upgrade, /youth/dynamic, /youth/dynamic/recent, /youth/listen/song, /youth/month/vip/record, /youth/union/vip, /youth/user/song |
| **乐库** | /yueku, /yueku/banner, /yueku/fm |

</details>

## 🛠️ 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 21 | LTS 版本 |
| Spring Boot | 3.4.1 | 应用框架 |
| WebFlux | - | 响应式 Web 框架 |
| Micrometer Tracing | - | 分布式追踪 |
| BouncyCastle | 1.78.1 | 加密库 |
| ZXing | 3.5.2 | 二维码生成 |
| Maven | 3.6+ | 构建工具 |
| Caffeine | 3.1.8 | 高性能本地缓存 |


## 🏗️ 项目结构

```
src/main/java/com/lanfunoe/gocache/
├── config/          # 配置类
│   ├── CorsConfig.java
│   ├── TracingConfig.java
│   ├── WebConfig.java
│   └── GocacheConfig.java
├── controller/      # REST 控制器
│   ├── BaseController.java
│   ├── RootController.java
│   ├── LoginController.java
│   ├── UserController.java
│   ├── ArtistController.java
│   └── ...
├── service/         # 业务逻辑层
│   ├── auth/        # 认证服务
│   ├── user/        # 用户服务
│   ├── music/       # 音乐服务
│   ├── lyrics/      # 歌词服务
│   ├── search/      # 搜索服务
│   ├── playlist/    # 歌单服务
│   └── cache/       # 缓存服务
├── filter/          # WebFlux 过滤器
├── util/            # 工具类
├── model/           # 数据模型
├── constants/       # 常量定义
└── exception/       # 异常处理
```

## 🤝 贡献指南

欢迎所有形式的贡献！


## 📝 更新日志

### v0.0.1 (2024-12-16)
- ✨ 初始版本发布
- ✅ 实现 23 个核心 API
- ✅ 支持二维码登录
- ✅ 集成分布式追踪

## 📄 API 文档

本项目 API 与原 Node.js 版本完全兼容，详细接口文档请参考：[KuGouMusicApi 文档](https://github.com/MakcRe/KuGouMusicApi)


## ⚠️ 免责声明

> 1. 本项目仅供学习使用，请尊重版权，请勿利用此项目从事商业行为及非法用途！
> 2. 使用本项目的过程中可能会产生版权数据。对于这些版权数据，本项目不拥有它们的所有权。为了避免侵权，使用者务必在 24 小时内清除使用本项目的过程中所产生的版权数据。
> 3. 由于使用本项目产生的包括由于本协议或由于使用或无法使用本项目而引起的任何性质的任何直接、间接、特殊、偶然或结果性损害（包括但不限于因商誉损失、停工、计算机故障或故障引起的损害赔偿，或任何及所有其他商业损害或损失）由使用者负责。
> 4. **禁止在违反当地法律法规的情况下使用本项目。** 对于使用者在明知或不知当地法律法规不允许的情况下使用本项目所造成的任何违法违规行为由使用者承担，本项目不承担由此造成的任何直接、间接、特殊、偶然或结果性责任。
> 5. 音乐平台不易，请尊重版权，支持正版。
> 6. 本项目仅用于对技术可行性的探索及研究，不接受任何商业（包括但不限于广告等）合作及捐赠。
> 7. 如果官方音乐平台觉得本项目不妥，可联系本项目更改或移除。

## 🙏 致谢

- [MakcRe/KuGouMusicApi](https://github.com/MakcRe/KuGouMusicApi) - 原 Node.js 版本
- [iAJue/MoeKoeMusic](https://github.com/MoeKoeMusic/MoeKoeMusic) - 酷狗概念版第三方客户端

## 📄 License

本项目采用 [MIT License](LICENSE) 开源协议。

