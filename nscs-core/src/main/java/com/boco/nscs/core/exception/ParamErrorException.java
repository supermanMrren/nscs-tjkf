package com.boco.nscs.core.exception;

public class ParamErrorException extends NscsException{

    public ParamErrorException(String msg) {
        super(NscsExceptionEnum.REQUEST_PARAM_Error.getCode(), msg);
    }
}
