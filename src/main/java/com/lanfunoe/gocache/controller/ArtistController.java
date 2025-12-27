package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.exception.BusinessException;
import com.lanfunoe.gocache.util.CookieUtils;
import com.lanfunoe.gocache.service.artist.ArtistService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 歌手控制器
 *
 * 提供歌手相关的API接口
 *
 */
@Slf4j
@RestController
@RequestMapping("/artist")
@RequiredArgsConstructor
public class ArtistController extends BaseController {

    private final ArtistService artistService;

    /**
     * 获取歌手详情
     *
     * @param id 歌手ID
     * @return 歌手详情
     */
    @GetMapping("/detail")
    public Mono<ResponseEntity<Map<String, Object>>> getArtistDetail(
            @RequestParam @NotBlank(message = "歌手ID不能为空") String id) {

        return handleOperation("获取歌手详情",
            artistService.getArtistDetail(id),
            id);
    }

    /**
     * 获取歌手音乐列表
     *
     * @param id       歌手ID
     * @param page     页码
     * @param pagesize 每页大小
     * @param sort     排序方式（hot:最热，new:最新）
     * @param request  HTTP请求
     * @return 音乐列表
     */
    @GetMapping("/audios")
    public Mono<ResponseEntity<Map<String, Object>>> getArtistAudios(
            @RequestParam @NotBlank(message = "歌手ID不能为空") String id,
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer page,
            @RequestParam(required = false, defaultValue = "30") @Min(value = 1, message = "每页大小必须大于0") Integer pagesize,
            @RequestParam(required = false, defaultValue = "hot") String sort,
            ServerHttpRequest request) {

        // 从Cookie中获取dfid
        String dfid = CookieUtils.extractDfid(request);

        ArtistService.ArtistAudiosRequest audiosRequest = new ArtistService.ArtistAudiosRequest(
                id, String.valueOf(page), String.valueOf(pagesize), sort, dfid);

        return handleOperation("获取歌手音乐列表",
            artistService.getArtistAudios(audiosRequest),
            id, page, pagesize, sort);
    }

    /**
     * 关注歌手
     *
     * @param id     歌手ID
     * @param token  用户token
     * @param userid 用户ID
     * @param request HTTP请求
     * @return 关注结果
     */
    @GetMapping("/follow")
    public Mono<ResponseEntity<Map<String, Object>>> followArtist(
            @RequestParam @NotBlank(message = "歌手ID不能为空") String id,
            @RequestParam(required = false) String token,
            @RequestParam(required = false) String userid,
            ServerHttpRequest request) {

        String extractedToken = CookieUtils.extractTokenCompatible(request);
        String extractedUserid = CookieUtils.extractUserIdCompatible(request);

        String finalToken = StringUtils.isNotBlank(extractedToken) ? extractedToken : token;
        String finalUserid = StringUtils.isNotBlank(extractedUserid) ? extractedUserid : userid;

        return handleOperation("关注歌手",
            Mono.defer(() -> {
                // 验证必要参数
                if (finalToken == null || finalUserid == null) {
                    return Mono.error(BusinessException.badRequest("缺少token或userid参数"));
                }

                ArtistService.ArtistFollowRequest followRequest = new ArtistService.ArtistFollowRequest(
                        id, finalToken, finalUserid);

                return artistService.followArtist(followRequest);
            }),
            id, finalUserid);
    }

    /**
     * 取消关注歌手
     *
     * @param id     歌手ID
     * @param token  用户token
     * @param userid 用户ID
     * @param request HTTP请求
     * @return 取消关注结果
     */
    @GetMapping("/unfollow")
    public Mono<ResponseEntity<Map<String, Object>>> unfollowArtist(
            @RequestParam @NotBlank(message = "歌手ID不能为空") String id,
            @RequestParam(required = false) String token,
            @RequestParam(required = false) String userid,
            ServerHttpRequest request) {

        String extractedToken = CookieUtils.extractTokenCompatible(request);
        String extractedUserid = CookieUtils.extractUserIdCompatible(request);

        String finalToken = StringUtils.isNotBlank(extractedToken) ? extractedToken : token;
        String finalUserid = StringUtils.isNotBlank(extractedUserid) ? extractedUserid : userid;

        return handleOperation("取消关注歌手",
            Mono.defer(() -> {
                // 验证必要参数
                if (finalToken == null || finalUserid == null) {
                    return Mono.error(BusinessException.badRequest("缺少token或userid参数"));
                }

                ArtistService.ArtistFollowRequest unfollowRequest = new ArtistService.ArtistFollowRequest(
                        id, finalToken, finalUserid);

                return artistService.unfollowArtist(unfollowRequest);
            }),
            id, finalUserid);
    }
}
