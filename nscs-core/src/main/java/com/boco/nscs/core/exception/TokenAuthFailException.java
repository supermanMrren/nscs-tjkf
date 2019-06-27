package com.boco.nscs.core.exception;

/**
 * Created by CC on 2017/9/1.
 * Token认证失败
 */
public class TokenAuthFailException extends NscsException{
    public TokenAuthFailException() {
        super(NscsExceptionEnum.TOKEN_ERROR);
    }
}
