package com.lanfunoe.gocache.service.common;

import com.lanfunoe.gocache.service.BaseGocacheService;
import com.lanfunoe.gocache.service.common.request.GetRequest;
import com.lanfunoe.gocache.service.common.request.PostRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 通用请求服务
 * 提供统一的HTTP请求发送能力，简化API调用
 */
@Slf4j
@Service
public class CommonService extends BaseGocacheService {


    // ========================= POST请求方法 =========================

    /**
     * 发送POST请求
     */
    public Mono<Map<String, Object>> post(PostRequest request) {
        return webClientRequestBuilder.sendPostRequest(
                webClientRequestBuilder.createDefaultWebClient(),
                request.getPath(),
                request.getPostBody(),
                request.getQueryParams(),
                request.getHeaders(),
                MAP_TYPE_REF
        );
    }


    /**
     * 发送POST请求（带默认参数和加密类型）
     */
    public Mono<Map<String, Object>> postWithDefaults(PostRequest request) {
        return webClientRequestBuilder.sendPostRequestWithDefaults(
                webClientRequestBuilder.createDefaultWebClient(),
                request.getPath(),
                request.getPostBody(),
                request.getQueryParams(),
                request.getHeaders(),
                request.getEncryptType(),
                MAP_TYPE_REF
        );
    }

    /**
     * 发送POST请求（带默认参数、用户认证和加密类型）
     */
    public Mono<Map<String, Object>> postWithDefaultsAndAuth(PostRequest request, String token, String userid) {
        return webClientRequestBuilder.sendPostRequestWithDefaultsAndAuth(
                webClientRequestBuilder.createDefaultWebClient(),
                request.getPath(),
                request.getPostBody(),
                request.getQueryParams(),
                request.getHeaders(),
                request.getEncryptType(),
                token,
                userid,
                MAP_TYPE_REF
        );
    }

    // ========================= GET请求方法 =========================

    /**
     * 发送GET请求
     */
    public Mono<Map<String, Object>> get(GetRequest request) {
        return webClientRequestBuilder.sendGetRequest(
                webClientRequestBuilder.createDefaultWebClient(),
                request.getPath(),
                request.getQueryParams(),
                request.getHeaders(),
                MAP_TYPE_REF
        );
    }

    /**
     * 发送GET请求（带默认参数和加密类型）
     */
    public Mono<Map<String, Object>> getWithDefaults(GetRequest request) {
        return webClientRequestBuilder.sendGetRequestWithDefaults(
                webClientRequestBuilder.createDefaultWebClient(),
                request.getPath(),
                request.getQueryParams(),
                request.getHeaders(),
                request.getEncryptType(),
                MAP_TYPE_REF
        );
    }

    /**
     * 发送GET请求（带默认参数、用户认证和加密类型）
     */
    public Mono<Map<String, Object>> getWithDefaultsAndAuth(GetRequest request, String token, String userid) {
        return webClientRequestBuilder.sendGetRequestWithDefaultsAndAuth(
                webClientRequestBuilder.createDefaultWebClient(),
                request.getPath(),
                request.getQueryParams(),
                request.getHeaders(),
                request.getEncryptType(),
                token,
                userid,
                MAP_TYPE_REF
        );
    }
}