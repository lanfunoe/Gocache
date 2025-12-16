package com.lanfunoe.gocache.util;

import com.lanfunoe.gocache.config.GocacheConfig;
import com.lanfunoe.gocache.constants.GocacheConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * API加密工具类
 * 封装API特有的加密逻辑
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EncryptionUtils {

    private final GocacheConfig gocacheConfig;

    public String encryptRsaNoPaddingClienttimeToken(long clientTimeSeconds, String token) {
        String payload = String.format("{\"clienttime\":%d,\"token\":\"%s\"}", clientTimeSeconds, token);
        return CryptoUtils.rsaEncryptNoPadding(payload, gocacheConfig.isLite());
    }

    /**
     * 注意：部分 Android 接口会把“原始 JSON 字符串”纳入签名计算。
     */
    public String encryptRsaNoPaddingTokenClienttime(long clientTimeSeconds, String token) {
        String payload = String.format("{\"token\":\"%s\",\"clienttime\":%d}", token, clientTimeSeconds);
        return CryptoUtils.rsaEncryptNoPadding(payload, gocacheConfig.isLite());
    }

    /**
     * 构建登录密码加密参数
     *
     * @param username      用户名
     * @param password      密码
     * @param currentTime   当前时间戳（毫秒）
     * @return 加密后的请求参数
     */
    public Map<String, Object> buildLoginEncryptedParams(String username, String password, long currentTime) {
        String aesKey = CryptoUtils.generateAESKey();

        // 加密密码参数
        String encryptDataStr = String.format(
                "{\"pwd\":\"%s\",\"code\":\"%s\",\"clienttime_ms\":%d}",
                password, GocacheConstants.EMPTY_CODE, currentTime
        );
        String encryptedParams = CryptoUtils.aesEncrypt(encryptDataStr, aesKey).getStr();

        // RSA加密 clienttime_ms 和 key
        String rsaData = String.format("{\"clienttime_ms\":%d,\"key\":\"%s\"}", currentTime, aesKey);
        String encryptedRsa = CryptoUtils.rsaEncrypt(rsaData, gocacheConfig.isLite());

        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("plat", GocacheConstants.PLATFORM_PHONE);
        requestBody.put("support_multi", 1);
        requestBody.put("clienttime_ms", currentTime);
        requestBody.put("t1", 0);
        requestBody.put("t2", 0);
        requestBody.put("t3", "MCwwLDAsMCwwLDAsMCwwLDA=");
        requestBody.put("username", username);
        requestBody.put("params", encryptedParams);
        requestBody.put("pk", encryptedRsa);

        return requestBody;
    }


    /**
     * 构建用户歌单的加密参数
     *
     * @param token        用户token
     * @param userid       用户ID
     * @param page         页码
     * @param pagesize     每页大小
     * @return 加密后的参数Map
     */
    public Map<String, Object> buildUserPlaylistEncryptedParams(String token, String userid, Integer page, Integer pagesize) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userid", Long.parseLong(userid));
        requestBody.put("token", token);
        requestBody.put("total_ver", GocacheConstants.PLAYLIST_TOTAL_VER);
        requestBody.put("type", GocacheConstants.PLAYLIST_TYPE_USER);
        requestBody.put("page", page != null ? page : GocacheConstants.DEFAULT_PAGE);
        requestBody.put("pagesize", pagesize != null ? pagesize : GocacheConstants.DEFAULT_PAGE_SIZE);
        return requestBody;
    }

    /**
     * 构建用户云盘的加密参数
     *
     * @param token        用户token
     * @param userid       用户ID
     * @param dfid         dfid值
     * @param page         页码
     * @param pagesize     每页大小
     * @return 包含加密数据和密钥的Result对象
     */
    public CloudEncryptionResult buildUserCloudEncryptedParams(String token, String userid, String dfid,
                                                               Integer page, Integer pagesize) {
        String actualDfid = dfid != null ? dfid : "-";
        String mid = CryptoUtils.md5(actualDfid);

        // 构建数据Map并进行AES加密
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("page", page != null ? page : GocacheConstants.DEFAULT_PAGE);
        dataMap.put("pagesize", pagesize != null ? pagesize : GocacheConstants.DEFAULT_PAGE_SIZE);
        dataMap.put("getkmr", 1);

        String dataJson = dataMap.toString();
        CryptoUtils.AESResult aesResult = CryptoUtils.playlistAesEncrypt(dataJson);

        // RSA加密
        String rsaData = String.format("{\"aes\":\"%s\",\"uid\":\"%s\",\"token\":\"%s\"}",
                aesResult.getKey(), userid, token);
        String encryptedRsa = CryptoUtils.rsaEncrypt2(rsaData).toUpperCase();

        // 构建查询参数
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clienttime", System.currentTimeMillis() / 1000);
        queryParams.put("mid", mid);
        queryParams.put("key", SignatureUtils.signParamsKey(
                String.valueOf(System.currentTimeMillis() / 1000),
                "1005", // 默认appid
                "11040" // 默认clientver
        ));

        return CloudEncryptionResult.builder()
                .encryptedData(aesResult.getStr())
                .encryptedKey(aesResult.getKey())
                .encryptedRsa(encryptedRsa)
                .queryParams(queryParams)
                .build();
    }

    /**
     * 构建歌手关注的加密参数
     *
     * @param singerId     歌手ID
     * @param token        用户token
     * @param userid       用户ID
     * @param clientTime   客户端时间（秒）
     * @param isFollow     true=关注，false=取消关注
     * @return 加密后的参数Map
     */
    public Map<String, Object> buildArtistFollowEncryptedParams(String singerId, String token,
                                                                String userid, long clientTime,
                                                                boolean isFollow) {
        // AES加密
        Map<String, Object> encryptData = new HashMap<>();
        encryptData.put("singerid", Long.parseLong(singerId));
        encryptData.put("token", token);
        CryptoUtils.AESResult aesResult = CryptoUtils.playlistAesEncrypt(encryptData.toString());

        // RSA加密
        String rsaData = String.format("{\"clienttime\":%d,\"key\":\"%s\"}", clientTime, aesResult.getKey());
        String encryptedRsa = CryptoUtils.rsaEncrypt2(rsaData).toUpperCase();

        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("plat", GocacheConstants.PLATFORM_LITE);
        requestBody.put("userid", Long.parseLong(userid));
        requestBody.put("singerid", Long.parseLong(singerId));
        requestBody.put("source", GocacheConstants.SINGER_FOLLOW_SOURCE);
        requestBody.put("p", encryptedRsa);
        requestBody.put("params", aesResult.getStr());

        // 如果是关注操作，添加查询参数
        if (isFollow) {
            Map<String, Object> extras = new HashMap<>();
            extras.put("queryParams", Map.of("clienttime", clientTime));
            requestBody.put("_extras", extras);
        }

        return requestBody;
    }

    /**
     * 构建音乐播放权限检查参数
     *
     * @param hashArray    哈希数组
     * @param albumIdArray 专辑ID数组
     * @param appid        应用ID
     * @param clientver    客户端版本
     * @return 加密后的参数Map
     */
    public Map<String, Object> buildMusicPrivilegeEncryptedParams(String[] hashArray, String[] albumIdArray,
                                                                  int appid, String clientver) {
        // 构建资源列表
        java.util.List<Map<String, Object>> resourceList = new java.util.ArrayList<>();
        for (int i = 0; i < hashArray.length; i++) {
            Map<String, Object> resource = new HashMap<>();
            resource.put("type", GocacheConstants.RESOURCE_TYPE_AUDIO);
            resource.put("page_id", 0);
            resource.put("hash", hashArray[i].trim());
            resource.put("album_id", albumIdArray.length > i ? Long.parseLong(albumIdArray[i].trim()) : 0);
            resourceList.add(resource);
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("appid", appid);
        requestBody.put("area_code", 1);
        requestBody.put("behavior", GocacheConstants.BEHAVIOR_PLAY);
        requestBody.put("clientver", clientver);
        requestBody.put("need_hash_offset", 1);
        requestBody.put("relate", 1);
        requestBody.put("support_verify", 1);
        requestBody.put("resource", resourceList);
        requestBody.put("qualities", java.util.Arrays.asList("128", "320", "flac", "high", "viper_atmos", "viper_tape", "viper_clear"));

        return requestBody;
    }

    /**
     * 云盘加密结果封装类
     */
    @lombok.Builder
    @lombok.Data
    public static class CloudEncryptionResult {
        private String encryptedData;      // AES加密后的数据
        private String encryptedKey;       // AES密钥
        private String encryptedRsa;       // RSA加密后的密钥
        private Map<String, Object> queryParams; // 查询参数
    }
}