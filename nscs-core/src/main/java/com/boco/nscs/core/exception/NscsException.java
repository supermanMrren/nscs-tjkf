package com.boco.nscs.core.exception;

/**
 * Created by CC on 2017/5/9.
 * 异常信息
 */
public class NscsException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code=-1;

    public NscsException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public NscsException(int code,String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }
    public NscsException(String msg,Exception ex) {
        super(msg,ex);
        this.msg = msg;
    }
    public  NscsException(NscsExceptionEnum expEnum){
        super(expEnum.getMsg());
        this.code = expEnum.getCode();
        this.msg =expEnum.getMsg();
    }

    public  NscsException(NscsExceptionEnum expEnum,String msg){
        super(expEnum.getMsg());
        this.code = expEnum.getCode();
        this.msg =msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
