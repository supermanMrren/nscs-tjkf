package com.boco.nscs.core.entity;

import com.boco.nscs.core.exception.NscsException;
import com.boco.nscs.core.exception.NscsExceptionEnum;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * Created by CC on 2017/8/31.
 * API执行返回结果
 */
public class BizResponseUtil {
    private static String MSG_ExeERROR = "操作失败";
    private static String MSG_ExeOK = "操作成功";
    private static String MSG_ValidERROR = "数据验证失败";

    //返回单个结果
    public static <T> BizResponse<T> single(T data) {
        BizResponse<T> response = new BizResponse<>();
        response.setCode(Constants.RtnCode_OK);
        response.setData(data);
        return response;
    }
    //返回列表结果
    public static <T> BizResponse<List<T>> list(List<T> data) {
        BizResponse<List<T>> response = new BizResponse<>();
        response.setCode(Constants.RtnCode_OK);
        response.setData(data);
        return response;
    }
    //系统错误结果
    public static  BizResponse<String> error(String message) {
        return new BizResponse<>(Constants.RtnCode_Error, message);
    }
    public static  BizResponse<String> error(NscsExceptionEnum errType) {
        return new BizResponse<>(errType.getCode(), errType.getMsg());
    }
    public static  BizResponse<String> error(NscsException exception) {
        return new BizResponse<>(exception.getCode(), exception.getMsg());
    }

    //执行成功结果
    public static BizResponse<String> success(String message,String data){
        BizResponse<String> response =  new BizResponse<>();
        response.setCode(Constants.RtnCode_OK);
        response.setMsg(message);
        response.setData(data);
        return response;
    }
    public static <T> BizResponse<T> success(String message,T data){
        BizResponse<T> response =  new BizResponse<>();
        response.setCode(Constants.RtnCode_OK);
        response.setMsg(message);
        response.setData(data);
        return response;
    }
    //执行成功结果
    public static BizResponse<Object> success(Object data){
        BizResponse<Object> response =  new BizResponse<>();
        response.setCode(Constants.RtnCode_OK);
        response.setMsg(MSG_ExeOK);
        response.setData(data);
        return response;
    }
    public static BizResponse<String> success(String message){
        return success(message,"");
    }
    public static BizResponse<String> success(){
        return success(MSG_ExeOK,"");
    }

    //执行失败结果
    public static BizResponse<String> fail(String message,String data){
        BizResponse<String> response =  new BizResponse<>();
        response.setCode(Constants.RtnCode_Error);
        response.setMsg(message);
        response.setData(data);
        return response;
    }
    public static BizResponse<String> fail(String message){
        return fail(message,"");
    }

    public static BizResponse<String> fail(){
        return fail(MSG_ExeERROR,"");
    }

    public static BizResponse<String> validFail(BindingResult result){
        return fail(MSG_ValidERROR+":"+result.getFieldError().getDefaultMessage(),"");
    }
}
