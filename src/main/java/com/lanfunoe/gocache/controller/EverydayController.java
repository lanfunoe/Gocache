package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.model.UserSessionContext;
import com.lanfunoe.gocache.service.everyday.EverydayService;
import com.lanfunoe.gocache.util.UserSessionExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 每日推荐控制器
 * 提供每日推荐相关的API接口
 *
 */
@Slf4j
@RestController
@RequestMapping("/everyday")
@RequiredArgsConstructor
public class EverydayController extends BaseController {

    private final EverydayService everydayService;

    /**
     * 获取每日推荐
     *
     * @param platform 平台类型（默认ios）
     * @return 标准化响应包含每日推荐歌曲列表
     */
    @GetMapping("/recommend")
    public Mono<ResponseEntity<Map<String, Object>>> getEverydayRecommend(
            @RequestParam(required = false, defaultValue = "ios") String platform,
            ServerHttpRequest request) {
        UserSessionContext session = UserSessionExtractor.extract(request);
        return handleBoxOperation("获取每日推荐",
                everydayService.getEverydayRecommend(platform, session),
                platform);
    }
}