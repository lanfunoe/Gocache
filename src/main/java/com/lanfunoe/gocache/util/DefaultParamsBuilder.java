package com.lanfunoe.gocache.util;

import com.lanfunoe.gocache.config.GocacheConfig;
import com.lanfunoe.gocache.model.UserSessionContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class DefaultParamsBuilder {

    private final GocacheConfig gocacheConfig;

    /**
     * 构建基础的默认参数集合
     * @return 包含所有默认参数的Map
     */
    public Map<String, Object> buildDefaultParams() {
        Map<String, Object> params = new HashMap<>();
        String dfid = "-";
        String mid = CryptoUtils.md5(dfid);
        String uuid = CryptoUtils.md5(dfid + mid);
        long clienttime = System.currentTimeMillis() / 1000;
        params.put("dfid", dfid);
        params.put("mid", mid);
        params.put("uuid", uuid);
        params.put("appid", gocacheConfig.getAppid());
        params.put("clientver", gocacheConfig.getClientver());
        params.put("clienttime", clienttime);

        return params;
    }

    /**
     * 构建带用户认证信息的默认参数
     *
     * @return 包含用户信息的默认参数Map
     */
    public Map<String, Object> buildDefaultParamsWithAuth(UserSessionContext session) {
        Map<String, Object> params = buildDefaultParams();

        if (StringUtils.isNotEmpty(session.token())) {
            params.put("token", session.token());
        }

        if (session.userId() != null) {
            params.put("userid", session.userId());
        }

        return params;
    }

    /**
     * 将默认参数合并到现有的参数Map中
     * 不会覆盖现有参数中已存在的键
     *
     * @param existingParams 现有参数Map
     * @return 合并后的参数Map
     */
    public Map<String, Object> mergeWithDefaultParams(Map<String, Object> existingParams) {
        if (existingParams == null) {
            return buildDefaultParams();
        }

        Map<String, Object> defaultParams = buildDefaultParams();
        Map<String, Object> mergedParams = new HashMap<>(defaultParams);

        // 现有参数优先，覆盖默认参数
        mergedParams.putAll(existingParams);

        return mergedParams;
    }

    /**
     * 将带认证的默认参数合并到现有的参数Map中
     *
     * @param existingParams 现有参数Map
     * @return 合并后的参数Map
     */
    public Map<String, Object> mergeWithDefaultParamsWithAuth(Map<String, Object> existingParams, UserSessionContext session) {
        if (existingParams == null) {
            return buildDefaultParamsWithAuth(session);
        }

        Map<String, Object> defaultParams = buildDefaultParamsWithAuth(session);
        Map<String, Object> mergedParams = new HashMap<>(defaultParams);

        // 现有参数优先，覆盖默认参数
        mergedParams.putAll(existingParams);

        return mergedParams;
    }

}
