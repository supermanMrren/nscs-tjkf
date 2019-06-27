package com.boco.nscs.core.security;

/**
 * Created by CC on 2017/9/28.
 */
public class JwtTokenInfo {
    /**
     * jwt token
     */
    private  String token;

    /**
     * 用于客户端混淆md5加密
     */
    private  String randomKey;

    //刷新token
    private String refreshToken;
    private Long expire;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRandomKey() {
        return randomKey;
    }

    public void setRandomKey(String randomKey) {
        this.randomKey = randomKey;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }
}
