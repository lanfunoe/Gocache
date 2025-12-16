package com.lanfunoe.gocache.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Hooks;

/**
 * Micrometer Tracing 配置类
 *
 * 配置 Reactor 自动上下文传播，确保 traceId/spanId 在整个响应式链路中传递
 */
@Slf4j
@Configuration
public class TracingConfig {

    /**
     * 启用 Reactor 自动上下文传播
     *
     * 这样 Observation/Tracing 上下文可以自动在 Reactor 的 publishOn/subscribeOn 线程切换时传播
     * 无需手动使用 tap() 或 handle() 处理上下文
     */
    @PostConstruct
    public void enableReactorContextPropagation() {
        Hooks.enableAutomaticContextPropagation();
        log.info("Reactor automatic context propagation enabled for Micrometer Tracing");
    }

    /**
     * 配置 @Observed 注解支持
     *
     * 允许使用 @Observed 注解自动为方法创建 Observation
     */
    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }
}
