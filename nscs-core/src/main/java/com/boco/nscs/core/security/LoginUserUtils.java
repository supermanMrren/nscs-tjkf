package com.boco.nscs.core.security;

import com.boco.nscs.core.entity.Constants;
import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by CC on 2017/1/13.
 * 登录用户信息
 */
public class LoginUserUtils {
    public static LoginUser GetCurUser(HttpServletRequest request){
        Object obj = request.getSession().getAttribute(Constants.CurrentUser);
        if (obj==null)
            return null;
        else  return (LoginUser)obj;
    }
    public static String GetCurUserId(HttpServletRequest request){
        Object obj = request.getSession().getAttribute(Constants.CurrentUserId);
        if (obj==null) {
            LoginUser user = GetCurUser(request);
            if (user == null)
                return "";
            else return user.getUserId();
        }
        else {
            return obj.toString();
        }
    }
    public  static Boolean CheckUserLogin(HttpServletRequest request){
        return  GetCurUser(request)==null? false:true;
    }

    public static  void UserLogin(HttpServletRequest request, LoginUser user){
        if(user==null)
            return;
        request.getSession().setAttribute(Constants.CurrentUser,user);
        request.getSession().setAttribute(Constants.CurrentUserId,user.getUserId());
    }

    public static void UserLogout(HttpServletRequest request){
        if (request.getSession()!=null){
            request.getSession().removeAttribute(Constants.CurrentUser);
            request.getSession().removeAttribute(Constants.CurrentUserId);
        }
    }
}
