package com.lanfunoe.gocache.service.user;

import com.lanfunoe.gocache.config.GocacheConfig;
import com.lanfunoe.gocache.constants.GocacheConstants;
import com.lanfunoe.gocache.service.BaseGocacheService;
import com.lanfunoe.gocache.util.EncryptionUtils;
import com.lanfunoe.gocache.util.WebClientRequestBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息服务
 * 处理用户基本信息相关的业务逻辑
 */
@Slf4j
@Service
public class UserInfoService extends BaseGocacheService {

    public UserInfoService(GocacheConfig gocacheConfig,
                                WebClientRequestBuilder webClientRequestBuilder,
                                EncryptionUtils encryptionUtils) {
        super(gocacheConfig, webClientRequestBuilder, encryptionUtils);
    }

    /**
     * 获取用户详情
     *
     * @param token  用户token
     * @param userid 用户ID
     * @return 用户详情
     */
    public Mono<Map<String, Object>> getUserDetail(String token, String userid) {
        if (StringUtils.isBlank(token) || StringUtils.isBlank(userid)) {
            throw new IllegalArgumentException("token and userid cannot be empty");
        }
        long clientTime = System.currentTimeMillis() / GocacheConstants.MILLIS_TO_SECONDS;
        String requestBodyJson = buildUserDetailBodyJson(token, userid, clientTime);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("plat", GocacheConstants.PLATFORM_PHONE);
        queryParams.put("clienttime", clientTime);

        Map<String, String> headers = WebClientRequestBuilder.buildHeadersWithRouter(
                GocacheConstants.USERCENTER_DOMAIN);

        return webClientRequestBuilder.sendPostRequestWithDefaultsAndAuth(
                webClientRequestBuilder.createDefaultWebClient(),
                GocacheConstants.PATH_GET_MY_INFO,
                requestBodyJson,
                queryParams,
                headers,
                "android",
                token,
                userid,
                MAP_TYPE_REF
        );
    }

    /**
     * 获取用户关注列表
     *
     * @param token  用户token
     * @param userId 用户ID
     * @return 关注列表
     */
    public Mono<Map<String, Object>> getUserFollow(String token, String userId) {
        if (StringUtils.isBlank(token) || StringUtils.isBlank(userId)) {
            throw new IllegalArgumentException("token and userid cannot be empty");
        }
        long dateTime = System.currentTimeMillis() / GocacheConstants.MILLIS_TO_SECONDS;

        Map<String, Object> requestBody = buildUserFollowRequestBody(token, userId, dateTime);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("plat", GocacheConstants.PLATFORM_PHONE);

        Map<String, String> headers = WebClientRequestBuilder.buildHeadersWithRouter(
                GocacheConstants.RELATION_USER_DOMAIN);

        return webClientRequestBuilder.sendPostRequestWithDefaultsAndAuth(
                webClientRequestBuilder.createDefaultWebClient(),
                GocacheConstants.PATH_FOLLOW_LIST,
                requestBody,
                queryParams,
                headers,
                "android",
                token,
                userId,
                MAP_TYPE_REF
        );
    }

    private String buildUserDetailBodyJson(String token, String userid, long clientTimeSeconds) {
        String p = encryptionUtils.encryptRsaNoPaddingTokenClienttime(clientTimeSeconds, token);
        long userIdLong = Long.parseLong(userid);
        return String.format("{\"visit_time\":%d,\"usertype\":1,\"p\":\"%s\",\"userid\":%d}",
                clientTimeSeconds, p, userIdLong);
    }

    private Map<String, Object> buildUserFollowRequestBody(String token, String userId, long clientTimeSeconds) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("merge", 2);
        requestBody.put("need_iden_type", 1);
        requestBody.put("ext_params", "k_pic,jumptype,singerid,score");
        requestBody.put("userid", userId);
        requestBody.put("type", 0);
        requestBody.put("id_type", 0);
        requestBody.put("p", encryptionUtils.encryptRsaNoPaddingClienttimeToken(clientTimeSeconds, token));
        return requestBody;
    }
}