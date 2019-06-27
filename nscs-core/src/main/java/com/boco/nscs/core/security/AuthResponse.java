package com.boco.nscs.core.security;


import java.io.Serializable;

/**
 * Created by CC on 2017/8/30.
 * 认证结果
 */
public class AuthResponse implements Serializable  {
    private static final long serialVersionUID = 1250166508152483573L;

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

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    private Long expire;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public AuthResponse(String token, String randomKey,String refreshToken) {
        this.token = token;
        this.randomKey = randomKey;
        this.refreshToken = refreshToken;
    }
    
    public AuthResponse() {
    }

    public String getToken() {
        return this.token;
    }

    public String getRandomKey() {
        return randomKey;
    }
}
