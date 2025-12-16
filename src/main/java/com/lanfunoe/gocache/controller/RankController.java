package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.service.common.CommonService;
import com.lanfunoe.gocache.service.common.request.GetRequest;
import com.lanfunoe.gocache.service.common.request.PostRequest;
import com.lanfunoe.gocache.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 排行榜控制器
 *
 * 提供排行榜相关的API接口
 *
 */
@Slf4j
@RestController
@RequestMapping("/rank")
@RequiredArgsConstructor
public class RankController extends BaseController {

    private final CommonService commonService;

    /**
     * 获取排行榜音乐列表
     *
     * @param rankid 排行榜ID
     * @param rankCID 排行榜分类ID
     * @param page 页码
     * @param pagesize 每页大小
     * @param request HTTP请求对象
     * @return 排行榜音乐列表
     */
    @GetMapping("/audio")
    public Mono<ResponseEntity<Map<String, Object>>> getRankAudio(
            @RequestParam(required = true) String rankid,
            @RequestParam(required = false, defaultValue = "0") Integer rankCID,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "30") Integer pagesize,
            ServerHttpRequest request) {

        return handleOperationWithResponse("获取排行榜音乐列表",
            Mono.defer(() -> {
                if (rankid == null || rankid.trim().isEmpty()) {
                    Map<String, Object> error = new HashMap<>();
                    error.put("status", 400);
                    error.put("message", "排行榜ID不能为空");
                    return Mono.just(ResponseEntity.badRequest().body(error));
                }

                Map<String, Object> requestData = new HashMap<>();
                requestData.put("show_portrait_mv", 1);
                requestData.put("show_type_total", 1);
                requestData.put("filter_original_remarks", 1);
                requestData.put("area_code", 1);
                requestData.put("pagesize", pagesize);
                requestData.put("rank_cid", rankCID);
                requestData.put("type", 1);
                requestData.put("page", page);
                requestData.put("rank_id", rankid);

                Map<String, Object> queryParams = new HashMap<>();
                String finalToken = CookieUtils.extractTokenCompatible(request);
                String finalUserid = CookieUtils.extractUserIdCompatible(request);
                if (finalToken != null && !finalToken.isBlank()) {
                    queryParams.put("token", finalToken);
                }
                if (finalUserid != null && !finalUserid.isBlank()) {
                    queryParams.put("userid", finalUserid);
                }

                // 构建请求头
                Map<String, String> headers = new HashMap<>();
                headers.put("kg-tid", "369");

                PostRequest postRequest = new PostRequest(
                        "/openapi/kmr/v2/rank/audio",
                        requestData,
                        queryParams,
                        headers,
                        "android"
                );

                return commonService.postWithDefaultsAndAuth(postRequest, finalToken, finalUserid)
                        .map(ResponseEntity::ok);
            }),
            rankid, page, pagesize);
    }

    /**
     * 获取排行榜列表
     *
     * @param withsong 是否包含歌曲信息
     * @param cookie 用户Cookie
     * @return 排行榜列表
     */
    @GetMapping("/list")
    public Mono<ResponseEntity<Map<String, Object>>> getRankList(
            @RequestParam(required = false, defaultValue = "1") Integer withsong,
            @RequestHeader(value = "Cookie", required = false) String cookie) {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("plat", 2);
        queryParams.put("withsong", withsong);
        queryParams.put("parentid", 0);

        Map<String, String> headers = new HashMap<>();
        if (cookie != null) {
            headers.put("Cookie", cookie);
        }

        GetRequest request = new GetRequest("/ocean/v6/rank/list", queryParams, headers);

        return handleOperationWithResponse("获取排行榜列表",
            commonService.getWithDefaults(request)
                .map(ResponseEntity::ok),
            withsong);
    }

}