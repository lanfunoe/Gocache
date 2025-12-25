package com.lanfunoe.gocache.service.playlist;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.lanfunoe.gocache.config.GocacheConfig;
import com.lanfunoe.gocache.constants.GocacheConstants;
import com.lanfunoe.gocache.dto.TagResponse;
import com.lanfunoe.gocache.service.BaseGocacheService;
import com.lanfunoe.gocache.service.cache.EntityCacheService;
import com.lanfunoe.gocache.service.common.CommonService;
import com.lanfunoe.gocache.service.common.request.GetRequest;
import com.lanfunoe.gocache.service.common.request.PostRequest;
import com.lanfunoe.gocache.service.data.TagDataConverter;
import com.lanfunoe.gocache.util.EncryptionUtils;
import com.lanfunoe.gocache.util.WebClientRequestBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 歌单服务
 * 处理歌单相关的业务逻辑
 */
@Slf4j
@Service
public class PlaylistService extends BaseGocacheService {

    private final CommonService commonService;
    private final EntityCacheService cacheService;
    private final TagDataConverter tagDataConverter;
    ObjectMapper mapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    public PlaylistService(GocacheConfig gocacheConfig,
                           WebClientRequestBuilder webClientRequestBuilder,
                           EncryptionUtils encryptionUtils,
                           CommonService commonService,
                           EntityCacheService cacheService,
                           TagDataConverter tagDataConverter) {
        super(gocacheConfig, webClientRequestBuilder, encryptionUtils);
        this.commonService = commonService;
        this.cacheService = cacheService;
        this.tagDataConverter = tagDataConverter;
    }

    /**
     * 获取歌单所有歌曲
     */
    public Mono<Map<String, Object>> getPlaylistTracksAll(TracksAllRequest request) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("area_code", 1);
        requestData.put("begin_idx", (request.page() - 1) * request.pagesize());
        requestData.put("plat", 1);
        requestData.put("type", 1);
        requestData.put("mode", 1);
        requestData.put("personal_switch", 1);
        requestData.put("extend_fields", "abtags,hot_cmt,popularization");
        requestData.put("pagesize", request.pagesize());
        requestData.put("global_collection_id", request.id());

        Map<String, String> headers = new HashMap<>();
        if (request.cookie() != null) {
            headers.put("Cookie", request.cookie());
        }

