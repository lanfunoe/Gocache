package com.lanfunoe.gocache.constants;

/**
 * API常量类
 * 用于管理所有API相关的常量和配置
 */
public final class GocacheConstants {

    private GocacheConstants() {
        // 防止实例化
    }

    // API基础配置
    public static final String API_GATEWAY_URL = "https://gateway.kugou.com";
    public static final String LOGIN_USER_DOMAIN = "login.user.kugou.com";
    public static final String USERCENTER_DOMAIN = "usercenter.kugou.com";
    public static final String VIP_DOMAIN = "https://kugouvip.kugou.com";
    public static final String LISTEN_SERVICE_DOMAIN = "https://listenservice.kugou.com";
    public static final String RELATION_USER_DOMAIN = "relationuser.kugou.com";
    public static final String CLOUD_LIST_DOMAIN = "cloudlist.service.kugou.com";
    public static final String MCLOUD_SERVICE_DOMAIN = "https://mcloudservice.kugou.com";
    public static final String LYRIC_DOMAIN = "https://lyrics.kugou.com";
    public static final String EXPENDABLE_KMR_CDN_DOMAIN = "https://expendablekmrcdn.kugou.com";
    public static final String MEDIA_STORE_DOMAIN = "media.store.kugou.com";
    public static final String OPENAPI_DOMAIN = "openapi.kugou.com";
    public static final String TRACKER_CDN_DOMAIN = "trackercdn.kugou.com";

    // 完整URL常量（用于硬编码URL替换）
    public static final String LOGIN_USER_URL = "https://login-user.kugou.com";
    public static final String H5_QRCODE_URL = "https://h5.kugou.com/apps/loginQRCode/html/index.html";

    // 请求头常量
    public static final String HEADER_X_ROUTER = "x-router";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_KG_TID = "kg-tid";


    // 请求路径常量
    public static final String PATH_LOGIN_BY_PWD = "/v9/login_by_pwd";
    public static final String PATH_QRCODE = "/v2/qrcode";
    public static final String PATH_GET_USERINFO_QRCODE = "/v2/get_userinfo_qrcode";
    public static final String PATH_SEND_MOBILE_CODE = "/v7/send_mobile_code";
    public static final String PATH_GET_MY_INFO = "/v3/get_my_info";
    public static final String PATH_GET_UNION_VIP = "/v1/get_union_vip";
    public static final String PATH_GET_LIST = "/v2/get_list";
    public static final String PATH_FOLLOW_LIST = "/v4/follow_list";
    public static final String PATH_GET_ALL_LIST = "/v7/get_all_list";
    public static final String PATH_GET_CLOUD_LIST = "/v1/get_list";
    public static final String PATH_QUERY_MUSICCLOUND_URL = "/bsstrackercdngz/v2/query_musicclound_url";
    public static final String PATH_SONG_URL = "/v5/url";
    public static final String PATH_AUDIO_CLIMAX = "/v1/audio_climax/audio";
    public static final String PATH_GET_RES_PRIVILEGE = "/v2/get_res_privilege/lite";
    public static final String PATH_SEARCH_LYRICS = "/v1/search";
    public static final String PATH_DOWNLOAD_LYRICS = "/download";
    public static final String PATH_AUTHOR_DETAIL = "/kmr/v3/author";
    public static final String PATH_AUTHOR_AUDIOS = "/kmr/v1/audio_group/author";
    public static final String PATH_FOLLOW_SINGER = "/followservice/v3/follow_singer";
    public static final String PATH_UNFOLLOW_SINGER = "/followservice/v3/unfollow_singer";

    // 平台/设备类型
    public static final int PLATFORM_PHONE = 1;
    public static final int PLATFORM_WEB = 4;
    public static final int PLATFORM_ANDROID = 3;
    public static final int PLATFORM_LITE = 0;

    // 应用ID
    public static final Integer Lite_APP_ID = 3116;
    public static final int APP_ID_WEB = 1014;
    public static final int APP_ID_ANDROID = 1005;
    public static final int APP_ID_QR_DEFAULT = 1001;

    // 通用默认值
    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_PAGE_SIZE = 30;
    public static final long DEFAULT_ALBUM_ID = 0L;
    public static final long DEFAULT_ALBUM_AUDIO_ID = 0L;
    public static final long DEFAULT_AUDIO_ID = 0L;
    public static final String DEFAULT_QUALITY = "128";
    public static final String DEFAULT_SORT = "hot";

    // 音质类型
    public static final String MAGIC_PREFIX = "magic_";
    public static final String QUALITY_LITE = "lite";
    public static final String QUALITY_PIANO = "piano";
    public static final String QUALITY_ACAPPELLA = "acappella";
    public static final String QUALITY_SUBWOOFER = "subwoofer";
    public static final String QUALITY_ANCIENT = "ancient";
    public static final String QUALITY_DJ = "dj";
    public static final String QUALITY_SURNAY = "surnay";

