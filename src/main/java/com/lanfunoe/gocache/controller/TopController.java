package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.config.GocacheConfig;
import com.lanfunoe.gocache.service.common.CommonService;
import com.lanfunoe.gocache.service.common.request.PostRequest;
import com.lanfunoe.gocache.service.top.TopPlaylistService;
import com.lanfunoe.gocache.util.CookieUtils;
import com.lanfunoe.gocache.util.SignatureUtils;
import com.lanfunoe.gocache.util.WebClientRequestBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/top")
@RequiredArgsConstructor
public class TopController extends BaseController {

    private final CommonService commonService;
    private final GocacheConfig gocacheConfig;
    private final TopPlaylistService topPlaylistService;


    /**
     * 获取推荐卡片
     *
     * @param card_id 卡片ID：1-精选好歌随心听，2-经典怀旧金曲，3-热门好歌精选，4-小众宝藏佳作，6-vip专属推荐
     * @param userid 用户ID
     * @return 推荐卡片内容
     */
    @GetMapping("/card")
    public Mono<ResponseEntity<Map<String, Object>>> getTopCard(
            @RequestParam(required = false, defaultValue = "1") Integer card_id,
            @RequestParam(required = false) String userid) {

        String fakem = "60f7ebf1f812edbac3c63a7310001701760f";
        long dateTime = System.currentTimeMillis();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("clienttime", dateTime);
        dataMap.put("userid", userid != null ? userid : 0);
        dataMap.put("key", SignatureUtils.signParamsKey(String.valueOf(dateTime), "1005", "11083"));
        dataMap.put("fakem", fakem);
        dataMap.put("area_code", 1);
        dataMap.put("client_playlist", new Object[0]);
        dataMap.put("u_info", "a0c35cd40af564444b5584c2754dedec");

        // 构建查询参数
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("card_id", card_id);
        queryParams.put("fakem", fakem);
        queryParams.put("area_code", 1);
        queryParams.put("platform", "android");

        // 构建请求头
        Map<String, String> headers = WebClientRequestBuilder.buildHeadersWithRouter("singlecardrec.service.kugou.com");

        PostRequest request = new PostRequest(
                "/singlecardrec.service/v1/single_card_recommend",
                dataMap,
                queryParams,
                headers,
                "android"
        );

        return handleOperation("获取推荐卡片",
            commonService.postWithDefaults(request),
            card_id, userid);
    }

    /**
     * 获取热门歌单
     *
     * @param category_id 分类ID：0-推荐
     * @param page 页码
     * @param pagesize 每页大小
     * @param withtag 是否包含标签
     * @param withsong 是否包含歌曲
     * @param sort 排序方式
     * @param module_id 模块ID
     * @return 热门歌单列表
     */
    @GetMapping("/playlist")
    public Mono<ResponseEntity<Map<String, Object>>> getTopPlaylist(@RequestParam(required = false, defaultValue = "0") Integer category_id, @RequestParam(required = false, defaultValue = "1") Integer page, @RequestParam(required = false, defaultValue = "30") Integer pagesize, @RequestParam(required = false, defaultValue = "1") Integer withtag, @RequestParam(required = false, defaultValue = "1") Integer withsong, @RequestParam(required = false, defaultValue = "1") Integer sort, @RequestParam(required = false, defaultValue = "1") Integer module_id, ServerHttpRequest request) {

        String userid = CookieUtils.extractUserIdCompatible(request);
        String token = CookieUtils.extractTokenCompatible(request);

        return handleBoxOperation("获取热门歌单",
                topPlaylistService.getTopPlaylist(category_id, page, pagesize, withtag, withsong, sort, module_id, token, userid),
                category_id, page, pagesize);
    }
}