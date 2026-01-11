package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.service.config.SystemConfigService;
import com.lanfunoe.gocache.service.storage.DownloadTaskQueue;
import com.lanfunoe.gocache.service.storage.MediaStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 存储管理API
 * 提供媒体文件存储的统计、配置和管理功能
 */
@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController extends BaseController {

    private final MediaStorageService mediaStorageService;
    private final DownloadTaskQueue downloadTaskQueue;
    private final SystemConfigService systemConfigService;

    //todo
}
