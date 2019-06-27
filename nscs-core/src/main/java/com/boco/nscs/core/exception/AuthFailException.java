package com.boco.nscs.core.exception;

/**
 * Created by CC on 2017/5/9.
 * 认证失败异常
 */
public class AuthFailException extends NscsException {
    private static final long serialVersionUID = 1L;

    public AuthFailException() {
        super(NscsExceptionEnum.AUTH_REQUEST_ERROR);
    }
}
