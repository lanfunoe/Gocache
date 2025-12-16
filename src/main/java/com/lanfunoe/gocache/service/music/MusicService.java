package com.lanfunoe.gocache.service.music;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanfunoe.gocache.config.GocacheConfig;
import com.lanfunoe.gocache.constants.GocacheConstants;
import com.lanfunoe.gocache.model.UserSessionContext;
import com.lanfunoe.gocache.service.BaseGocacheService;
import com.lanfunoe.gocache.service.music.strategy.UrlStrategy;
import com.lanfunoe.gocache.util.CryptoUtils;
import com.lanfunoe.gocache.util.EncryptionUtils;
import com.lanfunoe.gocache.util.SignatureUtils;
import com.lanfunoe.gocache.util.WebClientRequestBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 音乐服务
 * 处理音乐相关的所有业务逻辑
 */
@Slf4j
@Service
public class MusicService extends BaseGocacheService {

    private final ObjectMapper objectMapper;
    private final UrlStrategy standardUrlStrategy;
    private final UrlStrategy cloudUrlStrategy;

    public MusicService(GocacheConfig gocacheConfig,
                             WebClientRequestBuilder webClientRequestBuilder,
                             EncryptionUtils encryptionUtils,
                             ObjectMapper objectMapper,
                             UrlStrategy standardUrlStrategy,
                             UrlStrategy cloudUrlStrategy) {
        super(gocacheConfig, webClientRequestBuilder, encryptionUtils);
        this.objectMapper = objectMapper;
        this.standardUrlStrategy = standardUrlStrategy;
        this.cloudUrlStrategy = cloudUrlStrategy;
    }


