package com.boco.nscs.core.entity.kf;

import cn.hutool.core.bean.BeanUtil;
import com.boco.nscs.core.entity.Constants;
import com.boco.nscs.core.exception.NscsException;
import com.boco.nscs.core.exception.NscsExceptionEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by CC on 2017/8/31.
 * API处理返回结果
 */
public class KFRespDataUtil {

    public static <T> KFRespData signle(T data) {
        Map<String, Object> map = BeanUtil.beanToMap(data);
        KFRespData resp = new KFRespData();
        resp.setResult(map);
        resp.setRespDesc(Constants.RespMsg_OK);
        resp.setRespCode(Constants.RespCode_OK);
        return resp;
    }

    public static <T> KFRespData success(Map<String,Object> map){
        KFRespData resp = new KFRespData();
        resp.setResult(map);
        resp.setRespDesc(Constants.RespMsg_OK);
        resp.setRespCode(Constants.RespCode_OK);
        return resp;
    }
    //返回成功
    public static <T> KFRespData success(String key,List<T> list) {
        KFRespData resp = new KFRespData();
        resp.setResult(key,list);
        resp.setRespDesc(Constants.RespMsg_OK);
        resp.setRespCode(Constants.RespCode_OK);
        return resp;
    }

    //返回成功
    public static <T> KFRespData success(String key,String value) {
        KFRespData resp = new KFRespData();
        resp.setResult(key,value);
        resp.setRespDesc(Constants.RespMsg_OK);
        resp.setRespCode(Constants.RespCode_OK);
        return resp;
    }

    //系统错误结果
    public static KFRespData error() {
        return error(Constants.RespMsg_Error);
    }
    public static KFRespData error(String message) {
        return error(Constants.RespCode_Error,message);
    }
    public static KFRespData error(int code, String message) {
        KFRespData resp = new KFRespData();
        resp.setRespDesc(message);
        resp.setRespCode(code);
        return resp;
    }
    public static  KFRespData error(NscsExceptionEnum errType) {
        return error(errType.getCode(), errType.getMsg());
    }
    public static  KFRespData error(NscsException exception) {
        return error(exception.getCode(), exception.getMsg());
    }
}
