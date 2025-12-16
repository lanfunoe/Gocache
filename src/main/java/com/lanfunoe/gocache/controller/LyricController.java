package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.service.lyrics.LyricsService;
import jakarta.validation.constraints.NotBlank;
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
 * 歌词控制器
 *
 * 提供歌词相关的API接口
 *
 */
@Slf4j
@RestController
@RequestMapping("/lyric")
@RequiredArgsConstructor
public class LyricController extends BaseController {

    private final LyricsService lyricsService;

    /**
     * 获取歌词
     *
     * @param id        歌词ID
     * @param accesskey 访问密钥
     * @param client    客户端类型
     * @param fmt       歌词格式 (krc/lrc)
     * @param decode    是否解码歌词内容
     * @return 歌词内容
     */
    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> getLyric(
            @RequestParam @NotBlank(message = "歌词ID不能为空") String id,
            @RequestParam @NotBlank(message = "访问密钥不能为空") String accesskey,
            @RequestParam(required = false, defaultValue = "android") String client,
            @RequestParam(required = false, defaultValue = "krc") String fmt,
            @RequestParam(required = false, defaultValue = "false") Boolean decode) {

        LyricsService.LyricsRequest request = new LyricsService.LyricsRequest(
                id, accesskey, client, fmt, decode);

        return handleOperation("获取歌词",
            lyricsService.getLyrics(request),
            id, client, fmt, decode);
    }
}