package com.lanfunoe.gocache.util;

import com.lanfunoe.gocache.model.UserSessionContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * 用户会话信息提取器。
 *
 * <p>封装 CookieUtils 的兼容逻辑，避免 controller 里反复解析 token/userid。</p>
 */
public final class UserSessionExtractor {

    private UserSessionExtractor() {
    }

    public static UserSessionContext extract(ServerHttpRequest request) {
        if (request == null) {
            return new UserSessionContext(null, null);
        }
        String token = CookieUtils.extractTokenCompatible(request);
        String userIdStr = CookieUtils.extractUserIdCompatible(request);
        Long userId = userIdStr == null ? null : Long.parseLong(userIdStr);
        return new UserSessionContext(userId, StringUtils.trimToNull(token));
    }
}