package com.boco.nscs.core.interceptor;

import cn.hutool.core.date.DateUtil;
import com.boco.nscs.core.security.JwtTokenUtil;
import com.boco.nscs.core.security.LoginUserUtils;
import com.boco.nscs.core.util.JsonUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 操 on 2017/01/14.
 * 访问日志记录拦截器
 */
public class AccessLogInterceptor implements HandlerInterceptor {
    private static Logger log = LoggerFactory.getLogger(AccessLogInterceptor.class);
//    @Autowired
//    JwtTokenUtil jwtTokenUtil;

    private final static String REQUEST_ID = "reqId";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ip = httpServletRequest.getRemoteAddr();
        String uri = httpServletRequest.getRequestURI();
        String cpath=httpServletRequest.getContextPath();
        if (StrUtil.isNotBlank(cpath)){
            uri = StrUtil.removePrefix(uri,cpath);
        }

        String querStr =httpServletRequest.getQueryString();
        if(StrUtil.isNotBlank(querStr)){
            uri += "?" + querStr;
        }
        String user="" ;

        //requestid
        MDC.put(REQUEST_ID, DateUtil.date().toString("yyyyMMddHHmmssSSS"));
//        if (uri.startsWith("api")){
//            user = jwtTokenUtil.getUsernameFromRequest(httpServletRequest);
//        }
//        else {
//            user=LoginUserUtils.GetCurUserId(httpServletRequest);
//        }

        String requestType = httpServletRequest.getHeader("X-Requested-With");
        if(StrUtil.isNotBlank(requestType) && "XMLHttpRequest".equals(requestType)) {
            log.debug("{} {} Ajax[{}] from {}", user, httpServletRequest.getMethod(), uri, ip);
            if (log.isDebugEnabled()) {
                log.debug("req Param:{}", JsonUtil.toJsonStr(httpServletRequest.getParameterMap()));
            }
        }
        else {
            log.debug("{} {} Request[{}] from {} ", user, httpServletRequest.getMethod(), uri, ip);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        MDC.remove(REQUEST_ID);
    }


    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
