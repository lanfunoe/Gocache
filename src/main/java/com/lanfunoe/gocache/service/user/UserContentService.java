package com.lanfunoe.gocache.service.user;

import com.lanfunoe.gocache.config.GocacheConfig;
import com.lanfunoe.gocache.constants.GocacheConstants;
import com.lanfunoe.gocache.service.BaseGocacheService;
import com.lanfunoe.gocache.util.CryptoUtils;
import com.lanfunoe.gocache.util.EncryptionUtils;
import com.lanfunoe.gocache.util.WebClientRequestBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户内容服务
 * 处理用户内容相关的业务逻辑（听歌记录、歌单、云盘）
 */
@Slf4j
@Service
public class UserContentService extends BaseGocacheService {

    public UserContentService(GocacheConfig gocacheConfig,
                                   WebClientRequestBuilder webClientRequestBuilder,
                                   EncryptionUtils encryptionUtils) {
        super(gocacheConfig, webClientRequestBuilder, encryptionUtils);
    }

    /**
     * 获取听歌记录
     *
     * @param token  用户token
     * @param userid 用户ID
     * @param type   记录类型
     * @return 听歌记录
     */
    public Mono<Map<String, Object>> getListenHistory(String token, String userid, Integer type) {
        if (StringUtils.isBlank(token) || StringUtils.isBlank(userid)) {
            throw new IllegalArgumentException("token and userid cannot be empty");
        }
        long clientTime = System.currentTimeMillis() / GocacheConstants.MILLIS_TO_SECONDS;

        Map<String, Object> requestBody = buildListenHistoryRequestBody(token, userid, type, clientTime);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("plat", GocacheConstants.PLATFORM_LITE);

        return webClientRequestBuilder.sendPostRequestWithDefaultsAndAuth(
                webClientRequestBuilder.createWebClient(GocacheConstants.LISTEN_SERVICE_DOMAIN),
                GocacheConstants.PATH_GET_LIST,
                requestBody,
                queryParams,
                null,
                "android",
                token,
                userid,
                MAP_TYPE_REF
        );
    }

    /**
     * 获取用户歌单
     *
     * @param request 歌单请求
     * @return 用户歌单
     */
    public Mono<Map<String, Object>> getUserPlaylist(UserPlaylistRequest request) {
        Map<String, Object> requestBody = encryptionUtils.buildUserPlaylistEncryptedParams(
                request.getToken(), request.getUserid(), request.getPage(), request.getPagesize());

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("plat", GocacheConstants.PLATFORM_PHONE);
        queryParams.put("userid", Long.parseLong(request.getUserid()));
        queryParams.put("token", request.getToken());

        Map<String, String> headers = WebClientRequestBuilder.buildHeadersWithRouter(
                GocacheConstants.CLOUD_LIST_DOMAIN);

        return webClientRequestBuilder.sendPostRequestWithDefaults(
                webClientRequestBuilder.createDefaultWebClient(),
                GocacheConstants.PATH_GET_ALL_LIST,
                requestBody,
                queryParams,
                headers,
                "android",
                MAP_TYPE_REF
        );
    }

    /**
     * 获取用户云盘音乐
     *
     * @param request 云盘音乐请求
     * @return 云盘音乐列表
     */
    public Mono<Map<String, Object>> getUserCloud(UserCloudRequest request) {
        EncryptionUtils.CloudEncryptionResult encryptionResult =
                encryptionUtils.buildUserCloudEncryptedParams(
                        request.getToken(), request.getUserid(), request.getDfid(),
                        request.getPage(), request.getPagesize());

        encryptionResult.getQueryParams().put("clientver", gocacheConfig.getClientver());
        encryptionResult.getQueryParams().put("appid", gocacheConfig.getAppid());
        encryptionResult.getQueryParams().put("p", encryptionResult.getEncryptedRsa());

        return webClientRequestBuilder.sendPostRequestBinary(
                webClientRequestBuilder.createWebClient(GocacheConstants.MCLOUD_SERVICE_DOMAIN),
                GocacheConstants.PATH_GET_CLOUD_LIST,
                java.util.Base64.getDecoder().decode(encryptionResult.getEncryptedData()),
                encryptionResult.getQueryParams(),
                null,
                byte[].class
        ).map(responseBody -> {
            try {
                String decryptedData = CryptoUtils.playlistAesDecrypt(
                        java.util.Base64.getEncoder().encodeToString(responseBody),
                        encryptionResult.getEncryptedKey()
                );
                Map<String, Object> result = new HashMap<>();
                result.put("data", decryptedData);
                return result;
            } catch (Exception e) {
                log.error("云盘音乐解密失败", e);
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("error", e.getMessage());
                return errorResult;
            }
        });
    }

    private Map<String, Object> buildListenHistoryRequestBody(String token, String userid, Integer type, long clientTime) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("t_userid", userid);
        requestBody.put("userid", userid);
        requestBody.put("list_type", type != null ? type : GocacheConstants.LISTEN_TYPE_DEFAULT);
        requestBody.put("area_code", 1);
        requestBody.put("cover", 2);
        requestBody.put("p", encryptionUtils.encryptRsaNoPaddingClienttimeToken(clientTime, token));
        return requestBody;
    }

    // 请求参数对象
    @Getter
    @RequiredArgsConstructor
    public static class UserPlaylistRequest {
        private final String token;
        private final String userid;
        private final Integer page;
        private final Integer pagesize;
    }

    @Getter
    @RequiredArgsConstructor
    public static class UserCloudRequest {
        private final String token;
        private final String userid;
        private final String dfid;
        private final Integer page;
        private final Integer pagesize;
    }
}