    /**
     * 获取云盘音乐播放地址
     *
     * @param request 云盘音乐URL请求
     * @return 播放地址
     */
    public Mono<Map<String, Object>> getCloudMusicUrl(CloudMusicUrlRequest request) {
        if (request == null || StringUtils.isBlank(request.getHash())) {
            throw new IllegalArgumentException("云盘音乐URL请求不能为空");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("hash", request.getHash());
        params.put("albumAudioId", request.getAlbumAudioId());
        params.put("audioId", request.getAudioId());
        params.put("name", request.getName());

        return cloudUrlStrategy.generateUrl(params);
    }

    /**
     * 获取音乐播放地址
     *
     * @param request 音乐URL请求
     * @param session 用户会话（userid/token）
     * @return 播放地址
     */
    public Mono<Map<String, Object>> getSongUrl(SongUrlRequest request, UserSessionContext session) {
        SongUrlContext context = buildSongUrlContext(request, session);
        Map<String, Object> queryParams = buildSongUrlQueryParams(context);
        Map<String, String> headers = buildSongUrlHeaders();

        // /song/url 复用默认参数与鉴权参数的合并逻辑，避免重复写 dfid/mid/uuid/appid/clientver/clienttime/userid/token
        return webClientRequestBuilder.sendGetRequestWithDefaultsAndAuth(
                webClientRequestBuilder.createDefaultWebClient(),
                GocacheConstants.PATH_SONG_URL,
                queryParams,
                headers,
                "android",
                context.token(),
                context.userId(),
                MAP_TYPE_REF
        );
    }

    /**
     * 获取音乐高潮部分时间点
     *
     * @param hash 音乐哈希值（支持多个，逗号分隔）
     * @return 高潮时间点信息
     */

    public Mono<Map<String, Object>> getSongClimax(String hash) {
        if (hash == null || hash.trim().isEmpty()) {
            throw new IllegalArgumentException("音乐哈希值不能为空");
        }

        String[] hashArray = hash.split(",");
        List<Map<String, String>> dataList = new ArrayList<>();

        for (String h : hashArray) {
            Map<String, String> item = new HashMap<>();
            item.put("hash", h.trim());
            dataList.add(item);
        }

        try {
            String dataJson = objectMapper.writeValueAsString(dataList);
            String encodedData = URLEncoder.encode(dataJson, StandardCharsets.UTF_8.toString());
            String absoluteUrl = GocacheConstants.EXPENDABLE_KMR_CDN_DOMAIN + GocacheConstants.PATH_AUDIO_CLIMAX
                    + "?data=" + encodedData;

            return webClientRequestBuilder.sendGetAbsoluteUrl(
                    absoluteUrl,
                    Map.of(
                            GocacheConstants.HEADER_USER_AGENT, GocacheConstants.USER_AGENT_ANDROID
                    ),
                    MAP_TYPE_REF
            );
        } catch (Exception e) {
            log.error("获取音乐高潮失败", e);
            return Mono.error(e);
        }
    }

    /**
     * 检查音乐播放权限
     *
     * @param hash    音乐哈希值
     * @param albumId 专辑ID
     * @return 播放权限信息
     */
    public Mono<Map<String, Object>> checkMusicPrivilege(String hash, String albumId) {
        String[] hashArray = hash.split(",");
        String[] albumIdArray = albumId != null ?
                albumId.split(",") : new String[hashArray.length];

        Map<String, Object> requestBody = encryptionUtils.buildMusicPrivilegeEncryptedParams(
                hashArray, albumIdArray,
                gocacheConfig.getAppid(),
                gocacheConfig.getClientver());

        Map<String, String> headers = WebClientRequestBuilder.buildHeadersWithRouter(
                GocacheConstants.MEDIA_STORE_DOMAIN);

        return webClientRequestBuilder.sendPostRequest(
                webClientRequestBuilder.createDefaultWebClient(),
                GocacheConstants.PATH_GET_RES_PRIVILEGE,
                requestBody,
                null,
                headers,
                MAP_TYPE_REF
        );
    }

    /**
     * 处理音质参数
     */
    private String processQuality(String quality) {
        if (quality == null) {
            return GocacheConstants.DEFAULT_QUALITY;
        }

        List<String> magicQualities = Arrays.asList(
                GocacheConstants.QUALITY_PIANO,
                GocacheConstants.QUALITY_ACAPPELLA,
                GocacheConstants.QUALITY_SUBWOOFER,
                GocacheConstants.QUALITY_ANCIENT,
                GocacheConstants.QUALITY_DJ,
                GocacheConstants.QUALITY_SURNAY
        );

        if (magicQualities.contains(quality)) {
            return GocacheConstants.MAGIC_PREFIX + quality;
        }

        return quality;
    }

    /**
     * 构建获取歌曲URL的查询参数
     */
    private Map<String, Object> buildSongUrlQueryParams(SongUrlContext context) {
        Map<String, Object> queryParams = new HashMap<>();

        queryParams.put("album_id", context.albumId());
        queryParams.put("area_code", 1);
        queryParams.put("hash", context.hashLower());
        queryParams.put("ssa_flag", GocacheConstants.DEFAULT_FROM_TRACK);
        queryParams.put("version", "11040");
        queryParams.put("page_id", context.pageId());
        queryParams.put("quality", context.quality());
        queryParams.put("album_audio_id", context.albumAudioId());
        queryParams.put("behavior", GocacheConstants.BEHAVIOR_PLAY);
        queryParams.put("pid", context.pid());
        queryParams.put("cmd", 26);
        queryParams.put("pidversion", 3001);
        queryParams.put("IsFreePart", context.freePart());
        queryParams.put("ppage_id", context.ppageId());
        queryParams.put("cdnBackup", 1);
        queryParams.put("kcard", 0);
        queryParams.put("module", "");
        // /song/url 仍需显式传 key（默认参数会由 WebClientRequestBuilder 合并后参与签名）
        queryParams.put("key", context.key());

        return queryParams;
    }

    private Map<String, String> buildSongUrlHeaders() {
        return WebClientRequestBuilder.buildHeadersWithRouter(GocacheConstants.TRACKER_CDN_DOMAIN);
    }

    private SongUrlContext buildSongUrlContext(SongUrlRequest request, UserSessionContext session) {
        requireNonNull(request, "SongUrlRequest");
        requireNonNull(session, "UserSessionContext");
        requireNonNull(session.userId(), "userid");
        requireNonNull(session.token(), "token");

        boolean isLite = gocacheConfig.isLite();

        String hashLower = request.getHash() != null ? request.getHash().toLowerCase() : "";
        String quality = processQuality(request.getQuality());

        long albumId = request.getAlbumId() != null ? Long.parseLong(request.getAlbumId()) : GocacheConstants.DEFAULT_ALBUM_ID;
        long albumAudioId = request.getAlbumAudioId() != null ? Long.parseLong(request.getAlbumAudioId()) : GocacheConstants.DEFAULT_ALBUM_AUDIO_ID;

        int freePart = (request.getFreePart() != null && request.getFreePart()) ? 1 : 0;

        // 默认参数中的 mid 计算方式与 DefaultParamsBuilder 保持一致：dfid 固定为 "-"
        String mid = CryptoUtils.md5("-");
        String key = SignatureUtils.signKey(hashLower, mid, session.userId(), String.valueOf(gocacheConfig.getAppid()));

        return new SongUrlContext(
                hashLower,
                albumId,
                albumAudioId,
                quality,
                freePart,
                isLite ? GocacheConstants.PAGE_ID_LITE : GocacheConstants.PAGE_ID_MAIN,
                isLite ? GocacheConstants.PID_LITE : GocacheConstants.PID_MAIN,
                isLite ? GocacheConstants.PPAGE_ID_LITE : GocacheConstants.PPAGE_ID_MAIN,
                session.userId(),
                session.token(),
                key
        );
    }

    private record SongUrlContext(
            String hashLower,
            long albumId,
            long albumAudioId,
            String quality,
            int freePart,
            int pageId,
            int pid,
            String ppageId,
            String userId,
            String token,
            String key
    ) {
    }


    @Getter
    public static class CloudMusicUrlRequest {
        private final String hash;
        private final String albumAudioId;
        private final String audioId;
        private final String name;

        public CloudMusicUrlRequest(String hash, String albumAudioId, String audioId, String name) {
            this.hash = hash;
            this.albumAudioId = albumAudioId;
            this.audioId = audioId;
            this.name = name;
        }

    }
    @Getter
    public static class SongUrlRequest {
        private final String hash;
        private final String albumId;
        private final String albumAudioId;
        private final String quality;
        private final Boolean freePart;

        public SongUrlRequest(String hash, String albumId, String albumAudioId, String quality, Boolean freePart) {
            this.hash = hash;
            this.albumId = albumId;
            this.albumAudioId = albumAudioId;
            this.quality = quality;
            this.freePart = freePart;
        }
    }

}