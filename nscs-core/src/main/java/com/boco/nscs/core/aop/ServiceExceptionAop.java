package com.boco.nscs.core.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//@Aspect   //暂不使用 通过全局异常捕获
//@Component
public class ServiceExceptionAop {
	
	private static Logger log =LoggerFactory.getLogger(ServiceExceptionAop.class);

	 @AfterThrowing(pointcut="execution(* com.boco.nscs.service..*.*(..))", throwing="e")
	  public void afterThrowing(JoinPoint joinPoint, Exception e) {
		  log.warn("Class:[{}] Method:[{}]",joinPoint.getTarget().getClass(),joinPoint.getSignature().getName());
		  log.warn("service impl Error:",e);
	  }
}
