package com.lanfunoe.gocache.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class SignatureUtils {

    private static final String SIGN_KEY = "R6snCXJgbCaj9WFRJKefTMIFp0ey6Gza";
    private static final String SIGN_PARAMS_KEY = "OIlwieks28dk2k092lksi2UIkp";
    private static final String SIGN_PARAMS_KEY_LITE = "LnT6xpN3khm36zse0QzvmgTZ3waWdRSA";
    private static final String SIGN_CLOUD_KEY = "ebd1ac3134c880bda6a2194537843caa0162e2e7";
    private static final String SIGN_KEY_NORMAL = "57ae12eb6890223e355ccfcb74edf70d";
    private static final String SIGN_KEY_LITE = "185672dd44712f60bb1736df5a377e82";


    /**
     * 签名Key加密
     *
     * @param hash   哈希值
     * @param mid    mid值
     * @param userid 用户ID
     * @param appid  应用ID
     * @return 签名字符串
     */
    public static String signKey(String hash, String mid, String userid, String appid) {
        String appIdValue = appid != null ? appid : "1001";
        boolean isLite = "3116".equals(appIdValue);
        String signSecret = isLite ? SIGN_KEY_LITE : SIGN_KEY_NORMAL;
        String signString = hash + signSecret + appIdValue + mid + (userid != null ? userid : "0");
        return CryptoUtils.md5(signString);
    }

    /**
     * 签名Params Key加密
     *
     * @param data      数据
     * @param appid     应用ID
     * @param clientver 客户端版本
     * @return 签名字符串
     */
    public static String signParamsKey(String data, String appid, String clientver) {
        String appId = appid != null ? appid : "1001";
        String clientVer = clientver != null ? clientver : "12123";

        String signString = appId + SIGN_PARAMS_KEY + clientVer + data;
        return CryptoUtils.md5(signString);
    }


    /**
     * Web版本签名加密
     *
     * @param params 参数Map
     * @return 签名字符串
     */
    public static String signatureWebParams(Map<String, Object> params) {
        String str = "NVPh5oo715z5DIWAeQlhMDsWXXQV4hwt";

        // 先拼接所有key=value对
        List<String> paramPairs = new ArrayList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            paramPairs.add(entry.getKey() + "=" + entry.getValue());
        }

        // 然后对拼接后的字符串进行排序
        Collections.sort(paramPairs);

        // 最后连接所有参数
        String paramsString = String.join("", paramPairs);
        String signString = str + paramsString + str;
        return CryptoUtils.md5(signString);
    }

    /**
     * Android版本签名加密
     *
     * @param params 参数Map
     * @param data   额外数据
     * @return 签名字符串
     */
    public static String signatureAndroidParams(Map<String, Object> params, String data) {
        TreeMap<String, Object> sortedParams = new TreeMap<>(params);
        StringBuilder paramsString = new StringBuilder();

        for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
            Object value = entry.getValue();
            String valueStr = value instanceof Map ? value.toString() : String.valueOf(value);
            paramsString.append(entry.getKey()).append("=").append(valueStr);
        }

        String appId = params != null && params.get("appid") != null ? String.valueOf(params.get("appid")) : "";
        boolean isLite = "3116".equals(appId);
        String secret = isLite ? SIGN_PARAMS_KEY_LITE : SIGN_PARAMS_KEY;
        String signString = secret + paramsString.toString() + (data != null ? data : "") + secret;
        return CryptoUtils.md5(signString);
    }
}
