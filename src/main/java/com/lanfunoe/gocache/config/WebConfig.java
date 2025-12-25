package com.lanfunoe.gocache.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

    @Value("${spring.codec.max-in-memory-size:64MB}")
    private DataSize maxInMemorySize;

    /**
     * 全局 Codec 自定义器（服务端）
     * 配置支持 text/plain 作为 JSON 响应的解析
     */
    @Bean
    public CodecCustomizer codecCustomizer() {
        return configurer -> {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE);
            configurer.customCodecs().register(
                    new Jackson2JsonDecoder(objectMapper, MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON));
            configurer.customCodecs().register(
                    new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
        };
    }

    /**
     * 配置 WebClient Bean
     * WebClient 需要显式配置 ExchangeStrategies
     */
    @Bean
    public WebClient.Builder webClientBuilder(ObservationRegistry observationRegistry) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(this::configureClientCodecs)
                .build();

        return WebClient.builder()
                .observationRegistry(observationRegistry)
                .exchangeStrategies(strategies);
    }

    /**
     * WebClient 专用的 Codec 配置
     */
    private void configureClientCodecs(ClientCodecConfigurer configurer) {
        configurer.defaultCodecs().maxInMemorySize((int) maxInMemorySize.toBytes());

        ObjectMapper objectMapper = new ObjectMapper();
        configurer.customCodecs().register(
                new Jackson2JsonDecoder(objectMapper, MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON));
        configurer.customCodecs().register(
                new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
    }
}