        GetRequest getRequest = new GetRequest(GocacheConstants.PATH_GET_OTHER_LIST_FILE_NOFILT, requestData, headers);
        return commonService.getWithDefaultsAndAuth(getRequest, null, null);
    }

    /**
     * 创建歌单
     */
    public Mono<Map<String, Object>> addPlaylist(AddPlaylistRequest request) {
        String finalUserid = request.userid() != null ? request.userid() : "0";
        String finalToken = request.token() != null ? request.token() : "";
        Integer finalSource = (request.source() == 0) ? 0 : 1;
        long clienttime = System.currentTimeMillis() / 1000;

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("userid", finalUserid);
        requestData.put("token", finalToken);
        requestData.put("total_ver", 0);
        requestData.put("name", request.name());
        requestData.put("type", request.type());
        requestData.put("source", finalSource);
        requestData.put("is_pri", 0);
        requestData.put("list_create_userid", request.listCreateUserid());
        requestData.put("list_create_listid", request.listCreateListid());
        requestData.put("list_create_gid", request.listCreateGid() != null ? request.listCreateGid() : "");
        requestData.put("from_shupinmv", 0);

        if (request.type() == 0 && request.isPri() != null) {
            requestData.put("is_pri", request.isPri());
        }

        Map<String, Object> queryParams = new HashMap<>();
        if (request.type() == 0) {
            queryParams.put("last_time", clienttime);
            queryParams.put("last_area", "gztx");
            queryParams.put("userid", finalUserid);
            queryParams.put("token", finalToken);
        }

        Map<String, String> headers = new HashMap<>();
        if (request.cookie() != null) {
            headers.put("Cookie", request.cookie());
        }

        PostRequest postRequest = new PostRequest(
                "/cloudlist.service/v5/add_list",
                requestData,
                queryParams,
                headers,
                "android"
        );

        return commonService.post(postRequest);
    }

    /**
     * 添加歌曲到歌单
     */
    public Mono<Map<String, Object>> addTracksToPlaylist(AddTracksRequest request) {
        String finalUserid = request.userid() != null ? request.userid() : "0";
        String finalToken = request.token() != null ? request.token() : "";
        long clienttime = System.currentTimeMillis() / 1000;

        List<Map<String, Object>> resourceList = parseSongData(request.data());

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("userid", finalUserid);
        requestData.put("token", finalToken);
        requestData.put("listid", request.listid());
        requestData.put("list_ver", 0);
        requestData.put("type", 0);
        requestData.put("slow_upload", 1);
        requestData.put("scene", "false;null");
        requestData.put("data", resourceList);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("last_time", clienttime);
        queryParams.put("last_area", "gztx");
        queryParams.put("userid", finalUserid);
        queryParams.put("token", finalToken);

        Map<String, String> headers = new HashMap<>();
        if (request.cookie() != null) {
            headers.put("Cookie", request.cookie());
        }

        PostRequest postRequest = new PostRequest(
                "/cloudlist.service/v6/add_song",
                requestData,
                queryParams,
                headers,
                "android"
        );

        return commonService.post(postRequest);
    }

    /**
     * 从歌单删除歌曲
     */
    public Mono<Map<String, Object>> deleteTracksFromPlaylist(DeleteTracksRequest request) {
        String finalUserid = request.userid() != null ? request.userid() : "0";
        String finalToken = request.token() != null ? request.token() : "";

        List<Map<String, Object>> resourceList = parseFileIds(request.fileids());

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("listid", request.listid());
        requestData.put("userid", finalUserid);
        requestData.put("data", resourceList);
        requestData.put("type", 0);
        requestData.put("token", finalToken);
        requestData.put("list_ver", 0);

        Map<String, String> headers = new HashMap<>();
        headers.put("x-router", "cloudlist.service.kugou.com");
        if (request.cookie() != null) {
            headers.put("Cookie", request.cookie());
        }

        PostRequest postRequest = new PostRequest(
                "/v4/delete_songs",
                requestData,
                null,
                headers,
                "android"
        );

        return commonService.post(postRequest);
    }

    /**
     * 获取歌单分类标签
     */
    public Mono<List<TagResponse>> getPlaylistTags(String userId, String token) {
        return cacheService.getAllTags(
                () -> {
                    // API调用获取原始响应
                    Map<String, Object> requestData = new HashMap<>();
                    requestData.put("tag_type", "collection");
                    requestData.put("tag_id", 0);
                    requestData.put("source", 3);
                    PostRequest postRequest = new PostRequest(
                            "/pubsongs/v1/get_tags_by_type",
                            requestData,
                            null,
                            null,
                            "android"
                    );
                    return commonService.postWithDefaultsAndAuth(postRequest, token, userId).map(response -> {
                        try {
                            Object data = response.get("data");
                            return mapper.convertValue(data, new TypeReference<List<TagResponse>>() {});
                        } catch (Exception e) {
                            log.error("Failed to convert API response data to TagResponse list", e);
                            return List.of();
                        }
                    });
                },
                true
        );
    }


    /**
     * 解析歌曲数据
     * 格式：歌曲名|歌曲hash|专辑ID|歌曲ID, 多个用逗号分隔
     */
    private List<Map<String, Object>> parseSongData(String data) {
        List<Map<String, Object>> resourceList = new ArrayList<>();
        String[] songs = data.split(",");
        for (String song : songs) {
            String[] parts = song.split("\\|");
            if (parts.length >= 4) {
                Map<String, Object> resource = new HashMap<>();
                resource.put("number", 1);
                resource.put("name", parts[0] != null ? parts[0] : "");
                resource.put("hash", parts[1] != null ? parts[1] : "");
                resource.put("size", 0);
                resource.put("sort", 0);
                resource.put("timelen", 0);
                resource.put("bitrate", 0);
                try {
                    resource.put("album_id", Integer.parseInt(parts[2]));
                } catch (NumberFormatException e) {
                    resource.put("album_id", 0);
                }
                try {
                    resource.put("mixsongid", Integer.parseInt(parts[3]));
                } catch (NumberFormatException e) {
                    resource.put("mixsongid", 0);
                }
                resourceList.add(resource);
            }
        }
        return resourceList;
    }

    /**
     * 解析文件ID列表
     */
    private List<Map<String, Object>> parseFileIds(String fileids) {
        List<Map<String, Object>> resourceList = new ArrayList<>();
        String[] fileIdArray = fileids.split(",");
        for (String fileId : fileIdArray) {
            try {
                Map<String, Object> resource = new HashMap<>();
                resource.put("fileid", Integer.parseInt(fileId.trim()));
                resourceList.add(resource);
            } catch (NumberFormatException e) {
                log.warn("无效的文件ID格式: {}", fileId);
            }
        }
        return resourceList;
    }


    /**
     * 获取歌单歌曲请求
     */
    public record TracksAllRequest(String id, Integer page, Integer pagesize, String cookie) {
        public TracksAllRequest {
            page = page != null ? page : 1;
            pagesize = pagesize != null ? pagesize : 30;
        }
    }

    /**
     * 创建歌单请求
     */
    public record AddPlaylistRequest(
            String name,
            Integer type,
            Integer source,
            Integer isPri,
            String listCreateUserid,
            String listCreateListid,
            String listCreateGid,
            String userid,
            String token,
            String cookie
    ) {
        public AddPlaylistRequest {
            type = type != null ? type : 0;
            source = source != null ? source : 1;
        }
    }

    /**
     * 添加歌曲请求
     */
    public record AddTracksRequest(String listid, String data, String userid, String token, String cookie) {}

    /**
     * 删除歌曲请求
     */
    public record DeleteTracksRequest(String listid, String fileids, String userid, String token, String cookie) {}
}