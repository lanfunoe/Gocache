package com.lanfunoe.gocache.service.top;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanfunoe.gocache.config.GocacheConfig;
import com.lanfunoe.gocache.constants.GocacheConstants;
import com.lanfunoe.gocache.dto.TopPlaylistResponse;
import com.lanfunoe.gocache.model.UserSessionContext;
import com.lanfunoe.gocache.service.BaseGocacheService;
import com.lanfunoe.gocache.service.cache.TopPlaylistCacheService;
import com.lanfunoe.gocache.service.common.CommonService;
import com.lanfunoe.gocache.service.common.request.PostRequest;
import com.lanfunoe.gocache.util.CryptoUtils;
import com.lanfunoe.gocache.util.SignatureUtils;
import com.lanfunoe.gocache.util.WebClientRequestBuilder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static com.lanfunoe.gocache.constants.GocacheConstants.X_ROUTER_SPECIAL_REC;

/**
 * 热门歌单服务
 * 参考/everyday/recommend的实现模式
 */
@Slf4j
@Service
public class TopPlaylistService extends BaseGocacheService {

    @Resource
    private CommonService commonService;

    @Resource
    private TopPlaylistCacheService cacheService;

    @Resource
    private GocacheConfig gocacheConfig;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 获取热门歌单
     *
     * @param categoryId 分类ID
     * @param page 页码
     * @param pageSize 每页大小
     * @param withtag 是否包含标签
     * @param withsong 是否包含歌曲
     * @param sort 排序方式
     * @param moduleId 模块ID
     * @return 热门歌单响应
     */
    public Mono<TopPlaylistResponse> getTopPlaylist(Integer categoryId, Integer page, Integer pageSize, Integer withtag, Integer withsong, Integer sort, Integer moduleId, UserSessionContext session) {

        return cacheService.get(categoryId, page, pageSize, withtag, withsong, sort, moduleId,
                () -> fetchFromApi(categoryId, page, pageSize, withtag, withsong, sort, moduleId, session));
    }

    /**
     * 从上游API获取数据
     */
    private Mono<TopPlaylistResponse> fetchFromApi(
            Integer categoryId,
            Integer page,
            Integer pageSize,
            Integer withtag,
            Integer withsong,
            Integer sort,
            Integer moduleId,
            UserSessionContext session) {

        long dateTime = System.currentTimeMillis() / 1000;

        // 构建special_recommend对象
        Map<String, Object> specialRecommend = Map.of(
                "withtag", withtag,
                "withsong", withsong,
                "sort", sort,
                "ugc", 1,
                "is_selected", 0,
                "withrecommend", 1,
                "area_code", 1,
                "categoryid", categoryId
        );

        Map<String, Object> postBody = new HashMap<>();
        String dfid = "-";
        String mid = CryptoUtils.md5(dfid);
        postBody.put("mid", mid);
        postBody.put("platform", "android");
        postBody.put("clientver", gocacheConfig.getClientver());
        postBody.put("appid", gocacheConfig.getAppid());
        postBody.put("clienttime", dateTime);
        postBody.put("userid", session.userId());
        postBody.put("module_id", moduleId);
        postBody.put("page", page);
        postBody.put("pagesize", pageSize);
        postBody.put("key", SignatureUtils.signParamsKey(String.valueOf(dateTime), String.valueOf(gocacheConfig.getAppid()), gocacheConfig.getClientver()));
        postBody.put("special_recommend", specialRecommend);
        postBody.put("req_multi", 1);
        postBody.put("retrun_min", 5);
        postBody.put("return_special_falg", 1);

        Map<String, Object> queryParams = Map.of("platform", "android");

        // 构建请求头
        Map<String, String> headers = WebClientRequestBuilder.buildHeadersWithRouter(X_ROUTER_SPECIAL_REC);
        headers.put(GocacheConstants.HEADER_USER_AGENT, GocacheConstants.USER_AGENT_ANDROID);

        PostRequest postRequest = new PostRequest("/v2/special_recommend", postBody, queryParams, headers, "android");

        return commonService.postWithDefaultsAndAuth(postRequest, session)
                .map(response -> {
                    try {
                        return objectMapper.convertValue(response.get("data"), new TypeReference<>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to convert API response to TopPlaylistResponse", e);
                    }
                });
    }
}