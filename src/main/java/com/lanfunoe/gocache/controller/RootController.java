package com.lanfunoe.gocache.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 根路径控制器
 *
 * 提供API根路径的欢迎信息
 *
 */
@RestController
public class RootController {

    /**
     * API根路径
     *
     * @return 欢迎信息
     */
    @GetMapping("/")
    public Map<String, Object> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("type", "welcome");
        response.put("data", "gocache");
        return response;
    }
}