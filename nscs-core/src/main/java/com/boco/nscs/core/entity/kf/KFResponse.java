package com.boco.nscs.core.entity.kf;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.boco.nscs.core.entity.Constants;

import java.io.Serializable;

public class KFResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    @JSONField(ordinal = 1)
    private int rtnCode= Constants.RtnCode_OK;;
    @JSONField(ordinal = 2)
    private String rtnMsg="";
    @JSONField(ordinal = 3)
    private KFRespData object;

    public int getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(int rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getRtnMsg() {
        return rtnMsg;
    }

    public void setRtnMsg(String rtnMsg) {
        this.rtnMsg = rtnMsg;
    }

    public KFRespData getObject() {
        return object;
    }

    public void setObject(KFRespData object) {
        this.object = object;
    }

    public KFResponse() {
    }

    public KFResponse(int rtnCode, String rtnMsg) {
        this.rtnCode = rtnCode;
        this.rtnMsg = rtnMsg;
    }

    public String toJsonStr(){
        return JSONObject.toJSONString(this);
    }
}