    // 时间戳相关
    public static final long MILLIS_TO_SECONDS = 1000L;

    // 加密相关
    public static final String EMPTY_CODE = "";
    public static final String DEFAULT_QRCODE_TEXT_TEMPLATE = "https://h5.kugou.com/apps/loginQRCode/html/index.html?appid=%d&";

    // 响应常量
    public static final int HTTP_SUCCESS_CODE = 200;
    public static final int HTTP_ERROR_CODE = 500;

    // 歌词相关
    public static final String DEFAULT_LYRIC_CLIENT = "android";
    public static final String DEFAULT_LYRIC_FMT = "krc";
    public static final String DEFAULT_LYRIC_MAN = "no";
    public static final String CHARSET_UTF8 = "utf8";

    // 排序方式
    public static final String SORT_HOT = "hot";
    public static final String SORT_NEW = "new";

    // 业务类型
    public static final int BUSINESS_ID_MOBILE_CODE = 5;

    // 歌单类型
    public static final int PLAYLIST_TYPE_USER = 2;


    // 加密类型
    public static final String ENCRYPT_TYPE_ANDROID = "android";

    // 音频资源类型
    public static final String RESOURCE_TYPE_AUDIO = "audio";

    // 听歌记录类型
    public static final int LISTEN_TYPE_DEFAULT = 0;

    // 行为类型
    public static final String BEHAVIOR_PLAY = "play";

    // 签名相关
    public static final String DEFAULT_FROM_TRACK = "is_fromtrack";
    public static final String SIGN_KEY = "R6snCXJgbCaj9WFRJKefTMIFp0ey6Gza";
    public static final String SIGN_PARAMS_KEY = "OIlwieks28dk2k092lksi2UIkp";
    public static final String SIGN_CLOUD_KEY = "ebd1ac3134c880bda6a2194537843caa0162e2e7";
    public static final String SIGN_KEY_NORMAL = "57ae12eb6890223e355ccfcb74edf70d";

    // 页码ID
    public static final int PAGE_ID_MAIN = 151369488;
    public static final int PAGE_ID_LITE = 967177915;
    public static final String PPAGE_ID_MAIN = "463467626,350369493,788954147";
    public static final String PPAGE_ID_LITE = "356753938,823673182,967485191";

    // PID
    public static final int PID_MAIN = 2;
    public static final int PID_LITE = 411;
    public static final int PID_CLOUD = 20026;

    // 歌手关注来源
    public static final int SINGER_FOLLOW_SOURCE = 7;

    // 超时配置（秒）
    public static final int DEFAULT_TIMEOUT_SECONDS = 30;

    // 歌单版本
    public static final int PLAYLIST_TOTAL_VER = 979;

    // 请求路径常量
    public static final String PATH_GET_OTHER_LIST_FILE_NOFILT = "/pubsongs/v2/get_other_list_file_nofilt";

    // 客户端版本
    public static final String CLIENT_VERSION = "11083";
    public static final String CLIENT_VERSION_NEW = "12569";
    public static final String LITE_CLIENT_VERSION = "11040";

    // User Agent
    public static final String USER_AGENT_ANDROID = "Android15-1070-11083-46-0-DiscoveryDRADProtocol-wifi";

    // x-router 值
    public static final String X_ROUTER_SINGLE_CARD_REC = "singlecardrec.service.kugou.com";
    public static final String X_ROUTER_SPECIAL_REC = "specialrec.service.kugou.com";
    public static final String X_ROUTER_EVERYDAY_REC = "everydayrec.service.kugou.com";
    public static final String X_ROUTER_COMPLEX_SEARCH = "complexsearch.kugou.com";
    public static final String X_ROUTER_CLOUD_LIST = "cloudlist.service.kugou.com";

    // 客户端和MID常量
    public static final String CLIENT_ANDROID = "android";
    public static final String DEFAULT_MID = "default_mid_value";
    public static final String URL_DOMAIN = "kugou_url_domain";
    public static final String PATH_CLOUD_PLAY_SONG = "/v5/cloud_play_song";
    public static final String PATH_PLAY_SONG = "/v5/play_song";

    // VIP领取相关
    public static final String PATH_YOUTH_RECEIVE_VIP = "/youth/v1/recharge/receive_vip_listen_song";
    public static final int VIP_SOURCE_ID = 90137;

    // Youth VIP 领取接口
    public static final String PATH_YOUTH_AD_PLAY_REPORT = "/youth/v1/ad/play_report";
    public static final long YOUTH_VIP_AD_ID = 12307537187L;
    public static final long YOUTH_VIP_PLAY_DURATION_MS = 30000L;
}