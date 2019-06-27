package com.boco.nscs.core.aop;

import cn.hutool.core.util.StrUtil;
import com.boco.nscs.core.annotion.RateLimit;
import com.boco.nscs.core.entity.kf.KFResponseUtil;
import com.boco.nscs.core.exception.NscsExceptionEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简单限流 通过计数器
 */
@Aspect
@Component
public class WsRateLimitAop {
    private  static Logger logger = LoggerFactory.getLogger(ServiceLogAop.class);
    private static ConcurrentHashMap<String, AtomicInteger> map = new ConcurrentHashMap<>();

    private AtomicInteger rateNum;
    @Pointcut("@annotation(com.boco.nscs.core.annotion.RateLimit)")
    private  void serviceLimit(){

    }

    @Around("serviceLimit()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Object obj = null;
        //获取拦截的方法名
        Signature sig = pjp.getSignature();
        MethodSignature msig = (MethodSignature) sig;
        //返回被织入增加处理目标对象
        Object target = pjp.getTarget();
        //为了获取注解信息
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        //获取注解信息
        RateLimit annotation = currentMethod.getAnnotation(RateLimit.class);
        String funName = annotation.name();
        if (StrUtil.isBlank(funName)){
            funName =currentMethod.getName();
        }
        //计数器
        int limitNum = annotation.limit();

        //获取rate计数器
        if(map.containsKey(funName)){
            rateNum =map.get(funName);
        }else {
            map.put(funName, new AtomicInteger());
            rateNum =map.get(funName);
        }

        int curNum =rateNum.get();
        if (curNum>=limitNum){
            logger.debug("服务[{}]当前请求{},请求过多 拒绝本次请求",funName,curNum);
                //超过限流
            return KFResponseUtil.error(NscsExceptionEnum.RateLimit).toJsonStr();
        } else{
                try {
                    rateNum.incrementAndGet();
                    obj = pjp.proceed();
                }finally {
                    rateNum.decrementAndGet();
                }
        }
        return obj;
    }
}
