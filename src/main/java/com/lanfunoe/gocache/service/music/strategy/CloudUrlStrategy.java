package com.lanfunoe.gocache.service.music.strategy;

import com.lanfunoe.gocache.constants.GocacheConstants;
import com.lanfunoe.gocache.util.WebClientRequestBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 云盘URL策略
 * 处理云盘音乐的播放链接生成
 */
@Slf4j
@Component("cloudUrlStrategy")
@RequiredArgsConstructor
public class CloudUrlStrategy implements UrlStrategy {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE_REF =
            new ParameterizedTypeReference<>() {};

    private final WebClientRequestBuilder webClientRequestBuilder;

    @Override
    public Mono<Map<String, Object>> generateUrl(Map<String, Object> params) {
        if (!validateParams(params)) {
            return Mono.error(new IllegalArgumentException("云盘URL生成参数不能为空"));
        }

        String hash = (String) params.get("hash");
        String albumAudioId = (String) params.get("albumAudioId");
        String audioId = (String) params.get("audioId");
        String name = (String) params.get("name");

        if (hash == null || hash.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("音乐hash不能为空"));
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("hash", hash);
        requestBody.put("album_audio_id", albumAudioId);
        requestBody.put("audio_id", audioId);
        requestBody.put("name", name);
        requestBody.put("plat", GocacheConstants.PLATFORM_ANDROID);
        requestBody.put("appid", GocacheConstants.APP_ID_ANDROID);
        requestBody.put("version", GocacheConstants.CLIENT_VERSION);
        requestBody.put("client", GocacheConstants.CLIENT_ANDROID);
        requestBody.put("mid", GocacheConstants.DEFAULT_MID);

        Map<String, String> headers = new HashMap<>();
        headers.put(GocacheConstants.HEADER_X_ROUTER, GocacheConstants.URL_DOMAIN);

        return webClientRequestBuilder.sendPostRequest(
                webClientRequestBuilder.createDefaultWebClient(),
                GocacheConstants.PATH_CLOUD_PLAY_SONG,
                requestBody,
                null,
                headers,
                MAP_TYPE_REF
        );
    }

    @Override
    public String getStrategyType() {
        return "CLOUD";
    }

    @Override
    public boolean validateParams(Map<String, Object> params) {
        if (!UrlStrategy.super.validateParams(params)) {
            return false;
        }

        return params.containsKey("hash");
    }

    @Override
    public String[] getSupportedQualities() {
        return new String[]{"standard", "high", "super"};
    }
}