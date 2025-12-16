package com.lanfunoe.gocache.service.auth.strategy;

import com.lanfunoe.gocache.constants.GocacheConstants;
import com.lanfunoe.gocache.util.EncryptionUtils;
import com.lanfunoe.gocache.util.WebClientRequestBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 密码认证策略
 * 处理用户名密码登录认证
 */
@Slf4j
@Component("passwordAuthStrategy")
@RequiredArgsConstructor
public class PasswordAuthStrategy implements AuthStrategy {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE_REF =
            new ParameterizedTypeReference<>() {};

    private final WebClientRequestBuilder webClientRequestBuilder;
    private final EncryptionUtils encryptionUtils;

    @Override
    public Mono<Map<String, Object>> authenticate(Map<String, Object> params) {
        if (!validateParams(params)) {
            return Mono.error(new IllegalArgumentException("密码认证参数不能为空"));
        }

        String username = (String) params.get("username");
        String password = (String) params.get("password");

        if (username == null || username.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("用户名不能为空"));
        }

        if (password == null || password.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("密码不能为空"));
        }

        long currentTime = System.currentTimeMillis();

        Map<String, Object> requestBody = encryptionUtils.buildLoginEncryptedParams(
                username, password, currentTime);

        Map<String, String> headers = new HashMap<>();
        headers.put(GocacheConstants.HEADER_X_ROUTER, GocacheConstants.LOGIN_USER_DOMAIN);

        return webClientRequestBuilder.sendPostRequest(
                webClientRequestBuilder.createDefaultWebClient(),
                GocacheConstants.PATH_LOGIN_BY_PWD,
                requestBody,
                null,
                headers,
                MAP_TYPE_REF
        );
    }

    @Override
    public String getStrategyType() {
        return "PASSWORD";
    }

    @Override
    public boolean validateParams(Map<String, Object> params) {
        if (!AuthStrategy.super.validateParams(params)) {
            return false;
        }

        return params.containsKey("username") && params.containsKey("password");
    }
}