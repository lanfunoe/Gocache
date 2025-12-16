package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.exception.BusinessException;
import com.lanfunoe.gocache.service.user.VipService;
import com.lanfunoe.gocache.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * @param token  用户token
     * @param userid 用户ID
     * @return 领取结果
     */
    @GetMapping("/day/vip")
    public Mono<ResponseEntity<Map<String, Object>>> receiveDayVip(
            @RequestParam(required = false) String token,
            @RequestParam(required = false) String userid,
            ServerHttpRequest request) {
        String finalToken = StringUtils.isBlank(token) ? CookieUtils.extractTokenCompatible(request) : token;
        String finalUserid = StringUtils.isBlank(userid) ? CookieUtils.extractUserIdCompatible(request) : userid;

        return handleOperation("领取每日VIP",
            Mono.defer(() -> {
                // 验证必要参数
                if (StringUtils.isBlank(finalToken) || StringUtils.isBlank(finalUserid)) {
                    return Mono.error(BusinessException.badRequest("缺少token或userid参数"));
                }
                return vipService.receiveDayVip(finalToken, finalUserid);
            }),
            finalUserid);
    }

    /**
     * 领取Youth VIP (通过广告)
     *
     * @param token  用户token
     * @param userid 用户ID
     * @return 领取结果
     */
    @GetMapping("/vip")
    public Mono<ResponseEntity<Map<String, Object>>> receiveYouthVip(
            @RequestParam(required = false) String token,
            @RequestParam(required = false) String userid,
            ServerHttpRequest request) {
        String finalToken = StringUtils.isBlank(token) ? CookieUtils.extractTokenCompatible(request) : token;
        String finalUserid = StringUtils.isBlank(userid) ? CookieUtils.extractUserIdCompatible(request) : userid;

        return handleOperation("领取Youth VIP",
            Mono.defer(() -> {
                if (StringUtils.isBlank(finalToken) || StringUtils.isBlank(finalUserid)) {
                    return Mono.error(BusinessException.badRequest("缺少token或userid参数"));
                }
                return vipService.receiveYouthVip(finalToken, finalUserid);
            }),
            finalUserid);
    }
}
