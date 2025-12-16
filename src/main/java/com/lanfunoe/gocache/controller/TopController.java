package com.lanfunoe.gocache.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanfunoe.gocache.constants.GocacheConstants;
import com.lanfunoe.gocache.service.common.CommonService;
import com.lanfunoe.gocache.service.common.request.PostRequest;
import com.lanfunoe.gocache.util.CryptoUtils;
import com.lanfunoe.gocache.util.SignatureUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
    private final ObjectMapper objectMapper;

    /**
     * 获取推荐卡片
     *
     * @param card_id 卡片ID：1-精选好歌随心听，2-经典怀旧金曲，3-热门好歌精选，4-小众宝藏佳作，6-vip专属推荐
     * @param userid 用户ID
     * @param dfid 设备指纹ID
     * @return 推荐卡片内容
     */
    @GetMapping("/card")
    public Mono<ResponseEntity<Map<String, Object>>> getTopCard(
            @RequestParam(required = false, defaultValue = "1") Integer card_id,
            @RequestParam(required = false) String userid,
            @RequestParam(required = false) String dfid) {

        String finalDfid = dfid != null ? dfid : "-";
        String fakem = "60f7ebf1f812edbac3c63a7310001701760f";
        String mid = CryptoUtils.md5(finalDfid);
        long dateTime = System.currentTimeMillis();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("appid", 1005);
        dataMap.put("clientver", "11083");
        dataMap.put("platform", "android");
        dataMap.put("clienttime", dateTime);
        dataMap.put("userid", userid != null ? userid : 0);
        dataMap.put("key", SignatureUtils.signParamsKey(String.valueOf(dateTime), "1005", "11083"));
        dataMap.put("fakem", fakem);
        dataMap.put("area_code", 1);
        dataMap.put("mid", mid);
        dataMap.put("uuid", CryptoUtils.md5(finalDfid + mid));
        dataMap.put("client_playlist", new Object[0]);
        dataMap.put("u_info", "a0c35cd40af564444b5584c2754dedec");

        // 构建查询参数
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("card_id", card_id);
        queryParams.put("fakem", fakem);
        queryParams.put("area_code", 1);
        queryParams.put("platform", "android");

        // 构建请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("x-router", "singlecardrec.service.kugou.com");

        PostRequest request = new PostRequest(
                "/singlecardrec.service/v1/single_card_recommend",
                dataMap,
                queryParams,
                headers,
                "android"
        );

        return handleOperation("获取推荐卡片",
            commonService.post(request),
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
     * @param cookie 用户Cookie
     * @return 热门歌单列表
     */
    @GetMapping("/playlist")
    public Mono<ResponseEntity<Map<String, Object>>> getTopPlaylist(
            @RequestParam(required = false, defaultValue = "0") Integer category_id,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "30") Integer pagesize,
            @RequestParam(required = false, defaultValue = "1") Integer withtag,
            @RequestParam(required = false, defaultValue = "1") Integer withsong,
            @RequestParam(required = false, defaultValue = "1") Integer sort,
            @RequestParam(required = false, defaultValue = "1") Integer module_id,
            @RequestHeader(value = "Cookie", required = false) String cookie) {

        String dfid = "-";
        String mid = CryptoUtils.md5(dfid);
        String uuid = CryptoUtils.md5(dfid + mid);
        String dateTime = String.valueOf(System.currentTimeMillis() / 1000);

        // 解析cookie获取userid
        String userid = "0";
        if (cookie != null && !cookie.isEmpty()) {
            String[] cookieParts = cookie.split(";");
            for (String part : cookieParts) {
                String[] keyValue = part.trim().split("=");
                if (keyValue.length == 2 && "userid".equals(keyValue[0].trim())) {
                    userid = keyValue[1].trim();
                    break;
                }
            }
        }

        // 构建special_recommend对象
        Map<String, Object> specialRecommend = new HashMap<>();
        specialRecommend.put("withtag", withtag);
        specialRecommend.put("withsong", withsong);
        specialRecommend.put("sort", sort);
        specialRecommend.put("ugc", 1);
        specialRecommend.put("is_selected", 0);
        specialRecommend.put("withrecommend", 1);
        specialRecommend.put("area_code", 1);
        specialRecommend.put("categoryid", category_id);

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("appid", 1005);
        requestData.put("mid", mid);
        requestData.put("clientver", "11083");
        requestData.put("platform", "android");
        requestData.put("clienttime", dateTime);
        requestData.put("userid", Long.parseLong(userid));
        requestData.put("module_id", module_id);
        requestData.put("page", page);
        requestData.put("pagesize", pagesize);
        requestData.put("key", SignatureUtils.signParamsKey(String.valueOf(dateTime), "1005", "11083"));
        requestData.put("special_recommend", specialRecommend);
        requestData.put("req_multi", 1);
        requestData.put("retrun_min", 5);
        requestData.put("return_special_falg", 1);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("dfid", dfid);
        queryParams.put("mid", mid);
        queryParams.put("uuid", uuid);
        queryParams.put("appid", 1005);
        queryParams.put("clientver", "11083");
        queryParams.put("platform", "android");
        queryParams.put("clienttime", dateTime);
        queryParams.put("userid", userid);

        // 生成signature - 使用android签名方式
        String dataJson;
        try {
            dataJson = objectMapper.writeValueAsString(requestData);
        } catch (JsonProcessingException e) {
            log.error("JSON序列化失败: {}", e.getMessage());
            dataJson = "{}";
        }
        queryParams.put("signature", com.lanfunoe.gocache.util.SignatureUtils.signatureAndroidParams(queryParams, dataJson));

        // 构建请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("x-router", "specialrec.service.kugou.com");
        headers.put(GocacheConstants.HEADER_USER_AGENT, GocacheConstants.USER_AGENT_ANDROID);
        headers.put("dfid", dfid);
        headers.put("clienttime", dateTime);
        headers.put("mid", mid);

        if (cookie != null) {
            headers.put("Cookie", cookie);
        }

        PostRequest request = new PostRequest(
                "/v2/special_recommend",
                requestData,
                queryParams,
                headers,
                "android"
        );

        return handleOperation("获取热门歌单",
            commonService.post(request),
            category_id, page, pagesize);
    }
}