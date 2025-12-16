package com.lanfunoe.gocache.model;

/**
 * 用户会话上下文（从 cookie / query 等兼容来源提取）
 *
 * <p>仅承载数据，不包含提取逻辑，避免 service 依赖 web 层。</p>
 */
public record UserSessionContext(String userId, String token) {
}

