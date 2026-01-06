package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.model.UserSessionContext;
import com.lanfunoe.gocache.service.user.VipService;
import com.lanfunoe.gocache.util.UserSessionExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * VIP控制器
 *
 * 提供VIP相关的API接口
 *
 */
@Slf4j
@RestController
@RequestMapping("/youth")
@RequiredArgsConstructor
public class VipController extends BaseController {

    private final VipService vipService;

    /**
     * 领取每日VIP (一天)
     *
     * @param request HTTP请求
     * @return 领取结果
     */
    @GetMapping("/day/vip")
    public Mono<ResponseEntity<Map<String, Object>>> receiveDayVip(ServerHttpRequest request) {
        UserSessionContext session = UserSessionExtractor.extract(request);
        validateUserSession(session);

        return handleOperation("领取每日VIP",
            vipService.receiveDayVip(session),
            session.userId());
    }

    /**
     * 领取Youth VIP (通过广告)
     *
     * @param request HTTP请求
     * @return 领取结果
     */
    @GetMapping("/vip")
    public Mono<ResponseEntity<Map<String, Object>>> receiveYouthVip(ServerHttpRequest request) {
        UserSessionContext session = UserSessionExtractor.extract(request);
        validateUserSession(session);

        return handleOperation("领取Youth VIP",
            vipService.receiveYouthVip(session),
            session.userId());
    }
}
