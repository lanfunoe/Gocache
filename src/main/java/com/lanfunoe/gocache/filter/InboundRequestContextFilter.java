package com.lanfunoe.gocache.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 入站请求日志过滤器
 *
 * 记录所有入站 HTTP 请求信息
 * traceId/spanId 由 Spring Boot 的 logging.pattern.correlation 配置自动添加到日志中
 * 使用 contextCapture() 确保日志在正确的 tracing context 中记录
 */
@Component
@Slf4j
@Order(0)
public class InboundRequestContextFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String method = exchange.getRequest().getMethod().name();
        String path = exchange.getRequest().getURI().getPath();
        String query = exchange.getRequest().getURI().getQuery();

        return chain.filter(exchange)
                .contextCapture()  // 捕获当前上下文，确保 MDC 可用
                .doFirst(() -> {
                    // doFirst 在订阅时执行，此时上下文已被正确设置
                    String requestInfo = String.format("%s %s%s",
                            method,
                            path,
                            query != null ? "?" + query : "");
                    log.info("INBOUND REQUEST: {}", requestInfo);
                });
    }
}
