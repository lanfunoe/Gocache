package com.lanfunoe.gocache.util;

import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * Cookie工具类
 *
 */
public class CookieUtils {

    /**
     * 从请求中提取Cookie值
     *
     * @param request    HTTP请求
     * @param cookieName Cookie名称
     * @return Cookie值，如果不存在则返回null
     */
    public static String extractCookieValue(ServerHttpRequest request, String cookieName) {
        String cookieHeader = request.getHeaders().getFirst("Cookie");
        if (cookieHeader == null) {
            return null;
        }

        String[] cookies = cookieHeader.split(";");
        for (String cookie : cookies) {
            String[] parts = cookie.trim().split("=", 2);
            if (parts.length == 2 && parts[0].equals(cookieName)) {
                return parts[1];
            }
        }

        return null;
    }

    /**
     * 从请求中提取token
     *
     * @param request HTTP请求
     * @return token值
     */
    public static String extractToken(ServerHttpRequest request) {
        return extractCookieValue(request, "token");
    }

    /**
     * 从请求中提取userid
     *
     * @param request HTTP请求
     * @return userid值
     */
    public static String extractUserId(ServerHttpRequest request) {
        return extractCookieValue(request, "userid");
    }

    /**
     * 从请求中提取dfid
     *
     * @param request HTTP请求
     * @return dfid值
     */
    public static String extractDfid(ServerHttpRequest request) {
        return extractCookieValue(request, "dfid");
    }

    /**
     * 从请求中提取vip_type
     *
     * @param request HTTP请求
     * @return vip_type值
     */
    public static String extractVipType(ServerHttpRequest request) {
        return extractCookieValue(request, "vip_type");
    }

    /**
     * 从请求中提取vip_token
     *
     * @param request HTTP请求
     * @return vip_token值
     */
    public static String extractVipToken(ServerHttpRequest request) {
        return extractCookieValue(request, "vip_token");
    }

    /**
     * 从Authorization头中提取指定key的值
     *
     * @param request HTTP请求
     * @param key     要提取的key
     * @return 对应的值，如果不存在则返回null
     */
    private static String extractFromAuthorization(ServerHttpRequest request, String key) {
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader == null) {
            return null;
        }

        String[] parts = authHeader.split(";");
        for (String part : parts) {
            String[] keyValue = part.trim().split("=", 2);
            if (keyValue.length == 2 && keyValue[0].equals(key)) {
                return keyValue[1];
            }
        }

        return null;
    }

    /**
     * 从Authorization头中提取token
     *
     * @param request HTTP请求
     * @return token值
     */
    public static String extractTokenFromAuthorization(ServerHttpRequest request) {
        return extractFromAuthorization(request, "token");
    }

    /**
     * 从Authorization头中提取userid
     *
     * @param request HTTP请求
     * @return userid值
     */
    public static String extractUserIdFromAuthorization(ServerHttpRequest request) {
        return extractFromAuthorization(request, "userid");
    }

    /**
     * 从请求中提取token（优先从Cookie，其次从Authorization）
     *
     * @param request HTTP请求
     * @return token值
     */
    public static String extractTokenCompatible(ServerHttpRequest request) {
        String token = extractToken(request);
        if (token == null) {
            token = extractTokenFromAuthorization(request);

        }
        return token;
    }

    /**
     * 从请求中提取userid（优先从Cookie，其次从Authorization）
     *
     * @param request HTTP请求
     * @return userid值
     */
    public static String extractUserIdCompatible(ServerHttpRequest request) {
        String userId = extractUserId(request);
        if (userId == null) {
            userId = extractUserIdFromAuthorization(request);
        }
        return userId;
    }
}