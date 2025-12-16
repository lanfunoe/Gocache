package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.exception.BusinessException;
import com.lanfunoe.gocache.service.music.MusicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 权限控制器
 *
 * 提供权限相关的API接口
 *
 */
@Slf4j
@RestController
@RequestMapping("/privilege")
@RequiredArgsConstructor
public class PrivilegeController extends BaseController {

    private final MusicService musicService;

    /**
     * 检查音乐播放权限
     *
     * @param hash    音乐哈希值（支持多个，逗号分隔）
     * @param albumId 专辑ID（支持多个，逗号分隔）
     * @return 播放权限信息
     */
    @GetMapping("/lite")
    public Mono<ResponseEntity<Map<String, Object>>> checkMusicPrivilege(
            @RequestParam String hash,
            @RequestParam(required = false) String albumId) {

        return handleOperation("检查音乐播放权限",
            Mono.defer(() -> {
                if (hash == null || hash.trim().isEmpty()) {
                    return Mono.error(BusinessException.badRequest("缺少hash参数"));
                }
                return musicService.checkMusicPrivilege(hash, albumId);
            }),
            hash, albumId);
    }
}