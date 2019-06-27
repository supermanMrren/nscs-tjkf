package com.boco.nscs.core.interceptor;

import com.boco.nscs.core.annotion.Anonymous;
import com.boco.nscs.core.exception.TokenAuthExpiredException;
import com.boco.nscs.core.exception.TokenAuthFailException;
import com.boco.nscs.core.security.JwtProperties;
import com.boco.nscs.core.security.JwtTokenUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by CC on 2017/1/13.
 * rest鉴权
 */
public class RestAuthInterceptor extends HandlerInterceptorAdapter {
    private  static Logger log = LoggerFactory.getLogger(RestAuthInterceptor.class);

    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    //环境变量
    @Autowired
    private Environment env;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();

        //检查是否开发环境
//        //开发环境不检查
//        if (Constants.DEV_PROFILES.equals(env.getProperty("spring.profiles.active"))) {
//            return true;
//        }

        //排除 options 不验证
        //log.debug("[{}]-{}",request.getMethod(),uri);
        if (request.getMethod().equalsIgnoreCase("OPTIONS")){
            return true;
        }

        //获取注解
        if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
            Anonymous allAnonymous= ((HandlerMethod) handler).getMethodAnnotation(Anonymous.class);
            if(allAnonymous!=null){
                //允许匿名访问
                return true;
            }
        }

        //从header中获取token
        String authToken = jwtTokenUtil.getRequestToken(request);

        //token为空
        if(StrUtil.isBlank(authToken)){
            log.warn("{} {} from {} 认证失败！ ",request.getMethod(),uri,ip);
            throw new TokenAuthFailException();
        }

        //检查token 合法性
        if (!jwtTokenUtil.validateToken(authToken,ip)) {
            log.warn("{} {} from {} 认证失败或过期！ ",request.getMethod(),uri,ip);
            throw new TokenAuthExpiredException();
        }

        //后续处理-api对外能力平台
        //注册
        //  提供service_code+secret注册，返回进行rsa加密 生成 sign
        //  同时每个service_code 分配对应的api权限列表
        //校验
        //  每次调用传入service_code 与 sign 进行认证
        //  根据访问的 URL+httpmethod与api权限列表进行鉴权

        //获取用户信息
        //jwtTokenUtil.getUsernameFromToken(authToken);

        return true;
    }
}
