package com.boco.nscs.core.entity;

/**
 * Created by CC on 2017/1/13.
 */
public final class Constants {
    //加密设置
    //md5,sha-1,sha-256
    public static final String SecHashAlgorithmName ="MD5";
    public static final Integer SecHashIterations =2;
    //平台当前用户
    public static final String CurrentPlatUser = "CurrentPlatUser";
    public final static String CurrentPlatUserId="CurPlatUserId";
    public static final String SysAdminUserId = "admin";
    //当前系统用户
    public final static String CurrentUserId="CurUserId";
    public static final String CurrentUserName = "CurrentUserName";
    public static final String CurrentUser = "CurrentUser";
    public static final String CurrentModuleID = "CurrentModuleID";
    public static final String CurrentPageSourceID = "CurrentPageSourceID";
    public static final String CurrentUserCredentials = "CurrentUserCredentials";
    public static final String SysAdminRoleId = "administrator";

    //错误信息
    public final static int RtnCode_Error=1;
    public final static int RtnCode_OK=0;
    public final static String RtnMsg_OK="成功!";
    public final static String RtnMsg_Error="失败!";
    public final static int RespCode_OK=0;
    public final static String RespMsg_OK="success";
    public final static int RespCode_Error=1;
    public final static String RespMsg_Error="error";


    //认证
    public final static String Msg_FAIL_Authentication ="你还未登录或登录过期,请重新登录";
    //鉴权
    public final static String Msg_FAIL_Authorization ="对不起，你没有访问该资源的权限";

    //API
    public final static String API_CurrentUserId="APIUserId";

    public final static String Rtn_Message_ExeERROR="执行出错";

}
