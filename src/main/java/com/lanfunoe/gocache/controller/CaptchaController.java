package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 验证码控制器
 *
 * 提供验证码相关的API接口
 *
 */
@RestController
@RequestMapping("/captcha")
@RequiredArgsConstructor
public class CaptchaController extends BaseController {

    private final AuthService authService;

    /**
     * 发送手机验证码
     *
     * @param mobile 手机号
     * @return 发送结果
     */
    @GetMapping("/sent")
    public Mono<ResponseEntity<Map<String, Object>>> sendVerificationCode(@RequestParam String mobile) {
        return handleOperation("发送手机验证码", authService.sendMobileCode(mobile), mobile);
    }
}