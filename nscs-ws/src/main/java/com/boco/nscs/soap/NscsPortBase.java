package com.boco.nscs.soap;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.boco.nscs.core.entity.kf.KFRequest;
import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.core.entity.kf.KFResponse;
import com.boco.nscs.core.entity.kf.PubInfo;
import com.boco.nscs.core.exception.NscsException;
import com.boco.nscs.core.exception.NscsExceptionEnum;
import com.boco.nscs.core.exception.ParamErrorException;
import com.boco.nscs.entity.netstat.CutoverReq;
import com.boco.nscs.entity.netstat.NetCoverReq;
import com.boco.nscs.entity.netstat.TfaAlarmReq;
import com.boco.nscs.entity.sys.WsCallLog;
import com.boco.nscs.service.sys.ISysService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

public abstract class NscsPortBase {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(NscsPortBase.class);

    @Autowired
    ISysService sysSer;
    /**
     * 记录接口调用日志
     * @param method 方法
     * @param req 请求
     * @param resp 响应
     */
    protected void addCallLog(String method,KFRequest req,KFResponse resp){
        addCallLog(method,req,resp,"");
    }

    /**
     * 记录接口调用日志
     * @param method 方法
     * @param req 请求
     * @param resp 响应
     * @param userNo 用户号码
     */
    protected void addCallLog(String method,KFRequest req,KFResponse resp,String userNo){
        try {
            WsCallLog log = new WsCallLog();
            if (req!=null) {
                if (req.getPubInfo()!=null) {
                    log.setCallStaffId(req.getPubInfo().getStaffId());
                }
                //log.setCallNo("");
                log.setIfName("NscsPort");
                log.setMethod(method);
                log.setCustomerNum(userNo);
                log.setStartTime(req.get_startTime());
            }
            if (resp != null) {
                log.setResultCode(resp.getRtnCode());
                log.setResultMsg(resp.getRtnMsg());
                if (resp.getObject() != null && resp.getObject().getResult() != null&&resp.getObject().getResult().size()>0) {
                    log.setIsGetData("true");
                }else{
                    log.setIsGetData("false");
                }
            }
            log.setFinishTime(new Date());
        }catch (Exception ex){
            logger.warn("add calllog error",ex);
        }
    }

    /**
     * 记录接口调用日志
     * @param method 方法
     * @param req 请求
     * @param resp 响应
     * @param ifName 调用接口名称
     */
    protected void addCallLog(String ifName,String method,KFRequest req,KFResponse resp){
        try {
            WsCallLog log = new WsCallLog();
            log.setIfName(ifName);
            log.setMethod(method);
            if (req!=null) {
                if (req.getPubInfo()!=null) {
                    log.setCallStaffId(req.getPubInfo().getStaffId());
                    log.setCallNo(req.getPubInfo().getIncomNumber());
                }
                log.setCustomerNum(req.getServNumber());
                log.setStartTime(req.get_startTime());
                if("releaseEngineerInfo".equals(method)){
                    String longLatitude = ((CutoverReq) req).getLongLatitude();
                    setLongLatitude(longLatitude,log);
                }else if("releaseFaultInfo".equals(method)){
                    String longLatitude = ((TfaAlarmReq) req).getLongLatitude();
                    setLongLatitude(longLatitude,log);
                }else if("qryCoverInfo".equals(method)){
                    String longLatitude = ((NetCoverReq) req).getLongLatitude();
                    setLongLatitude(longLatitude,log);
                }
            }else{
                log.setStartTime(new Date());
            }
            if (resp != null) {
                log.setResultCode(resp.getRtnCode());
                if(resp.getRtnCode()==0){
                    log.setResultMsg("");
                }else{
                    log.setResultMsg(resp.getRtnMsg());
                }
                if (resp.getObject() != null && resp.getObject().getResult() != null&&resp.getObject().getResult().size()>0) {
                    log.setIsGetData("true");
                }else{
                    log.setIsGetData("false");
                }
            }
            log.setFinishTime(new Date());
            sysSer.addWsCallLog(log);
        }catch (Exception ex){
            logger.warn("add calllog error",ex);
        }
    }

    private void setLongLatitude(String longLatitude,WsCallLog log){
        if(StrUtil.isNotBlank(longLatitude)){
            Double longitude = Double.parseDouble(longLatitude.substring(0,longLatitude.indexOf(",")));
            Double latitude = Double.parseDouble(longLatitude.substring(longLatitude.indexOf(",")+1));
            longitude = new BigDecimal(longitude).setScale(6,BigDecimal.ROUND_FLOOR).doubleValue();
            latitude = new BigDecimal(latitude).setScale(6,BigDecimal.ROUND_FLOOR).doubleValue();
            log.setLongitude(longitude);
            log.setLatitude(latitude);
        }
    }

    protected static <T extends KFRequest> T parseRequest(String params,Class<T> beanClass){
        if (StrUtil.isBlank(params)){
            throw NscsExceptionEnum.REQUEST_PARAM_NULL.getException();
        }
        JSONObject obj;
        try {
            obj = JSONUtil.parseObj(params);
        } catch (Exception ex) {
            logger.warn("解析Json参数出错",ex);
            throw NscsExceptionEnum.REQUEST_PARAM_Error.getException();
        }
        if (obj == null || obj.getStr("params") == null) {
                throw NscsExceptionEnum.REQUEST_PARAM_Error.getException();
        }
            T bean = JSONUtil.toBean(obj.getStr("params"), beanClass);
            if (bean == null) {
                throw NscsExceptionEnum.REQUEST_PARAM_Error.getException();
            }
            //在线客服规范改造
            if (bean.getPubInfo() == null) {
                throw NscsExceptionEnum.REQUEST_PARAM_Error.getException();
            }
            PubInfo pubInfo= bean.getPubInfo();
            //共用检查
            if (StrUtil.isBlank(pubInfo.getStaffId())){
                throw new ParamErrorException("调用工号[staffId]不能为空");
            }
            if(!Validator.isMobile(bean.getServNumber().trim())){
                throw new ParamErrorException("手机号码[servNumber]格式不正确");
            }
//            if(pubInfo.getRowsPerPage() >1000){
//                throw new ParamErrorException("每页查询不能大于1000条");
//            }
            return bean;

    }
}
