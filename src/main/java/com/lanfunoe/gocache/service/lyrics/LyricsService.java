package com.lanfunoe.gocache.service.lyrics;

import com.lanfunoe.gocache.config.GocacheConfig;
import com.lanfunoe.gocache.constants.GocacheConstants;
import com.lanfunoe.gocache.service.BaseGocacheService;
import com.lanfunoe.gocache.util.EncryptionUtils;
import com.lanfunoe.gocache.util.LyricsUtils;
import com.lanfunoe.gocache.util.WebClientRequestBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 歌词服务
 * 处理歌词相关的所有业务逻辑
 */
@Slf4j
@Service
public class LyricsService extends BaseGocacheService {

    public LyricsService(GocacheConfig gocacheConfig,
                              WebClientRequestBuilder webClientRequestBuilder,
                              EncryptionUtils encryptionUtils) {
        super(gocacheConfig, webClientRequestBuilder, encryptionUtils);
    }

    /**
     * 搜索歌词
     *
     * @param request 歌词搜索请求
     * @return 搜索结果
     */
    public Mono<Map<String, Object>> searchLyrics(LyricsSearchRequest request) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("album_audio_id", request.getAlbumAudioId() != null ?
                Long.parseLong(request.getAlbumAudioId()) : GocacheConstants.DEFAULT_ALBUM_AUDIO_ID);
        queryParams.put("appid", gocacheConfig.getAppid());
        queryParams.put("clientver", gocacheConfig.getClientver());
        queryParams.put("duration", 0);
        queryParams.put("hash", request.getHash() != null ? request.getHash() : "");
        queryParams.put("keyword", request.getKeywords() != null ? request.getKeywords() : "");
        queryParams.put("lrctxt", 1);
        queryParams.put("man", request.getMan() != null ? request.getMan() : GocacheConstants.DEFAULT_LYRIC_MAN);

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", gocacheConfig.getUserAgent());
        return webClientRequestBuilder.sendGetRequest(
                webClientRequestBuilder.createWebClient(GocacheConstants.LYRIC_DOMAIN),
                GocacheConstants.PATH_SEARCH_LYRICS,
                queryParams,
                headers,
                "android",
                MAP_TYPE_REF
        );
    }

    /**
     * 获取歌词内容
     *
     * @param request 歌词获取请求
     * @return 歌词内容
     */
    public Mono<Map<String, Object>> getLyrics(LyricsRequest request) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("ver", 1);
        queryParams.put("client", request.getClient() != null ?
                request.getClient() : GocacheConstants.DEFAULT_LYRIC_CLIENT);
        queryParams.put("id", request.getId());
        queryParams.put("accesskey", request.getAccessKey());
        queryParams.put("fmt", request.getFmt() != null ?
                request.getFmt() : GocacheConstants.DEFAULT_LYRIC_FMT);
        queryParams.put("charset", GocacheConstants.CHARSET_UTF8);

        return webClientRequestBuilder.sendGetRequest(
                webClientRequestBuilder.createWebClient(GocacheConstants.LYRIC_DOMAIN),
                GocacheConstants.PATH_DOWNLOAD_LYRICS,
                queryParams,
                null,
                "android",
                MAP_TYPE_REF
        ).map(response -> {
            if (request.getDecode() != null && request.getDecode() && response != null) {
                decodeLyricContent(response, request.getFmt());
            }
            return response;
        });
    }

    /**
     * 解码歌词内容
     * 如果decode为true且content存在，则进行解码
     */
    private void decodeLyricContent(Map<String, Object> response, String fmt) {
        if (response.containsKey("content")) {
            String content = response.get("content").toString();
            String contentType = response.getOrDefault("contenttype", "0").toString();

            String decodedContent;
            if ("lrc".equalsIgnoreCase(fmt) || !"0".equals(contentType)) {
                // LRC格式或contenttype不为0，直接Base64解码
                try {
                    decodedContent = new String(Base64.getDecoder().decode(content), StandardCharsets.UTF_8);
                } catch (Exception e) {
                    log.warn("歌词Base64解码失败，使用原始内容", e);
                    decodedContent = content;
                }
            } else {
                // KRC格式，使用KRC解码
                decodedContent = LyricsUtils.decodeKrcLyrics(content);
            }
            response.put("decodeContent", decodedContent);
        }
    }

    // 请求参数对象
    public static class LyricsSearchRequest {
        private final String hash;
        private final String keywords;
        private final String albumAudioId;
        private final String man;

        public LyricsSearchRequest(String hash, String keywords, String albumAudioId, String man) {
            this.hash = hash;
            this.keywords = keywords;
            this.albumAudioId = albumAudioId;
            this.man = man;
        }

        public String getHash() {
            return hash;
        }

        public String getKeywords() {
            return keywords;
        }

        public String getAlbumAudioId() {
            return albumAudioId;
        }

        public String getMan() {
            return man;
        }
    }
    @Getter
    public static class LyricsRequest {
        private final String id;
        private final String accessKey;
        private final String client;
        private final String fmt;
        private final Boolean decode;

        public LyricsRequest(String id, String accessKey, String client, String fmt, Boolean decode) {
            this.id = id;
            this.accessKey = accessKey;
            this.client = client;
            this.fmt = fmt;
            this.decode = decode;
        }

    }
}