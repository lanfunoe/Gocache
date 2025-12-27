package com.lanfunoe.gocache.service;

import com.lanfunoe.gocache.config.GocacheConfig;
import com.lanfunoe.gocache.util.EncryptionUtils;
import com.lanfunoe.gocache.util.WebClientRequestBuilder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Map;

@Slf4j
public abstract class BaseGocacheService {

    protected static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE_REF =
            new ParameterizedTypeReference<>() {};
    @Resource
    protected GocacheConfig gocacheConfig;
    @Resource
    protected WebClientRequestBuilder webClientRequestBuilder;
    @Resource
    protected EncryptionUtils encryptionUtils;



    /**
     * 验证参数不为空
     *
     * @param value 参数值
     * @param paramName 参数名称
     * @return 验证后的参数值
     * @throws IllegalArgumentException 参数为空时抛出
     */
    protected <T> T requireNonNull(T value, String paramName) {
        if (value == null) {
            throw new IllegalArgumentException(paramName + "不能为空");
        }
        return value;
    }

}