package com.boco.nscs.core.entity.kf;

import com.alibaba.fastjson.annotation.JSONField;
import com.boco.nscs.core.entity.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//public class KFRespData<T> implements Serializable {
public class KFRespData<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @JSONField(ordinal = 1)
    private int respCode = Constants.RespCode_OK;
    @JSONField(ordinal = 2)
    private String respDesc = Constants.RespMsg_OK;
//    private List<T> result;
    @JSONField(ordinal = 3)
    private Map<String,Object> result;

    public KFRespData() {
        result = new HashMap<>();
        //result = new ArrayList<>();
    }

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

//    public List<T> getResult() {
//        return result;
//    }
//
//    public void setResult(List<T> result) {
//        this.result = result;
//    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
    public void setResult(String key,Object data){
        result.put(key,data);
    }
}
