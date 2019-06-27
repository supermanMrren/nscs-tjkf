package com.boco.nscs.core.interceptor;

import com.boco.nscs.core.annotion.Anonymous;
import com.boco.nscs.core.security.LoginUser;
import com.boco.nscs.core.security.LoginUserUtils;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by CC on 2017/1/13.
 */

public class AuthInterceptor extends HandlerInterceptorAdapter {


    private  static Logger log = LoggerFactory.getLogger(AuthInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();

        if (request.getMethod().equalsIgnoreCase("OPTIONS")){
            return true;
        }

        String cpath=request.getContextPath();
        if (StrUtil.isNotBlank(cpath)){
            uri = StrUtil.removePrefix(uri,cpath);
        }

        //方式1：url过滤方式
        String[] noFilters = new String[] {
                "/login","/logout","/error","/error/500","/error/404",
                "/logout",
                "/modifyPassword","/swagger-resources",
                "/common","/init","about","/"};
        if(ArrayUtil.contains(noFilters,uri)){
            //url不校验
            return true;
        }

        //修改密码 单独校验
        String[] noFilters1 = new String[] {"/modifyPassword","/sys/checkOldPassword","/sys/modifyPassword"};
        if (ArrayUtil.contains(noFilters1,uri)){
            //检查 用户id
            //首次登录时 只有UserId 不能通过session中CurUser获取登录信息 只能通过UserId校验
            String userId = LoginUserUtils.GetCurUserId(request);
            if (StrUtil.isNotEmpty(userId))
                return true;  //登录成功
            else
                return false;
        }

        //方式2：获取匿名注解
        if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
            Anonymous allAnonymous= ((HandlerMethod) handler).getMethodAnnotation(Anonymous.class);
            if(allAnonymous!=null){
                //允许匿名访问
                return true;
            }
        }

        //校验是否登录
        LoginUser user= LoginUserUtils.GetCurUser(request);
        if (user!=null)
            return true;  //登录成功

        log.warn("{} {} from {} 认证失败！ ",request.getMethod(),uri,ip);
        AuthFail(request,response);
        return false;
    }

    private void AuthFail(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String requestType = request.getHeader("X-Requested-With");
        if(StrUtil.isNotBlank(requestType) && "XMLHttpRequest".equals(requestType)){
            //Ajax 请求
            //BizResponseUtil.error(Constants.Msg_FAIL_Authentication);

            response.setStatus(401);  //未授权
        }
        else{
            //页面
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
