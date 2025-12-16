package com.lanfunoe.gocache.controller;

import com.lanfunoe.gocache.exception.BusinessException;
import com.lanfunoe.gocache.service.playlist.PlaylistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 歌单控制器
 *
 * 提供歌单相关的API接口
 *
 */
@Slf4j
@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
public class PlaylistController extends BaseController {

    private final PlaylistService playlistService;

    /**
     * 获取歌单所有歌曲
     *
     * @param id 歌单ID
     * @param page 页码
     * @param pagesize 每页大小
     * @param cookie 用户Cookie
     * @return 歌单歌曲列表
     */
    @GetMapping("/track/all")
    public Mono<ResponseEntity<Map<String, Object>>> getPlaylistTracksAll(
            @RequestParam(required = true) String id,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "30") Integer pagesize,
            @RequestHeader(value = "Cookie", required = false) String cookie) {

        return handleOperation("获取歌单所有歌曲",
                Mono.defer(() -> {
                    if (id == null || id.trim().isEmpty()) {
                        return Mono.error(BusinessException.badRequest("歌单ID不能为空"));
                    }
                    PlaylistService.TracksAllRequest request =
                            new PlaylistService.TracksAllRequest(id, page, pagesize, cookie);
                    return playlistService.getPlaylistTracksAll(request);
                }),
                id, page, pagesize);
    }

    /**
     * 创建歌单
     *
     * @param name 歌单名称
     * @param type 歌单类型
     * @param source 来源
     * @param isPri 是否私密
     * @param listCreateUserid 歌单创建者用户ID
     * @param listCreateListid 歌单创建者歌单ID
     * @param listCreateGid 歌单创建者群组ID
     * @param userid 用户ID
     * @param token 用户令牌
     * @param cookie 用户Cookie
     * @return 创建结果
     */
    @GetMapping("/add")
    public Mono<ResponseEntity<Map<String, Object>>> addPlaylist(
            @RequestParam(required = true) String name,
            @RequestParam(required = false, defaultValue = "0") Integer type,
            @RequestParam(required = false, defaultValue = "1") Integer source,
            @RequestParam(required = false) Integer isPri,
            @RequestParam(required = false) String listCreateUserid,
            @RequestParam(required = false) String listCreateListid,
            @RequestParam(required = false) String listCreateGid,
            @RequestParam(required = false) String userid,
            @RequestParam(required = false) String token,
            @RequestHeader(value = "Cookie", required = false) String cookie) {

        return handleOperation("创建歌单",
                Mono.defer(() -> {
                    if (name == null || name.trim().isEmpty()) {
                        return Mono.error(BusinessException.badRequest("歌单名称不能为空"));
                    }
                    PlaylistService.AddPlaylistRequest request = new PlaylistService.AddPlaylistRequest(
                            name, type, source, isPri, listCreateUserid, listCreateListid, listCreateGid,
                            userid, token, cookie);
                    return playlistService.addPlaylist(request);
                }),
                name, type, source);
    }

    /**
     * 删除歌单
     *
     * @param listid 歌单ID
     * @param userid 用户ID
     * @param token 用户令牌
     * @param cookie 用户Cookie
     * @return 删除结果
     */
    @GetMapping("/del")
    public Mono<ResponseEntity<Map<String, Object>>> deletePlaylist(
            @RequestParam(required = true) String listid,
            @RequestParam(required = false) String userid,
            @RequestParam(required = false) String token,
            @RequestHeader(value = "Cookie", required = false) String cookie) {

        log.info("删除歌单请求: listid={}", listid);

        if (listid == null || listid.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 400);
            error.put("message", "歌单ID不能为空");
            return Mono.just(ResponseEntity.badRequest().body(error));
        }

        try {
            // 先返回说明信息，提示需要完整实现加密功能
            Map<String, Object> result = new HashMap<>();
            result.put("status", 200);
            result.put("message", "删除歌单API需要完整的加密功能实现");
            result.put("implementation_status", "partial");
            result.put("missing_features", Arrays.asList(
                "playlistAesEncrypt", "playlistAesDecrypt", "rsaEncrypt2", "signParamsKey"
            ));
            return Mono.just(ResponseEntity.ok(result));

        } catch (NumberFormatException e) {
            log.error("歌单ID格式错误: {}", listid);
            Map<String, Object> error = new HashMap<>();
            error.put("status", 400);
            error.put("message", "歌单ID格式错误");
            return Mono.just(ResponseEntity.badRequest().body(error));
        } catch (Exception e) {
            log.error("删除歌单失败: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 500);
            errorResponse.put("message", "删除歌单失败");
            return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
        }
    }

    /**
     * 添加歌曲到歌单
     *
     * @param listid 歌单ID
     * @param data 歌曲数据（格式：歌曲名|歌曲hash|专辑ID|歌曲ID, 多个用逗号分隔）
     * @param userid 用户ID
     * @param token 用户令牌
     * @param cookie 用户Cookie
     * @return 添加结果
     */
    @GetMapping("/tracks/add")
    public Mono<ResponseEntity<Map<String, Object>>> addTracksToPlaylist(
            @RequestParam(required = true) String listid,
            @RequestParam(required = true) String data,
            @RequestParam(required = false) String userid,
            @RequestParam(required = false) String token,
            @RequestHeader(value = "Cookie", required = false) String cookie) {

        return handleOperation("添加歌曲到歌单",
                Mono.defer(() -> {
                    if (listid == null || listid.trim().isEmpty()) {
                        return Mono.error(BusinessException.badRequest("歌单ID不能为空"));
                    }
                    if (data == null || data.trim().isEmpty()) {
                        return Mono.error(BusinessException.badRequest("歌曲数据不能为空"));
                    }
                    PlaylistService.AddTracksRequest request =
                            new PlaylistService.AddTracksRequest(listid, data, userid, token, cookie);
                    return playlistService.addTracksToPlaylist(request)
                            .flatMap(response -> {
                                // 验证歌曲数据格式
                                String[] songs = data.split(",");
                                boolean hasValidSong = false;
                                for (String song : songs) {
                                    if (song.split("\\|").length >= 4) {
                                        hasValidSong = true;
                                        break;
                                    }
                                }
                                if (!hasValidSong) {
                                    return Mono.error(BusinessException.badRequest("歌曲数据格式错误"));
                                }
                                return Mono.just(response);
                            });
                }),
                listid);
    }

    /**
     * 从歌单删除歌曲
     *
     * @param listid 歌单ID
     * @param fileids 歌曲文件ID列表，多个用逗号分隔
     * @param userid 用户ID
     * @param token 用户令牌
     * @param cookie 用户Cookie
     * @return 删除结果
     */
    @GetMapping("/tracks/del")
    public Mono<ResponseEntity<Map<String, Object>>> deleteTracksFromPlaylist(
            @RequestParam(required = true) String listid,
            @RequestParam(required = true) String fileids,
            @RequestParam(required = false) String userid,
            @RequestParam(required = false) String token,
            @RequestHeader(value = "Cookie", required = false) String cookie) {

        return handleOperation("从歌单删除歌曲",
                Mono.defer(() -> {
                    if (listid == null || listid.trim().isEmpty()) {
                        return Mono.error(BusinessException.badRequest("歌单ID不能为空"));
                    }
                    if (fileids == null || fileids.trim().isEmpty()) {
                        return Mono.error(BusinessException.badRequest("歌曲文件ID不能为空"));
                    }
                    // 验证文件ID格式
                    String[] fileIdArray = fileids.split(",");
                    boolean hasValidId = false;
                    for (String fileId : fileIdArray) {
                        try {
                            Integer.parseInt(fileId.trim());
                            hasValidId = true;
                            break;
                        } catch (NumberFormatException e) {
                            // continue checking
                        }
                    }
                    if (!hasValidId) {
                        return Mono.error(BusinessException.badRequest("文件ID格式错误"));
                    }
                    PlaylistService.DeleteTracksRequest request =
                            new PlaylistService.DeleteTracksRequest(listid, fileids, userid, token, cookie);
                    return playlistService.deleteTracksFromPlaylist(request);
                }),
                listid, fileids);
    }

    /**
     * 获取歌单分类标签
     *
     * @param cookie 用户Cookie
     * @return 歌单分类标签列表
     */
    @GetMapping("/tags")
    public Mono<ResponseEntity<Map<String, Object>>> getPlaylistTags(
            @RequestHeader(value = "Cookie", required = false) String cookie) {

        return handleOperation("获取歌单分类标签",
                playlistService.getPlaylistTags(cookie),
                cookie != null ? "with_cookie" : "no_cookie");
    }
}