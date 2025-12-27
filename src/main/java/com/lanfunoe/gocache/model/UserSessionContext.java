package com.lanfunoe.gocache.model;

/**
 * 用户会话上下文（从 cookie / query 等兼容来源提取）
 *
 */
public record UserSessionContext(String userId, String token) {
}

