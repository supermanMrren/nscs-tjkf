package com.boco.nscs.core.exception;

/**
 * Created by CC on 2017/8/30.
 * 系统异常信息
 */
public enum NscsExceptionEnum {

    //-1 开头的为通用异常代码
    //其他为业务异常代码

    /**
     * 安全相关
     */
    AUTH_REQUEST_ERROR(1001,"认证失败"),
    TOKEN_ERROR(1002, "token验证失败"),
    TOKEN_EXPIRED(1003, "token过期"),
    SIGN_ERROR(1004, "签名验证失败"),
    REFRESH_TOKEN_ERROR(1005, "非法的RefreshToken"),

    //查询相关错误
    REQUEST_PARAM_NULL(1010,"请求参数不能为空"),
    REQUEST_PARAM_Error(1011,"请求参数格式不正确"),
    NotFound(1012,"未找到对应数据"),
    Query_ERROR(1013,"查询出错"),
    RateLimit(1014,"当前查询人数过多，请稍后再试"),

    //添加失败
    INSERT_ERROR(1015,"添加失败"),

    //清除失败
    CLEAR_ERROR(1016,"清除失败"),

    //文件处理相关
    FILE_READING_ERROR(1020,"文件读取失败!"),
    FILE_NOT_FOUND(1021,"文件未找到!"),

    //服务调用相关
    REQUEST_URL_ERROR(1030,"服务调用错误!"),

    //数据库相关
    SERVER_DB_ERROR(1040,"数据库错误"),


    //未知错误

    Config_Error(1090,"系统配置错误"),
    SERVER_ERROR(1099,"服务器内部异常");

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

    private String msg;
    private int code;

    NscsExceptionEnum(int code,String msg) {
        this.msg = msg;
        this.code = code;
    }

    public NscsException getException(){
        return new NscsException(this);
    }
}
