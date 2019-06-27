package com.boco.nscs.core.controller;

import com.boco.nscs.core.entity.*;
import com.boco.nscs.core.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by CC on 2017/7/5.
 * 统一异常处理
 */
@RestControllerAdvice(assignableTypes = {BaseAPIController.class})
public class BaseRestExceptionHandler {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(BaseRestExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BizResponse defaultExpcetionHandler(Exception  e) {
        logger.error("系统异常:", e);
        return  BizResponseUtil.error(NscsExceptionEnum.SERVER_ERROR);
    }

    /**
     * 拦截异常
     */
    @ExceptionHandler(NscsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BizResponse ExpcetionHandler(NscsException e) {
        logger.warn("处理异常:{}", e.getMsg());
        return BizResponseUtil.error(e);
    }

    @ExceptionHandler({AuthFailException.class,TokenAuthFailException.class, TokenAuthExpiredException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public BizResponse authExpcetionHandler(NscsException e) {
        logger.warn("认证异常:{}", e.getMsg());
        return BizResponseUtil.error(e);
    }
}
