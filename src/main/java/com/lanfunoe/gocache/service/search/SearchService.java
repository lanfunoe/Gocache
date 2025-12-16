package com.lanfunoe.gocache.service.search;

import com.lanfunoe.gocache.config.GocacheConfig;
import com.lanfunoe.gocache.constants.GocacheConstants;
import com.lanfunoe.gocache.service.BaseGocacheService;
import com.lanfunoe.gocache.service.common.CommonService;
import com.lanfunoe.gocache.service.common.request.GetRequest;
import com.lanfunoe.gocache.util.EncryptionUtils;
import com.lanfunoe.gocache.util.WebClientRequestBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索服务
 * 处理搜索相关的业务逻辑
 */
@Slf4j
@Service
public class SearchService extends BaseGocacheService {

    private static final List<String> VALID_SEARCH_TYPES = Arrays.asList("special", "lyric", "song", "album", "author", "mv");

    private final CommonService commonService;

    public SearchService(GocacheConfig gocacheConfig,
                              WebClientRequestBuilder webClientRequestBuilder,
                              EncryptionUtils encryptionUtils,
                              CommonService commonService) {
        super(gocacheConfig, webClientRequestBuilder, encryptionUtils);
        this.commonService = commonService;
    }

    /**
     * 搜索音乐、歌手等
     *
     * @param request 搜索请求
     * @return 搜索结果
     */
    public Mono<Map<String, Object>> search(SearchRequest request) {
        // 解析cookie获取userid和token
        AuthInfo authInfo = parseCookie(request.getCookie());

        // 构建请求数据
        Map<String, Object> requestData = buildSearchRequestData(request);

        // 查询参数
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("keyword", request.getKeywords() != null ? request.getKeywords() : "");
        queryParams.put("page", request.getPage());
        queryParams.put("pagesize", request.getPagesize());

        // 确定API路径
        String apiPath = determineApiPath(request.getType());

        // 构建请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("x-router", "complexsearch.kugou.com");
        headers.put(GocacheConstants.HEADER_USER_AGENT, GocacheConstants.USER_AGENT_ANDROID);
        if (request.getCookie() != null) {
            headers.put("Cookie", request.getCookie());
        }

        GetRequest getRequest = new GetRequest(apiPath, queryParams, headers);
        return commonService.getWithDefaultsAndAuth(getRequest, authInfo.token(), authInfo.userid());
    }

    /**
     * 解析Cookie获取认证信息
     */
    private AuthInfo parseCookie(String cookie) {
        String userid = "0";
        String token = "";
        if (cookie != null && !cookie.isEmpty()) {
            String[] cookieParts = cookie.split(";");
            for (String part : cookieParts) {
                String[] keyValue = part.trim().split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    if ("userid".equals(key)) {
                        userid = value;
                    } else if ("token".equals(key)) {
                        token = value;
                    }
                }
            }
        }
        return new AuthInfo(token, userid);
    }

    /**
     * 构建搜索请求数据
     */
    private Map<String, Object> buildSearchRequestData(SearchRequest request) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("albumhide", 0);
        requestData.put("iscorrection", 1);
        requestData.put("keyword", request.getKeywords() != null ? request.getKeywords() : "");
        requestData.put("nocollect", 0);
        requestData.put("page", request.getPage());
        requestData.put("pagesize", request.getPagesize());
        requestData.put("platform", "AndroidFilter");
        return requestData;
    }

    /**
     * 确定API路径
     */
    private String determineApiPath(String type) {
        if (VALID_SEARCH_TYPES.contains(type)) {
            return "song".equals(type) ? "/v3/search/song" : "/v1/search/" + type;
        }
        return "/v3/search/song";
    }

    /**
     * 认证信息
     */
    public record AuthInfo(String token, String userid) {}

    /**
     * 搜索请求
     */
    @Getter
    @RequiredArgsConstructor
    public static class SearchRequest {
        private final String keywords;
        private final Integer page;
        private final Integer pagesize;
        private final String type;
        private final String cookie;
    }
}