package com.lanfunoe.gocache.config;

import com.lanfunoe.gocache.constants.GocacheConstants;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "gocache.api")
public class GocacheApiConfig {

    private String baseUrl = GocacheConstants.LOGIN_USER_URL;

    private Integer appid;

    private Integer srcappid = 2919;

    private String clientver;

    private String platform = "Android";

    private Integer plat = 4;

    private String userAgent = "Android15-1070-11083-46-0-DiscoveryDRADProtocol-wifi";

    private boolean isLite = false;

    private int pageId;

    private int pid;

    private String pPageId;

    //todo 配置规整 yml 数据库

    @PostConstruct
    public void init() {
        isLite = "lite".equals(platform);
        if (isLite) {
            appid = GocacheConstants.Lite_APP_ID;
            clientver = GocacheConstants.LITE_CLIENT_VERSION;
            pageId = GocacheConstants.PAGE_ID_LITE;
            pid = GocacheConstants.PID_LITE;
            pPageId = GocacheConstants.PPAGE_ID_LITE;
        } else {
            appid = GocacheConstants.APP_ID_ANDROID;
            clientver = GocacheConstants.CLIENT_VERSION;
            pageId = GocacheConstants.PAGE_ID_MAIN;
            pid = GocacheConstants.PID_MAIN;
            pPageId = GocacheConstants.PPAGE_ID_MAIN;
        }
    }
}