package com.lanfunoe.gocache.config;

import com.lanfunoe.gocache.constants.GocacheConstants;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "gocache.api")
public class GocacheConfig {

    private String baseUrl = GocacheConstants.LOGIN_USER_URL;

    private Integer appid;

    private Integer srcappid = 2919;

    private String clientver;

    private String platform = "Android";

    private Integer plat = 4;

    private String userAgent = "Android15-1070-11083-46-0-DiscoveryDRADProtocol-wifi";

    private boolean isLite = false;

    @PostConstruct
    public void init() {
        isLite = "lite".equals(platform);
        if (appid == null) {
            appid = isLite ? GocacheConstants.Lite_APP_ID : GocacheConstants.APP_ID_ANDROID;
        }
        if (clientver == null) {
            clientver = isLite ? GocacheConstants.LITE_CLIENT_VERSION : GocacheConstants.CLIENT_VERSION;
        }
    }
}