package com.boco.nscs.core.mon;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 操 on 2017/01/14.
 * pv记录拦截器
 */
@Order(9)
public class pvInterceptor implements HandlerInterceptor {
    private static Logger log = LoggerFactory.getLogger(pvInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ip = httpServletRequest.getRemoteAddr();
        String uri = httpServletRequest.getRequestURI();
        String cpath=httpServletRequest.getContextPath();
        if (StrUtil.isNotBlank(cpath)){
            uri = StrUtil.removePrefix(uri,cpath);
        }

        String requestType = httpServletRequest.getHeader("X-Requested-With");
        String requestMethod = httpServletRequest.getMethod();
        Boolean isAjax = false;
        if(StrUtil.isNotBlank(requestType) && "XMLHttpRequest".equals(requestType)) {
            isAjax = true;
        }
        else {
            isAjax = false;
        }
        //记录pv日志
        AppPVTool.addPv(ip,uri,requestMethod,isAjax);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
