package com.lanfunoe.gocache.service.common.request;

import lombok.Data;

import java.util.Map;

@Data
public class PostRequest extends GocacheRequest {

    /**
     * 请求体数据
     */
    private Map<String, Object> postBody;

    /**
     * 查询参数
     */
    private Map<String, Object> queryParams;

    public PostRequest(String path, Map<String, Object> postBody, Map<String, Object> queryParams,
                      Map<String, String> headers, String encryptType) {
        this.path = path;
        this.postBody = postBody;
        this.queryParams = queryParams;
        this.headers = headers;
        this.encryptType = encryptType != null ? encryptType : "android";
    }
}