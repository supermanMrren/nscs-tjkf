package com.boco.nscs.core.entity;

import cn.hutool.json.JSONUtil;

/**
 * Created by CC on 2017/8/31.
 * 业务处理结果
 */
public class BizResponse<T> {
    //1 成功  小于0为 异常
    //Constants
    private int code =Constants.RtnCode_OK;
    //信息 失败时不能为空
    private String msg="";

    public BizResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BizResponse(String msg) {
        this.msg = msg;
    }

    public BizResponse() {
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 转换当前对象为JsonStr
     * @return
     */
    public String toJsonStr(){
        return JSONUtil.toJsonStr(this);
    }

    /**
     * 转换data数据为JsonStr
     * @return
     */
    public String toDataJsonStr(){
        if (data== null)
            return "";
        else
            return JSONUtil.toJsonStr(this.data);
    }

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
