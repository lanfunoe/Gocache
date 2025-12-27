package com.lanfunoe.gocache.service.user;

import com.lanfunoe.gocache.constants.GocacheConstants;
import com.lanfunoe.gocache.service.BaseGocacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * VIP服务
 * 处理VIP相关的业务逻辑
 */
@Slf4j
@Service
public class VipService extends BaseGocacheService {


    /**
     * 获取VIP详情
     *
     * @param token  用户token
     * @param userid 用户ID
     * @return VIP详情
     */
    public Mono<Map<String, Object>> getVipDetail(String token, String userid) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("busi_type", "concept");
        return webClientRequestBuilder.sendGetRequestWithDefaultsAndAuth(
                webClientRequestBuilder.createWebClient(GocacheConstants.VIP_DOMAIN),
                GocacheConstants.PATH_GET_UNION_VIP,
                queryParams,
                null,
                "android",
                token,
                userid,
                MAP_TYPE_REF
        );
    }

    /**
     * 领取每日VIP (一天)
     * 需要登录
     *
     * @param token  用户token
     * @param userid 用户ID
     * @return 领取结果
     */
    public Mono<Map<String, Object>> receiveDayVip(String token, String userid) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("source_id", GocacheConstants.VIP_SOURCE_ID);
        return webClientRequestBuilder.sendPostRequestWithDefaultsAndAuth(
                webClientRequestBuilder.createDefaultWebClient(),
                GocacheConstants.PATH_YOUTH_RECEIVE_VIP,
                null,
                queryParams,
                null,
                "android",
                token,
                userid,
                MAP_TYPE_REF
        );
    }

    /**
     * 领取Youth VIP (通过广告)
     * 需要登录
     *
     * @param token  用户token
     * @param userid 用户ID
     * @return 领取结果
     */
    public Mono<Map<String, Object>> receiveYouthVip(String token, String userid) {
        long currentTime = System.currentTimeMillis();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("ad_id", GocacheConstants.YOUTH_VIP_AD_ID);
        requestBody.put("play_end", currentTime);
        requestBody.put("play_start", currentTime - GocacheConstants.YOUTH_VIP_PLAY_DURATION_MS);

        return webClientRequestBuilder.sendPostRequestWithDefaultsAndAuth(
                webClientRequestBuilder.createDefaultWebClient(),
                GocacheConstants.PATH_YOUTH_AD_PLAY_REPORT,
                requestBody,
                null,
                null,
                "android",
                token,
                userid,
                MAP_TYPE_REF
        );
    }
}