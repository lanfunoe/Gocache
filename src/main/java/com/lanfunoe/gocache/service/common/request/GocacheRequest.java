package com.lanfunoe.gocache.service.common.request;

import lombok.Data;

import java.util.Map;

@Data
public abstract class GocacheRequest {

    /**
     * 请求路径
     */
    protected String path;

    /**
     * 请求头
     */
    protected Map<String, String> headers;

    /**
     * 加密类型
     */
    protected String encryptType;

}