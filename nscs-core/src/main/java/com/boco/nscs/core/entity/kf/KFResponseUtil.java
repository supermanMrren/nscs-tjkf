package com.boco.nscs.core.entity.kf;

import com.boco.nscs.core.entity.Constants;
import com.boco.nscs.core.exception.NscsException;
import com.boco.nscs.core.exception.NscsExceptionEnum;

/**
 * Created by CC on 2017/8/31.
 * API执行返回结果
 */
public class KFResponseUtil {
    //测试用
    public static KFResponse none() {
        KFRespData data = new KFRespData();
        return success(data);
    }

    //返回成功
    public static KFResponse success(KFRespData data) {
        KFResponse response = new KFResponse();
        response.setRtnCode(Constants.RtnCode_OK);
        response.setRtnMsg(Constants.RtnMsg_OK);
        response.setObject(data);
        return response;
    }

    //返回失败
    public static KFResponse error(KFRespData data) {
        KFResponse response = new KFResponse();
        response.setRtnCode(Constants.RtnCode_OK);
        response.setRtnMsg(Constants.RtnMsg_OK);
        response.setObject(data);
        return response;
    }

    public static  KFResponse valideFail(String message) {
        return new KFResponse(NscsExceptionEnum.REQUEST_PARAM_Error.getCode(), message);
    }
    //系统错误结果
    public static  KFResponse error(String message) {
        return new KFResponse(Constants.RtnCode_Error, message);
    }
    public static  KFResponse error(NscsExceptionEnum errType) {
        return new KFResponse(errType.getCode(), errType.getMsg());
    }
    public static  KFResponse error(NscsException exception) {
        return new KFResponse(exception.getCode(), exception.getMsg());
    }
    //系统异常
    public static KFResponse unHandleError(){
        return error(NscsExceptionEnum.SERVER_ERROR);
    }
}
