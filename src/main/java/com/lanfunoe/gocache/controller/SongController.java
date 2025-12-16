package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.exception.BusinessException;
import com.lanfunoe.gocache.model.UserSessionContext;
import com.lanfunoe.gocache.service.music.MusicService;
import com.lanfunoe.gocache.util.UserSessionExtractor;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 音乐控制器
 * <p>
 * 提供音乐相关的API接口
 *
 */
@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
public class SongController extends BaseController {

    private final MusicService musicService;

    /**
     * 获取音乐播放地址
     *
     * @param hash         音乐哈希值
     * @param albumId      专辑ID
     * @param albumAudioId 专辑音频ID
     * @param quality      音质
     * @param freePart     是否只返回试听部分
     * @return 播放地址
     */
    @GetMapping("/url")
    public Mono<ResponseEntity<Map<String, Object>>> getSongUrl(
            @RequestParam @NotBlank(message = "音乐哈希值不能为空") String hash,
            @RequestParam(required = false) String albumId,
            @RequestParam(required = false) String albumAudioId,
            @RequestParam(required = false) String quality,
            @RequestParam(required = false, defaultValue = "false") Boolean freePart,
            ServerHttpRequest request) {
        return handleOperation("获取音乐播放地址",
                Mono.defer(() -> {
                    UserSessionContext session = UserSessionExtractor.extract(request);

                    MusicService.SongUrlRequest songUrl = new MusicService.SongUrlRequest(
                            hash, albumId, albumAudioId, quality, freePart);

                    // 验证必要参数
                    if (session.token() == null || session.userId() == null) {
                        return Mono.error(BusinessException.badRequest("缺少token或userid参数"));
                    }
                    return musicService.getSongUrl(songUrl, session);
                }), hash, quality);
    }

    /**
     * 获取音乐高潮部分时间点
     *
     * @param hash 音乐哈希值（支持多个，逗号分隔）
     * @return 高潮时间点信息
     */
    @GetMapping("/climax")
    public Mono<ResponseEntity<Map<String, Object>>> getSongClimax(
            @RequestParam @NotBlank(message = "音乐哈希值不能为空") String hash) {

        return handleOperation("获取音乐高潮部分时间点", musicService.getSongClimax(hash), hash);
    }
}
