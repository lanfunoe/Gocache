package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.exception.BusinessException;
import com.lanfunoe.gocache.service.music.MusicService;
import com.lanfunoe.gocache.service.user.UserContentService;
import com.lanfunoe.gocache.service.user.UserInfoService;
import com.lanfunoe.gocache.service.user.VipService;
import com.lanfunoe.gocache.util.CookieUtils;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserInfoService userInfoService;
    private final VipService vipService;
    private final UserContentService userContentService;
    private final MusicService musicService;

    /**
     * 获取用户详情
     *
     * @param userid 用户ID
     * @param token  用户token
     * @param request HTTP请求
     * @return 用户详情
     */
    @GetMapping("/detail")
    public Mono<ResponseEntity<Map<String, Object>>> getUserDetail(
            @RequestParam(required = false) String userid,
            @RequestParam(required = false) String token,
            ServerHttpRequest request) {

        String extractedToken = CookieUtils.extractTokenCompatible(request);
        String extractedUserid = CookieUtils.extractUserIdCompatible(request);

        String finalToken = extractedToken != null && !extractedToken.isBlank() ? extractedToken : token;
        String finalUserid = extractedUserid != null && !extractedUserid.isBlank() ? extractedUserid : userid;

        // 验证必要参数并 handle within reactive chain
        if (finalToken == null || finalUserid == null) {
            return handleOperation("获取用户详情",
                Mono.error(BusinessException.badRequest("缺少token或userid参数")),
                finalUserid != null ? finalUserid : "null");
        }

        return handleOperation("获取用户详情", userInfoService.getUserDetail(finalToken, finalUserid), finalUserid);
    }

    /**
     * 获取VIP详情
     *
     * @param userid 用户ID
     * @param token  用户token
     * @param request HTTP请求
     * @return VIP详情
     */
    @GetMapping("/vip/detail")
    public Mono<ResponseEntity<Map<String, Object>>> getVipDetail(
            @RequestParam(required = false) String userid,
            @RequestParam(required = false) String token,
            ServerHttpRequest request) {

        String extractedToken = CookieUtils.extractTokenCompatible(request);
        String extractedUserid = CookieUtils.extractUserIdCompatible(request);
        String finalToken = extractedToken != null && !extractedToken.isBlank() ? extractedToken : token;
        String finalUserid = extractedUserid != null && !extractedUserid.isBlank() ? extractedUserid : userid;

        return handleOperation("获取VIP详情",
                vipService.getVipDetail(finalToken, finalUserid),
                finalUserid);
    }

    /**
     * 获取听歌记录
     *
     * @param userid 用户ID
     * @param token  用户token
     * @param type   记录类型
     * @param request HTTP请求
     * @return 听歌记录
     */
    @GetMapping("/listen")
    public Mono<ResponseEntity<Map<String, Object>>> getListenHistory(
            @RequestParam(required = false) String userid,
            @RequestParam(required = false) String token,
            @RequestParam(required = false, defaultValue = "0") Integer type,
            ServerHttpRequest request) {

        String extractedToken = CookieUtils.extractTokenCompatible(request);
        String extractedUserid = CookieUtils.extractUserIdCompatible(request);
        String finalToken = extractedToken != null && !extractedToken.isBlank() ? extractedToken : token;
        String finalUserid = extractedUserid != null && !extractedUserid.isBlank() ? extractedUserid : userid;

        // 验证必要参数并 handle within reactive chain
        if (finalToken == null || finalUserid == null) {
            return handleOperation("获取听歌记录",
                Mono.error(BusinessException.badRequest("缺少token或userid参数")),
                finalUserid != null ? finalUserid : "null", type);
        }

        return handleOperation("获取听歌记录", userContentService.getListenHistory(finalToken, finalUserid, type), finalUserid, type);
    }

    /**
     * 获取用户关注列表
     *
     * @param userid 用户ID
     * @param token  用户token
     * @param request HTTP请求
     * @return 关注列表
     */
    @GetMapping("/follow")
    public Mono<ResponseEntity<Map<String, Object>>> getUserFollow(
            @RequestParam(required = false) String userid,
            @RequestParam(required = false) String token,
            ServerHttpRequest request) {

        String extractedToken = CookieUtils.extractTokenCompatible(request);
        String extractedUserid = CookieUtils.extractUserIdCompatible(request);
        String finalToken = extractedToken != null && !extractedToken.isBlank() ? extractedToken : token;
        String finalUserid = extractedUserid != null && !extractedUserid.isBlank() ? extractedUserid : userid;

        // 验证必要参数并 handle within reactive chain
        if (finalToken == null || finalUserid == null) {
            return handleOperation("获取用户关注列表",
                Mono.error(BusinessException.badRequest("缺少token或userid参数")),
                finalUserid != null ? finalUserid : "null");
        }

        return handleOperation("获取用户关注列表", userInfoService.getUserFollow(finalToken, finalUserid), finalUserid);
    }

    /**
     * 获取用户歌单
     *
     * @param userid   用户ID
     * @param token    用户token
     * @param page     页码
     * @param pagesize 每页大小
     * @param request  HTTP请求
     * @return 用户歌单
     */
    @GetMapping("/playlist")
    public Mono<ResponseEntity<Map<String, Object>>> getUserPlaylist(
            @RequestParam(required = false) String userid,
            @RequestParam(required = false) String token,
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer page,
            @RequestParam(required = false, defaultValue = "30") @Min(value = 1, message = "每页大小必须大于0") Integer pagesize,
            ServerHttpRequest request) {

        String extractedToken = CookieUtils.extractTokenCompatible(request);
        String extractedUserid = CookieUtils.extractUserIdCompatible(request);
        String finalToken = extractedToken != null && !extractedToken.isBlank() ? extractedToken : token;
        String finalUserid = extractedUserid != null && !extractedUserid.isBlank() ? extractedUserid : userid;

        if (finalToken == null || finalUserid == null) {
            return handleOperation("获取用户歌单",
                Mono.error(BusinessException.badRequest("缺少token或userid参数")),
                finalUserid != null ? finalUserid : "null", page, pagesize);
        }

        UserContentService.UserPlaylistRequest playlistRequest = new UserContentService.UserPlaylistRequest(
                finalToken, finalUserid, page, pagesize);
        return handleOperation("获取用户歌单", userContentService.getUserPlaylist(playlistRequest), finalUserid, page, pagesize);
    }

    /**
     * 获取用户云盘音乐
     *
     * @param userid   用户ID
     * @param token    用户token
     * @param page     页码
     * @param pagesize 每页大小
     * @param request  HTTP请求
     * @return 云盘音乐列表
     */
    @GetMapping("/cloud")
    public Mono<ResponseEntity<Map<String, Object>>> getUserCloud(
            @RequestParam(required = false) String userid,
            @RequestParam(required = false) String token,
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer page,
            @RequestParam(required = false, defaultValue = "30") @Min(value = 1, message = "每页大小必须大于0") Integer pagesize,
            ServerHttpRequest request) {

        String extractedToken = CookieUtils.extractTokenCompatible(request);
        String extractedUserid = CookieUtils.extractUserIdCompatible(request);
        String extractedDfid = CookieUtils.extractDfid(request);
        String finalToken = extractedToken != null && !extractedToken.isBlank() ? extractedToken : token;
        String finalUserid = extractedUserid != null && !extractedUserid.isBlank() ? extractedUserid : userid;

        // 验证必要参数并 handle within reactive chain
        if (finalToken == null || finalUserid == null) {
            return handleOperation("获取用户云盘音乐",
                Mono.error(BusinessException.badRequest("缺少token或userid参数")),
                finalUserid != null ? finalUserid : "null", page, pagesize);
        }

        UserContentService.UserCloudRequest cloudRequest = new UserContentService.UserCloudRequest(
                finalToken, finalUserid, extractedDfid, page, pagesize);

        return handleOperation("获取用户云盘音乐", userContentService.getUserCloud(cloudRequest), finalUserid, page, pagesize);
    }

    /**
     * 获取云盘音乐播放地址
     *
     * @param hash          音乐哈希值
     * @param albumAudioId 专辑音频ID
     * @param audioId      音频ID
     * @param name          音乐名称
     * @return 播放地址
     */
    @GetMapping("/cloud/url")
    public Mono<ResponseEntity<Map<String, Object>>> getCloudMusicUrl(
            @RequestParam String hash,
            @RequestParam(required = false) String albumAudioId,
            @RequestParam(required = false) String audioId,
            @RequestParam(required = false) String name) {

        MusicService.CloudMusicUrlRequest request = new MusicService.CloudMusicUrlRequest(
                hash, albumAudioId, audioId, name);

        return handleOperation("获取云盘音乐播放地址", musicService.getCloudMusicUrl(request), hash);
    }
}