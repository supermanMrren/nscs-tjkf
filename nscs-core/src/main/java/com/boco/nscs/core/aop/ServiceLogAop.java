package com.boco.nscs.core.aop;

import cn.hutool.json.JSONUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
@Order(3)
public class ServiceLogAop {

	private  static Logger logger = LoggerFactory.getLogger(ServiceLogAop.class);

	@Pointcut("execution(*  com.boco.nscs.service..*.*(..))")
	private  void allServiceMethod(){

	}

	@Before("allServiceMethod()")
	public void before(JoinPoint jp){
		List list=Arrays.asList(jp.getArgs());

		if (logger.isInfoEnabled()){
			logger.info("[ClassName:{}-{}]  args:{}",jp.getTarget().getClass().getName(),jp.getSignature().getName(),JSONUtil.toJsonStr(list));
		}
	}

	@Around("allServiceMethod()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable{
		Object result = null;
		long start = System.currentTimeMillis();
		//有返回参数 则需返回值
		result =  pjp.proceed();
		long end = System.currentTimeMillis();
		logger.info("Services Method:[{}] castTime:{}ms",pjp.getSignature().getName(), end - start);
		if (logger.isDebugEnabled()) {
			logger.debug("Services Method:[{}] result:{}", pjp.getSignature().getName(), result==null?"":JSONUtil.toJsonStr(result));
		}
		return result;
	}
}
