package com.lanfunoe.gocache.service.artist;

import com.lanfunoe.gocache.config.GocacheConfig;
import com.lanfunoe.gocache.constants.GocacheConstants;
import com.lanfunoe.gocache.service.BaseGocacheService;
import com.lanfunoe.gocache.util.CryptoUtils;
import com.lanfunoe.gocache.util.EncryptionUtils;
import com.lanfunoe.gocache.util.WebClientRequestBuilder;
import com.lanfunoe.gocache.util.SignatureUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 歌手服务
 * 处理歌手相关的所有业务逻辑
 */
@Slf4j
@Service
public class ArtistService extends BaseGocacheService {

    public ArtistService(GocacheConfig gocacheConfig,
                              WebClientRequestBuilder webClientRequestBuilder,
                              EncryptionUtils encryptionUtils) {
        super(gocacheConfig, webClientRequestBuilder, encryptionUtils);
    }

    /**
     * 获取歌手详情
     *
     * @param artistId 歌手ID
     * @return 歌手详情
     */
    public Mono<Map<String, Object>> getArtistDetail(String artistId) {
        validateArtistId(artistId);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("author_id", Long.parseLong(artistId));

        Map<String, String> headers = WebClientRequestBuilder.buildHeadersWithRouterAndKgTid(
                GocacheConstants.OPENAPI_DOMAIN, "36");

        return webClientRequestBuilder.sendPostRequestWithDefaults(
                webClientRequestBuilder.createDefaultWebClient(),
                GocacheConstants.PATH_AUTHOR_DETAIL,
                requestBody,
                null,
                headers,
                GocacheConstants.ENCRYPT_TYPE_ANDROID,
                MAP_TYPE_REF
        );
    }

    /**
     * 获取歌手音乐列表
     *
     * @param request 歌手音乐列表请求
     * @return 音乐列表
     */
    public Mono<Map<String, Object>> getArtistAudios(ArtistAudiosRequest request) {
        validateArtistAudiosRequest(request);

        long clientTime = System.currentTimeMillis() / GocacheConstants.MILLIS_TO_SECONDS;
        String mid = CryptoUtils.md5(request.getDfid() != null ? request.getDfid() : "-");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("appid", gocacheConfig.getAppid());
        requestBody.put("clientver", gocacheConfig.getClientver());
        requestBody.put("mid", mid);
        requestBody.put("clienttime", clientTime);
        requestBody.put("key", SignatureUtils.signParamsKey(
                String.valueOf(clientTime),
                String.valueOf(gocacheConfig.getAppid()),
                gocacheConfig.getClientver()));
        requestBody.put("author_id", Integer.parseInt(request.getArtistId()));
        requestBody.put("pagesize", request.getPagesize() != null ?
                request.getPagesize() : GocacheConstants.DEFAULT_PAGE_SIZE);
        requestBody.put("page", request.getPage() != null ?
                request.getPage() : GocacheConstants.DEFAULT_PAGE);
        requestBody.put("sort", GocacheConstants.SORT_HOT.equals(request.getSort()) ? 1 : 2);
        requestBody.put("area_code", "all");

        Map<String, String> headers = WebClientRequestBuilder.buildHeadersWithRouterAndKgTid(
                GocacheConstants.OPENAPI_DOMAIN, "220");

        return webClientRequestBuilder.sendPostRequestWithDefaults(
                webClientRequestBuilder.createWebClient("https://" + GocacheConstants.OPENAPI_DOMAIN),
                GocacheConstants.PATH_AUTHOR_AUDIOS,
                requestBody,
                null,
                headers,
                GocacheConstants.ENCRYPT_TYPE_ANDROID,
                MAP_TYPE_REF
        );
    }

