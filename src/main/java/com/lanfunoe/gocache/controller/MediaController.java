package com.lanfunoe.gocache.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 媒体文件服务API
 * 提供本地缓存的媒体文件访问，支持Range请求
 */
@Slf4j
@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController extends BaseController {

}
