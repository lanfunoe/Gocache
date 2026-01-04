package com.lanfunoe.gocache.service.everyday;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanfunoe.gocache.dto.EverydayRecommendResponse;
import com.lanfunoe.gocache.model.UserSessionContext;
import com.lanfunoe.gocache.service.BaseGocacheService;
import com.lanfunoe.gocache.service.cache.DailyRecommendCacheService;
import com.lanfunoe.gocache.service.common.CommonService;
import com.lanfunoe.gocache.service.common.request.PostRequest;
import com.lanfunoe.gocache.util.DateTimeUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static com.lanfunoe.gocache.constants.GocacheConstants.HEADER_X_ROUTER;
import static com.lanfunoe.gocache.constants.GocacheConstants.X_ROUTER_EVERYDAY_REC;

/**
 * 每日推荐服务
 */
@Slf4j
@Service
public class EverydayService extends BaseGocacheService {

    @Resource
    private CommonService commonService;
    @Resource
    private DailyRecommendCacheService cacheService;
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 获取每日推荐
     *
     * @param platform 平台类型（默认ios）
     * @return 完整的每日推荐响应（包含元数据和歌曲列表）
     */
    public Mono<EverydayRecommendResponse> getEverydayRecommend(String platform, UserSessionContext session) {
        String date = DateTimeUtils.formatCurrentDateYMD();
        return cacheService.get(date, session.userId(), () -> fetchFromApi(platform, session.token(), session.userId()));
    }

    /**
     * 从上游API获取数据
     */
    private Mono<EverydayRecommendResponse> fetchFromApi(String platform, String token, String userid) {
        Map<String, String> headers = new HashMap<>();
        headers.put(HEADER_X_ROUTER, X_ROUTER_EVERYDAY_REC);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("platform", platform);

        PostRequest request = new PostRequest("/everyday_song_recommend", null, queryParams, headers, null);

        return commonService.postWithDefaultsAndAuth(request, token, userid)
                .map(response -> {
                    try {
                        Object data = response.get("data");
                        return objectMapper.convertValue(data, new TypeReference<>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to convert API response data to EverydayRecommendResponse", e);
                    }
                });
    }
}