    /**
     * 关注歌手
     *
     * @param request 关注歌手请求
     * @return 关注结果
     */
    public Mono<Map<String, Object>> followArtist(ArtistFollowRequest request) {
        validateArtistFollowRequest(request);

        long clientTime = System.currentTimeMillis() / GocacheConstants.MILLIS_TO_SECONDS;

        Map<String, Object> requestBody = encryptionUtils.buildArtistFollowEncryptedParams(
                request.getSingerId(), request.getToken(), request.getUserid(), clientTime, true);

        // 提取查询参数（如果有）
        Map<String, Object> queryParams = null;
        if (requestBody.containsKey("_extras")) {
            Map<String, Object> extras = (Map<String, Object>) requestBody.get("_extras");
            if (extras.containsKey("queryParams")) {
                queryParams = (Map<String, Object>) extras.get("queryParams");
            }
            requestBody.remove("_extras");
        }

        return webClientRequestBuilder.sendPostRequest(
                webClientRequestBuilder.createDefaultWebClient(),
                GocacheConstants.PATH_FOLLOW_SINGER,
                requestBody,
                queryParams,
                null,
                MAP_TYPE_REF
        );
    }

    /**
     * 取消关注歌手
     *
     * @param request 取消关注歌手请求
     * @return 取消关注结果
     */
    public Mono<Map<String, Object>> unfollowArtist(ArtistFollowRequest request) {
        validateArtistFollowRequest(request);

        long clientTime = System.currentTimeMillis() / GocacheConstants.MILLIS_TO_SECONDS;

        Map<String, Object> requestBody = encryptionUtils.buildArtistFollowEncryptedParams(
                request.getSingerId(), request.getToken(), request.getUserid(), clientTime, false);

        return webClientRequestBuilder.sendPostRequest(
                webClientRequestBuilder.createDefaultWebClient(),
                GocacheConstants.PATH_UNFOLLOW_SINGER,
                requestBody,
                null,
                null,
                MAP_TYPE_REF
        );
    }

    // 验证方法
    private void validateArtistId(String artistId) {
        if (artistId == null || artistId.trim().isEmpty()) {
            throw new IllegalArgumentException("歌手ID不能为空");
        }
        try {
            Long.parseLong(artistId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("歌手ID必须是数字");
        }
    }

    private void validateArtistAudiosRequest(ArtistAudiosRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("歌手音乐列表请求不能为空");
        }
        if (request.getArtistId() == null || request.getArtistId().trim().isEmpty()) {
            throw new IllegalArgumentException("歌手ID不能为空");
        }
        try {
            Integer.parseInt(request.getArtistId());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("歌手ID必须是数字");
        }
        if (request.getPage() != null && request.getPage() < 1) {
            throw new IllegalArgumentException("页码必须大于0");
        }
        if (request.getPagesize() != null && (request.getPagesize() < 1 || request.getPagesize() > 100)) {
            throw new IllegalArgumentException("每页大小必须在1-100之间");
        }
    }

    private void validateArtistFollowRequest(ArtistFollowRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("歌手关注请求不能为空");
        }
        if (request.getSingerId() == null || request.getSingerId().trim().isEmpty()) {
            throw new IllegalArgumentException("歌手ID不能为空");
        }
        if (request.getToken() == null || request.getToken().trim().isEmpty()) {
            throw new IllegalArgumentException("用户token不能为空");
        }
        if (request.getUserid() == null || request.getUserid().trim().isEmpty()) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
    }

    // 请求参数对象
    public static class ArtistAudiosRequest {
        private final String artistId;
        private final Integer page;
        private final Integer pagesize;
        private final String sort;
        private final String dfid;

        public ArtistAudiosRequest(String artistId, Integer page, Integer pagesize, String sort, String dfid) {
            this.artistId = artistId;
            this.page = page;
            this.pagesize = pagesize;
            this.sort = sort;
            this.dfid = dfid;
        }

        public String getArtistId() { return artistId; }
        public Integer getPage() { return page; }
        public Integer getPagesize() { return pagesize; }
        public String getSort() { return sort; }
        public String getDfid() { return dfid; }
    }

    public static class ArtistFollowRequest {
        private final String singerId;
        private final String token;
        private final String userid;

        public ArtistFollowRequest(String singerId, String token, String userid) {
            this.singerId = singerId;
            this.token = token;
            this.userid = userid;
        }

        public String getSingerId() { return singerId; }
        public String getToken() { return token; }
        public String getUserid() { return userid; }
    }
}