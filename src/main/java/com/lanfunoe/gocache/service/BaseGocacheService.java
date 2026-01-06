package com.lanfunoe.gocache.service;

import com.lanfunoe.gocache.config.GocacheApiConfig;
import com.lanfunoe.gocache.util.EncryptionUtils;
import com.lanfunoe.gocache.util.WebClientRequestBuilder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Map;

@Slf4j
public abstract class BaseGocacheService {

    protected static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE_REF = new ParameterizedTypeReference<>() {};
    @Resource
    protected GocacheApiConfig gocacheApiConfig;
    @Resource
    protected WebClientRequestBuilder webClientRequestBuilder;
    @Resource
    protected EncryptionUtils encryptionUtils;

}