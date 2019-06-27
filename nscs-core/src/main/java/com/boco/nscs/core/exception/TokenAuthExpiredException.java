package com.boco.nscs.core.exception;

/**
 * Created by CC on 2017/9/1.
 */
public class TokenAuthExpiredException extends NscsException {
    public TokenAuthExpiredException() {
        super(NscsExceptionEnum.TOKEN_EXPIRED);
    }
}
