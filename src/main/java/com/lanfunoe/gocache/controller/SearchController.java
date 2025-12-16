package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.service.lyrics.LyricsService;
import com.lanfunoe.gocache.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 搜索控制器
 *
 * 提供搜索相关的API接口
 *
 */
@Slf4j
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController extends BaseController {

    private final LyricsService lyricsService;
    private final SearchService searchService;

    /**
     * 搜索歌词
     *
     * @param hash          音乐哈希值
     * @param keywords      搜索关键词
     * @param albumAudioId 专辑音频ID
     * @param man           是否男声（默认no）
     * @return 搜索结果
     */
    @GetMapping("/lyric")
    public Mono<ResponseEntity<Map<String, Object>>> searchLyrics(
            @RequestParam(required = false) String hash,
            @RequestParam(required = false) String keywords,
            @RequestParam(required = false) String albumAudioId,
            @RequestParam(required = false, defaultValue = "no") String man) {

        return handleOperation("搜索歌词",
            lyricsService.searchLyrics(new LyricsService.LyricsSearchRequest(hash, keywords, albumAudioId, man)),
            hash, keywords, man);
    }

    /**
     * 搜索音乐、歌手等
     *
     * @param keywords 搜索关键词
     * @param page     页码
     * @param pagesize 每页大小
     * @param type     搜索类型：song, album, author, mv, lyric, special
     * @param cookie   用户Cookie
     * @return 搜索结果
     */
    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> search(
            @RequestParam(required = false) String keywords,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "30") Integer pagesize,
            @RequestParam(required = false, defaultValue = "song") String type,
            @RequestHeader(value = "Cookie", required = false) String cookie) {

        SearchService.SearchRequest request = new SearchService.SearchRequest(
                keywords, page, pagesize, type, cookie);

        return handleOperation("搜索", searchService.search(request), keywords, page, pagesize, type);
    }
}