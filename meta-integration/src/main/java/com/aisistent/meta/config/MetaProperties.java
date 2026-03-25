package com.aisistent.meta.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Binds the "meta.*" properties from application.properties into a type-safe POJO.
 *
 * Properties:
 *   meta.app-id       – Facebook App ID
 *   meta.app-secret   – Facebook App Secret
 *   meta.redirect-uri – OAuth callback URL
 */
@Configuration
@ConfigurationProperties(prefix = "meta")
public class MetaProperties {

    private String appId;
    private String appSecret;
    private String redirectUri;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
