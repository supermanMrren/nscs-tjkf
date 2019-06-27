package com.boco.nscs.core.entity;

import cn.hutool.json.JSONUtil;

/**
 * 执行结果基类
 */
public abstract class ResultBase {
	//0 失败 1 成功  -1 异常
    //Constants
	private int code =Constants.RtnCode_OK;
	//信息 失败时不能为空
	private String msg="";

	public ResultBase(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

    public ResultBase(String msg) {
        this.msg = msg;
    }

    public ResultBase() {
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
	
	public String toJsonStr(){
		return JSONUtil.toJsonStr(this);
	}
}
