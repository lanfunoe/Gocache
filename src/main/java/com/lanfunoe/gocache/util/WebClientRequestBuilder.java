package com.lanfunoe.gocache.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanfunoe.gocache.config.GocacheConfig;
import com.lanfunoe.gocache.constants.GocacheConstants;
import com.lanfunoe.gocache.filter.WebClientLoggingFilter;
import com.lanfunoe.gocache.model.UserSessionContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebClient请求构建器
 * 统一处理WebClient的构建和请求发送
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebClientRequestBuilder {

    private final WebClientLoggingFilter loggingFilter;
    private final WebClient.Builder webClientBuilder;
    private final DefaultParamsBuilder defaultParamsBuilder;
    private final ObjectMapper objectMapper;
    private final GocacheConfig gocacheConfig;

    // WebClient 缓存，避免重复创建
    private final Map<String, WebClient> webClientCache = new ConcurrentHashMap<>();

    // ========================= WebClient 创建方法 =========================

    /**
     * 获取或创建WebClient实例（带缓存）
     * WebClient 是线程安全且可复用的，无需每次创建新实例
     */
    public WebClient createWebClient(String baseUrl) {
        return webClientCache.computeIfAbsent(baseUrl, url ->
                webClientBuilder.clone()
                        .baseUrl(url)
                        .filter(loggingFilter.loggingFilter())
                        .build()
        );
    }

    /**
     * 创建默认的WebClient实例（使用网关URL）
     */
    public WebClient createDefaultWebClient() {
        return createWebClient(GocacheConstants.API_GATEWAY_URL);
    }

    // ========================= GET请求方法 =========================

    /**
     * 发送GET请求
     *
     * @param path          请求路径
     * @param queryParams   查询参数
     * @param headers       请求头
     * @param encryptType   加密类型 (null/"web"/"android")
     * @param typeReference 响应类型引用
     */
    public <T> Mono<T> sendGetRequest(String path,
                                      Map<String, Object> queryParams,
                                      Map<String, String> headers,
                                      String encryptType,
                                      ParameterizedTypeReference<T> typeReference) {
        return sendGetRequest(createDefaultWebClient(), path, queryParams, headers, encryptType, typeReference);
    }

    /**
     * 发送GET请求（指定WebClient）
     */
    public <T> Mono<T> sendGetRequest(WebClient webClient, String path,
                                      Map<String, Object> queryParams,
                                      Map<String, String> headers,
                                      String encryptType,
                                      ParameterizedTypeReference<T> typeReference) {
        Map<String, Object> signedParams = applySignature(queryParams, encryptType);

        var requestBuilder = webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, path, signedParams, true));

        applyHeaders(requestBuilder, headers, signedParams);
        return executeWithJsonFallback(requestBuilder, typeReference);
    }


    /**
     * 发送GET请求（无加密类型）
     */
    public <T> Mono<T> sendGetRequest(WebClient webClient, String path,
                                      Map<String, Object> queryParams,
                                      Map<String, String> headers,
                                      ParameterizedTypeReference<T> typeReference) {
        Map<String, Object> finalParams = queryParams != null ? new HashMap<>(queryParams) : new HashMap<>();
        finalParams.remove("signature");

        var requestBuilder = webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, path, finalParams, true));

        applyHeaders(requestBuilder, headers, finalParams);
        return requestBuilder
                .retrieve()
                .bodyToMono(typeReference)
                .timeout(Duration.ofSeconds(GocacheConstants.DEFAULT_TIMEOUT_SECONDS));
    }

    /**
     * 发送GET请求（带默认参数和加密类型）
     */
    public <T> Mono<T> sendGetRequestWithDefaults(WebClient webClient, String path,
                                                  Map<String, Object> queryParams,
                                                  Map<String, String> headers,
                                                  String encryptType,
                                                  ParameterizedTypeReference<T> typeReference) {
        Map<String, Object> mergedParams = defaultParamsBuilder.mergeWithDefaultParams(queryParams);
        Map<String, Object> signedParams = applySignature(mergedParams, encryptType);

        var requestBuilder = webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, path, signedParams, false));

        applyHeaders(requestBuilder, headers, signedParams);

        return executeWithJsonFallback(requestBuilder, typeReference);
    }

    /**
     * 发送GET请求（带默认参数和用户认证）
     */
    public <T> Mono<T> sendGetRequestWithDefaultsAndAuth(WebClient webClient, String path,
                                                         Map<String, Object> queryParams,
                                                         Map<String, String> headers,
                                                         String encryptType,
                                                         UserSessionContext session,
                                                         ParameterizedTypeReference<T> typeReference) {
        Map<String, Object> mergedParams = defaultParamsBuilder.mergeWithDefaultParamsWithAuth(queryParams, session);
        Map<String, Object> signedParams = applySignature(mergedParams, encryptType);

        var requestBuilder = webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, path, signedParams, false));

        applyHeaders(requestBuilder, headers, signedParams);

        return executeWithJsonFallback(requestBuilder, typeReference);
    }

    /**
     * 使用绝对URL发送GET请求（绕过UriComponents模板解析）
     */
    public <T> Mono<T> sendGetAbsoluteUrl(String absoluteUrl,
                                          Map<String, String> headers,
                                          ParameterizedTypeReference<T> typeReference) {
        var requestBuilder = webClientBuilder.build().get().uri(URI.create(absoluteUrl));
        applyHeaders(requestBuilder, headers, null);

        return requestBuilder
                .retrieve()
                .bodyToMono(typeReference)
                .timeout(Duration.ofSeconds(GocacheConstants.DEFAULT_TIMEOUT_SECONDS));
    }

    // ========================= POST请求方法 =========================

    /**
     * 发送POST请求
     */
    public <T> Mono<T> sendPostRequest(WebClient webClient, String path,
                                       Object body,
                                       Map<String, Object> queryParams,
                                       Map<String, String> headers,
                                       ParameterizedTypeReference<T> typeReference) {
        var requestBuilder = webClient.post()
                .uri(uriBuilder -> buildUri(uriBuilder, path, queryParams, false))
                .contentType(MediaType.APPLICATION_JSON);

        applyHeaders(requestBuilder, headers, queryParams);

        // 将 body 存储为 attribute，供日志过滤器使用
        if (body != null) {
            requestBuilder.attribute(WebClientLoggingFilter.REQUEST_BODY_ATTR, body);
        }

        return requestBuilder
                .bodyValue(body)
                .retrieve()
                .bodyToMono(typeReference)
                .timeout(Duration.ofSeconds(GocacheConstants.DEFAULT_TIMEOUT_SECONDS));
    }

    /**
     * 发送POST请求（带默认参数和加密类型）
     */
    public <T> Mono<T> sendPostRequestWithDefaults(WebClient webClient, String path,
                                                   Object body,
                                                   Map<String, Object> queryParams,
                                                   Map<String, String> headers,
                                                   String encryptType,
                                                   ParameterizedTypeReference<T> typeReference) {
        Map<String, Object> mergedParams = defaultParamsBuilder.mergeWithDefaultParams(queryParams);
        Map<String, Object> signedParams = applySignature(mergedParams, encryptType, body);

        var requestBuilder = webClient.post()
                .uri(uriBuilder -> buildUri(uriBuilder, path, signedParams, false))
                .contentType(MediaType.APPLICATION_JSON);

        applyHeaders(requestBuilder, headers, signedParams);

        // 将 body 存储为 attribute，供日志过滤器使用
        if (body != null) {
            requestBuilder.attribute(WebClientLoggingFilter.REQUEST_BODY_ATTR, body);
        }

        return requestBuilder
                .bodyValue(body)
                .retrieve()
                .bodyToMono(typeReference)
                .timeout(Duration.ofSeconds(GocacheConstants.DEFAULT_TIMEOUT_SECONDS));
    }

    /**
     * 发送POST请求（带默认参数和用户认证）
     */
    public <T> Mono<T> sendPostRequestWithDefaultsAndAuth(WebClient webClient, String path,
                                                          Object body,
                                                          Map<String, Object> queryParams,
                                                          Map<String, String> headers,
                                                          String encryptType,
                                                          UserSessionContext session,
                                                          ParameterizedTypeReference<T> typeReference) {
        Map<String, Object> mergedParams = defaultParamsBuilder.mergeWithDefaultParamsWithAuth(queryParams, session);
        Map<String, Object> signedParams = applySignature(mergedParams, encryptType, body);

        var requestBuilder = webClient.post()
                .uri(uriBuilder -> buildUri(uriBuilder, path, signedParams, false))
                .contentType(MediaType.APPLICATION_JSON);

        applyHeaders(requestBuilder, headers, signedParams);

        // 将 body 存储为 attribute，供日志过滤器使用
        if (body != null) {
            requestBuilder.attribute(WebClientLoggingFilter.REQUEST_BODY_ATTR, body);
        }

        Mono<T> result;
        if (body != null) {
            result = requestBuilder
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(typeReference);
        } else {
            result = requestBuilder
                    .retrieve()
                    .bodyToMono(typeReference);
        }

        return result.timeout(Duration.ofSeconds(GocacheConstants.DEFAULT_TIMEOUT_SECONDS));
    }

    /**
     * 发送POST请求（二进制数据）
     */
    public <T> Mono<T> sendPostRequestBinary(WebClient webClient, String path,
                                             byte[] body,
                                             Map<String, Object> queryParams,
                                             Map<String, String> headers,
                                             Class<T> responseType) {
        var requestBuilder = webClient.post()
                .uri(uriBuilder -> buildUri(uriBuilder, path, queryParams, false))
                .contentType(MediaType.APPLICATION_OCTET_STREAM);

        applyHeaders(requestBuilder, headers, queryParams);

        // 将二进制 body 长度存储为 attribute（避免打印大量二进制数据）
        if (body != null) {
            requestBuilder.attribute(WebClientLoggingFilter.REQUEST_BODY_ATTR, "[binary data, length=" + body.length + "]");
        }

        return requestBuilder
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType)
                .timeout(Duration.ofSeconds(GocacheConstants.DEFAULT_TIMEOUT_SECONDS));
    }

    // ========================= Header构建静态方法 =========================

    /**
     * 构建带有x-router头的请求头Map
     */
    public static Map<String, String> buildHeadersWithRouter(String routerDomain) {
        Map<String, String> headers = new HashMap<>();
        headers.put(GocacheConstants.HEADER_X_ROUTER, routerDomain);
        return headers;
    }

    /**
     * 构建带有x-router和kg-tid头的请求头Map
     */
    public static Map<String, String> buildHeadersWithRouterAndKgTid(String routerDomain, String kgTid) {
        Map<String, String> headers = buildHeadersWithRouter(routerDomain);
        headers.put(GocacheConstants.HEADER_KG_TID, kgTid);
        return headers;
    }

    // ========================= 私有辅助方法 =========================

    /**
     * 应用签名到参数Map（不包含body数据）
     */
    private Map<String, Object> applySignature(Map<String, Object> params, String encryptType) {
        return applySignature(params, encryptType, null);
    }

    /**
     * 应用签名到参数Map（包含body数据用于签名计算）
     *
     * @param params      查询参数
     * @param encryptType 加密类型
     * @param body        POST请求体（用于Android签名计算）
     */
    private Map<String, Object> applySignature(Map<String, Object> params, String encryptType, Object body) {
        if (encryptType == null || params == null) {
            return params;
        }

        Map<String, Object> signedParams = new HashMap<>(params);
        String bodyData = serializeBodyForSignature(body);

        String signature = switch (encryptType) {
            case "web" -> SignatureUtils.signatureWebParams(signedParams);
            case "android" -> SignatureUtils.signatureAndroidParams(signedParams, bodyData);
            default -> null;
        };

        if (signature != null) {
            signedParams.put("signature", signature);
        }
        return signedParams;
    }

    /**
     * 将body对象序列化为字符串用于签名计算
     */
    private String serializeBodyForSignature(Object body) {
        if (body == null) {
            return "";
        }
        if (body instanceof String) {
            return (String) body;
        }
        try {
            return objectMapper.writeValueAsString(body);
        } catch (Exception e) {
            log.warn("Failed to serialize body for signature: {}", e.getMessage());
            return "";
        }
    }

    /**
     * 构建URI（统一的URI构建逻辑）
     *
     * @param serializeComplex 是否序列化复杂类型（Map/Iterable）
     */
    private URI buildUri(UriBuilder uriBuilder, String path, Map<String, Object> queryParams, boolean serializeComplex) {
        uriBuilder.path(path);
        if (queryParams != null) {
            queryParams.forEach((key, value) -> {
                if (value != null) {
                    if (serializeComplex && (value instanceof Map || value instanceof Iterable)) {
                        try {
                            uriBuilder.queryParam(key, objectMapper.writeValueAsString(value));
                        } catch (Exception e) {
                            uriBuilder.queryParam(key, String.valueOf(value));
                        }
                    } else {
                        uriBuilder.queryParam(key, value);
                    }
                }
            });
        }
        return uriBuilder.build();
    }

    /**
     * 应用请求头
     */
    private void applyHeaders(WebClient.RequestHeadersSpec<?> requestBuilder, Map<String, String> headers, Map<String, Object> queryParams) {
        Map<String, String> resHeaders = buildDefaultHeaders(queryParams);
        if (headers != null) {
            resHeaders.putAll(headers);
        }
        resHeaders.forEach(requestBuilder::header);
    }

    /**
     * 构建默认请求头
     */
    private Map<String, String> buildDefaultHeaders(Map<String, Object> queryParams) {

        Map<String, String> defaultHeaders = new HashMap<>();
        defaultHeaders.put("User-Agent", gocacheConfig.getUserAgent());

        if (queryParams != null) {
            addHeaderIfPresent(defaultHeaders, queryParams, "dfid");
            addHeaderIfPresent(defaultHeaders, queryParams, "mid");
            addHeaderIfPresent(defaultHeaders, queryParams, "clienttime");
        }

        defaultHeaders.put("X-Real-IP", "127.0.0.1");
        defaultHeaders.put("X-Forwarded-For", "127.0.0.1");
        return defaultHeaders;
    }

    private void addHeaderIfPresent(Map<String, String> headers, Map<String, Object> params, String key) {
        Object value = params.get(key);
        if (value != null) {
            headers.put(key, value.toString());
        }
    }

    /**
     * 执行请求并支持JSON回退解析
     */
    private <T> Mono<T> executeWithJsonFallback(WebClient.RequestHeadersSpec<?> requestBuilder,
                                                ParameterizedTypeReference<T> typeReference) {
        return requestBuilder
                .exchangeToMono(clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(bodyString -> {
                                    try {
                                        JavaType javaType = objectMapper.getTypeFactory().constructType(typeReference.getType());
                                        T result = objectMapper.readValue(bodyString, javaType);
                                        return Mono.just(result);
                                    } catch (Exception ex) {
                                        return handleParseError(bodyString, typeReference, ex);
                                    }
                                })
                )
                .timeout(Duration.ofSeconds(GocacheConstants.DEFAULT_TIMEOUT_SECONDS));
    }

    @SuppressWarnings("unchecked")
    private <T> Mono<T> handleParseError(String bodyString, ParameterizedTypeReference<T> typeReference, Exception ex) {
        try {
            String typeName = typeReference.getType().getTypeName();
            if (typeName != null && typeName.contains("Map")) {
                return Mono.just((T) Map.of("raw", bodyString));
            }
        } catch (Exception ignore) {
        }
        return Mono.error(ex);
    }
}
