package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.service.auth.AuthService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 登录控制器
 *
 * 提供登录相关的API接口
 *
 */
@Slf4j
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController extends BaseController {

    private final AuthService authService;

    /**
     * 账号密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> login(
            @RequestParam @NotBlank(message = "用户名不能为空") String username,
            @RequestParam @NotBlank(message = "密码不能为空") String password) {

        return handleOperationWithResponse("账号密码登录",
            authService.loginByPassword(username, password)
                .map(loginResult -> {
                    // 设置响应头Cookie
                    HttpHeaders headers = buildLoginCookies(loginResult);
                    return ResponseEntity.ok().headers(headers).body(loginResult);
                }),
            username);
    }

    /**
     * 获取二维码登录key
     *
     * @param type 类型（web或其他）
     * @return 二维码key信息
     */
    @GetMapping("/qr/key")
    public Mono<ResponseEntity<Map<String, Object>>> getQRCodeKey(
            @RequestParam(required = false) String type) {

        return handleOperation("获取二维码登录key", authService.getQRCodeKey(type), type);
    }

    /**
     * 创建二维码
     *
     * @param key 二维码key
     * @param qrimg 是否生成base64图片
     * @return 二维码信息
     */
    @GetMapping("/qr/create")
    public Mono<ResponseEntity<Map<String, Object>>> createQRCode(
            @RequestParam @NotBlank(message = "二维码key不能为空") String key,
            @RequestParam(required = false, defaultValue = "false") boolean qrimg) {

        return handleOperation("创建二维码", authService.createQRCode(key, qrimg), key, qrimg);
    }

    /**
     * 检查二维码登录状态
     *
     * @param key 二维码key
     * @return 登录状态信息
     */
    @GetMapping("/qr/check")
    public Mono<ResponseEntity<Map<String, Object>>> checkQRCodeStatus(
            @RequestParam @NotBlank(message = "二维码key不能为空") String key) {

        return handleOperationWithResponse("检查二维码登录状态",
            authService.checkQRCodeStatus(key)
                .map(statusResult -> {
                    // 如果登录成功，设置响应头Cookie
                    HttpHeaders headers = buildQRLoginCookies(statusResult);
                    return ResponseEntity.ok().headers(headers).body(statusResult);
                }),
            key);
    }

    /**
     * 构建登录成功后的Cookie头
     *
     * @param loginResult 登录结果
     * @return HTTP头
     */
    private HttpHeaders buildLoginCookies(Map<String, Object> loginResult) {
        HttpHeaders headers = new HttpHeaders();

        if (loginResult.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) loginResult.get("data");
            int maxAge = 365 * 24 * 60 * 60; // 一年

            addCookieToHeaders(headers, "token", data, maxAge);
            addCookieToHeaders(headers, "userid", data, maxAge);
            addCookieToHeaders(headers, "vip_type", data, maxAge);
            addCookieToHeaders(headers, "vip_token", data, maxAge);
        }

        return headers;
    }

    /**
     * 构建二维码登录成功后的Cookie头
     *
     * @param statusResult 状态检查结果
     * @return HTTP头
     */
    private HttpHeaders buildQRLoginCookies(Map<String, Object> statusResult) {
        HttpHeaders headers = new HttpHeaders();

        if (statusResult.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) statusResult.get("data");

            // 检查登录状态是否成功（状态码为4表示成功）
            if (data.containsKey("status") && Integer.parseInt(data.get("status").toString()) == 4) {
                int maxAge = 365 * 24 * 60 * 60; // 一年
                addCookieToHeaders(headers, "token", data, maxAge);
                addCookieToHeaders(headers, "userid", data, maxAge);
            }
        }

        return headers;
    }

    /**
     * 添加Cookie到HTTP头
     *
     * @param headers  HTTP头
     * @param key      Cookie键
     * @param data     数据Map
     * @param maxAge   最大存活时间
     */
    private void addCookieToHeaders(HttpHeaders headers, String key, Map<String, Object> data, int maxAge) {
        if (data.containsKey(key)) {
            String value = data.get(key).toString();
            String cookie = String.format("%s=%s; Max-Age=%d; Path=/",
                    key, URLEncoder.encode(value, StandardCharsets.UTF_8), maxAge);
            headers.add("Set-Cookie", cookie);
        }
    }
}