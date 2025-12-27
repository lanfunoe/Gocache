package com.lanfunoe.gocache.service.auth;

import com.lanfunoe.gocache.constants.GocacheConstants;
import com.lanfunoe.gocache.service.BaseGocacheService;
import com.lanfunoe.gocache.util.QRCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务
 * 处理登录相关的所有业务逻辑
 */
@Slf4j
@Service
public class AuthService extends BaseGocacheService {


    /**
     * 账号密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    public Mono<Map<String, Object>> loginByPassword(String username, String password) {

        long currentTime = System.currentTimeMillis();

        Map<String, Object> requestBody = encryptionUtils.buildLoginEncryptedParams(
                username, password, currentTime);

        Map<String, String> headers = new HashMap<>();
        headers.put(GocacheConstants.HEADER_X_ROUTER, GocacheConstants.LOGIN_USER_DOMAIN);

        return webClientRequestBuilder.sendPostRequest(
                webClientRequestBuilder.createDefaultWebClient(),
                GocacheConstants.PATH_LOGIN_BY_PWD,
                requestBody,
                null,
                headers,
                MAP_TYPE_REF
        );
    }

    /**
     * 获取二维码登录key
     *
     * @param type 类型（web或其他）
     * @return 二维码key信息
     */
    public Mono<Map<String, Object>> getQRCodeKey(String type) {
        int appId = "web".equals(type) ? GocacheConstants.APP_ID_WEB : GocacheConstants.APP_ID_QR_DEFAULT;

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("appid", appId);
        queryParams.put("type", 1);
        queryParams.put("plat", GocacheConstants.PLATFORM_WEB);
        queryParams.put("qrcode_txt", String.format(GocacheConstants.DEFAULT_QRCODE_TEXT_TEMPLATE, gocacheConfig.getAppid()));
        queryParams.put("srcappid", gocacheConfig.getSrcappid());

        return webClientRequestBuilder.sendGetRequestWithDefaults(
                webClientRequestBuilder.createWebClient(GocacheConstants.LOGIN_USER_URL),
                GocacheConstants.PATH_QRCODE,
                queryParams,
                null,
                "web",
                MAP_TYPE_REF
        );
    }

    /**
     * 创建二维码
     *
     * @param key 二维码key
     * @param qrimg 是否生成图片
     * @return 二维码信息
     */
    public Mono<Map<String, Object>> createQRCode(String key, boolean qrimg) {

        return Mono.fromCallable(() -> {
            String url = GocacheConstants.H5_QRCODE_URL + "?qrcode=" + key;

            Map<String, Object> result = new HashMap<>();
            result.put("code", GocacheConstants.HTTP_SUCCESS_CODE);
            result.put("status", GocacheConstants.HTTP_SUCCESS_CODE);

            Map<String, Object> data = new HashMap<>();
            data.put("url", url);
            if (qrimg) {
                data.put("base64", QRCodeUtils.generateQRCodeBase64(url));
            } else {
                data.put("base64", "");
            }

            result.put("data", data);
            return result;
        });
    }

    /**
     * 检查二维码登录状态
     *
     * @param key 二维码key
     * @return 登录状态信息
     */
    public Mono<Map<String, Object>> checkQRCodeStatus(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("二维码key不能为空");
        }

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("plat", GocacheConstants.PLATFORM_WEB);
        queryParams.put("appid", gocacheConfig.getAppid());
        queryParams.put("srcappid", gocacheConfig.getSrcappid());
        queryParams.put("qrcode", key);

        return webClientRequestBuilder.sendGetRequestWithDefaults(
                webClientRequestBuilder.createWebClient(GocacheConstants.LOGIN_USER_URL),
                GocacheConstants.PATH_GET_USERINFO_QRCODE,
                queryParams,
                null,
                "web",
                MAP_TYPE_REF
        );
    }

    /**
     * 发送手机验证码
     *
     * @param mobile 手机号
     * @return 发送结果
     */
    public Mono<Map<String, Object>> sendMobileCode(String mobile) {
        if (mobile == null || !mobile.matches("^1[3-9]\\d{9}$")) {
            throw new IllegalArgumentException("手机号格式不正确");
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("businessid", GocacheConstants.BUSINESS_ID_MOBILE_CODE);
        requestBody.put("mobile", mobile);
        requestBody.put("plat", GocacheConstants.PLATFORM_ANDROID);

        return webClientRequestBuilder.sendPostRequest(
                webClientRequestBuilder.createWebClient("http://" + GocacheConstants.LOGIN_USER_DOMAIN),
                GocacheConstants.PATH_SEND_MOBILE_CODE,
                requestBody,
                null,
                null,
                MAP_TYPE_REF
        );
    }
}