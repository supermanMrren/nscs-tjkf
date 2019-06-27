package com.boco.nscs.service.sys;

import com.boco.nscs.core.security.AuthRequest;

/**
 * Created by CC on 2017/5/9.
 * Token服务
 */
public interface ITokenService {
    boolean validate(AuthRequest request);

    boolean update(String token);
}
