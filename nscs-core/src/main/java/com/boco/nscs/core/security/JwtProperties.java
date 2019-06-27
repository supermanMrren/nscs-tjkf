package com.boco.nscs.core.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by CC on 2017/8/30.
 * Jwt相关配置
 */
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtProperties {
    //#http请求头
    private String tokenName = "accessToken";
    private String clientId="clientId";
    private String secret = "nscs#*!2017@&^G";
    private Long expiration = 604800L;  //7天 单位秒

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
