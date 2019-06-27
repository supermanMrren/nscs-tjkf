package com.boco.nscs.core.aop;

import cn.hutool.json.JSONUtil;
import com.boco.nscs.core.util.ToolUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class APILogAop {
    private  static Logger logger = LoggerFactory.getLogger(APILogAop.class);

    @Pointcut("execution(*  com.boco.nscs.controller.api..*.*(..))")
    private  void allServiceMethod(){

    }

    @Around("allServiceMethod()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        List<Object> args = Arrays.asList(pjp.getArgs());

        String className = pjp.getSignature().getDeclaringTypeName();
        String methodName = pjp.getSignature().getName();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = attributes.getRequest();
        String url = req.getRequestURI();
        String clientIp = ToolUtil.getClientIP(req);//req.getRemoteAddr();
        String httpMethod =req.getMethod();
        if (logger.isDebugEnabled()){            logger.info("[{} {}] ip:{} - args:{}",httpMethod,url,clientIp,JSONUtil.toJsonStr(args));
        }

        Object result = null;
        long start = System.currentTimeMillis();
        //有返回参数 则需返回值
        result =  pjp.proceed();
        long end = System.currentTimeMillis();
        logger.info("API:[{}-{}] - castTime:{}ms",className,methodName, end - start);
        if (logger.isDebugEnabled()) {
            logger.debug("API:[{}-{}] - result:{}", className,methodName, result==null?"":JSONUtil.toJsonStr(result));
        }
        return result;
    }
}
