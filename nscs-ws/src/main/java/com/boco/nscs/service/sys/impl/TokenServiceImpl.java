package com.boco.nscs.service.sys.impl;

import com.boco.nscs.core.security.AuthRequest;
import com.boco.nscs.service.sys.ITokenService;
import org.springframework.stereotype.Service;


@Service
public  class TokenServiceImpl implements ITokenService {

    private static String USER_NAME = "admin";

    @Override
    public boolean validate(AuthRequest req) {
        if (USER_NAME.equals(req.getUserName())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean update(String token) {
        return true;
    }
}