package com.boco.nscs.core.controller;

import cn.hutool.core.util.StrUtil;
import com.boco.nscs.core.exception.AuthFailException;
import com.boco.nscs.core.security.JwtProperties;
import com.boco.nscs.core.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by CC on 2017/8/30.
 */
@RestController
@RequestMapping("/")
public class BaseAPIController {
//logger
private static final Logger logger = LoggerFactory.getLogger(BaseAPIController.class);
    protected static String MSG_ExeERROR = "执行出错";
    protected static String MSG_ExeOK = "执行成功";

    @Autowired
    private HttpServletRequest request;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    JwtProperties jwtProperties;

    /**
     * @return 客户端IP
     */
    protected String getClientIp(){
        return request.getRemoteAddr();
    }

    /**
     * 获取当前用户ID
     * @return 登录用户id
     */
    protected String getCurUserId(){
        String token = getToken();
        String userid=jwtTokenUtil.getUsernameFromToken(token);
        if (StrUtil.isEmpty(userid)){
            throw  new AuthFailException();
        }
        return userid;
    }

    protected  String getToken(){
        //从header中获取token
        String authToken = request.getHeader(jwtProperties.getTokenName());

        //如果header中不存在token，则从参数中获取token
        if(StrUtil.isBlank(authToken)){
            authToken = request.getParameter("token");
        }
        return authToken;
    }
}
