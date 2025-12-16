package com.lanfunoe.gocache.service.common.request;

import lombok.Data;

import java.util.Map;

@Data
public class GetRequest extends GocacheRequest {

    /**
     * 查询参数
     */
    private Map<String, Object> queryParams;

    public GetRequest(String path, Map<String, Object> queryParams, Map<String, String> headers) {
        this.path = path;
        this.queryParams = queryParams;
        this.headers = headers;
        this.encryptType = "android";
    }
}