package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.service.common.CommonService;
import com.lanfunoe.gocache.service.common.request.GetRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 每日推荐控制器
 *
 * 提供每日推荐相关的API接口
 *
 */
@Slf4j
@RestController
@RequestMapping("/everyday")
@RequiredArgsConstructor
public class EverydayController extends BaseController {

    private final CommonService commonService;

    /**
     * 获取每日推荐
     *
     * @param platform 平台类型（默认ios）
     * @return 每日推荐内容
     */
    @GetMapping("/recommend")
    public Mono<ResponseEntity<Map<String, Object>>> getEverydayRecommend(
            @RequestParam(required = false, defaultValue = "ios") String platform) {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("platform", platform);

        // 构建请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("x-router", "everydayrec.service.kugou.com");

        GetRequest request = new GetRequest("/everyday_song_recommend", queryParams, headers);

        return handleOperation("获取每日推荐",
            commonService.get(request),
            platform);
    }
}