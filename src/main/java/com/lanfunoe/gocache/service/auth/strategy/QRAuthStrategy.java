package com.lanfunoe.gocache.service.auth.strategy;

import com.lanfunoe.gocache.config.GocacheApiConfig;
import com.lanfunoe.gocache.constants.GocacheConstants;
import com.lanfunoe.gocache.util.WebClientRequestBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 二维码认证策略
 * 处理二维码登录认证
 */
@Slf4j
@Component("qrAuthStrategy")
@RequiredArgsConstructor
public class QRAuthStrategy implements AuthStrategy {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE_REF =
            new ParameterizedTypeReference<>() {};

    private final WebClientRequestBuilder webClientRequestBuilder;
    private final GocacheApiConfig gocacheApiConfig;

    @Override
    public Mono<Map<String, Object>> authenticate(Map<String, Object> params) {
        if (!validateParams(params)) {
            return Mono.error(new IllegalArgumentException("二维码认证参数不能为空"));
        }

        String key = (String) params.get("key");
        if (key == null || key.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("二维码key不能为空"));
        }

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("plat", GocacheConstants.PLATFORM_WEB);
        queryParams.put("appid", gocacheApiConfig.getAppid());
        queryParams.put("srcappid", gocacheApiConfig.getSrcappid());
        queryParams.put("qrcode", key);

        return webClientRequestBuilder.sendGetRequestWithDefaults(
                webClientRequestBuilder.createWebClient(GocacheConstants.LOGIN_USER_URL),
                GocacheConstants.PATH_GET_USERINFO_QRCODE,
                queryParams,
                null,
                "web",
                MAP_TYPE_REF
        );
    }

    @Override
    public String getStrategyType() {
        return "QR";
    }

    @Override
    public boolean validateParams(Map<String, Object> params) {
        if (!AuthStrategy.super.validateParams(params)) {
            return false;
        }

        return params.containsKey("key");
    }
}