package com.boco.nscs.soap.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.boco.nscs.common.utils.FileUtil;
import com.boco.nscs.core.annotion.RateLimit;
import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.core.entity.kf.KFResponse;
import com.boco.nscs.core.entity.kf.KFResponseUtil;
import com.boco.nscs.core.exception.NscsException;
import com.boco.nscs.core.exception.NscsExceptionEnum;
import com.boco.nscs.core.exception.ParamErrorException;
import com.boco.nscs.entity.hss.HlrReq;
import com.boco.nscs.entity.netstat.TransferFaultReq;
import com.boco.nscs.entity.netstat.CutoverReq;
import com.boco.nscs.entity.netstat.NetCoverReq;
import com.boco.nscs.entity.netstat.TfaAlarmReq;
import com.boco.nscs.entity.sms.SMSLogReq;
import com.boco.nscs.service.hss.IHlrService;
import com.boco.nscs.service.netstat.IAlarmService;
import com.boco.nscs.service.netstat.ICutoverService;
import com.boco.nscs.service.netstat.IDotService;
import com.boco.nscs.service.netstat.INetCoverService;
import com.boco.nscs.service.netstat.ISMSLogService;
import com.boco.nscs.soap.INscsPort;
import com.boco.nscs.soap.NscsPortBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@WebService(targetNamespace = "http://service.ws.nscs.boco.com/", name = "NscsService",serviceName = "NscsService", portName = "NscsPort",endpointInterface = "com.boco.nscs.soap.INscsPort")
public class NscsPortImpl extends NscsPortBase implements INscsPort {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(NscsPortImpl.class);
    @Autowired
    ICutoverService cutoverSer;
    @Autowired
    IAlarmService alarmSer;
    @Autowired
    INetCoverService netCoverSer;
    @Autowired
    IDotService dotSer;
    @Autowired
    ISMSLogService smsSer;
    @Autowired
    IHlrService hlrSer;

    /*@Override
    @WebMethod
    @RateLimit(limit = 2)  //限流
    public String hello(String name) {
        ThreadUtil.sleep(3000);
        return StrUtil.format("hello {} {}", name, DateUtil.now());
    }*/

    @Override
    @WebMethod
    public String releaseEngineerInfo(String param) {
        KFResponse resp = null;
        CutoverReq req = null;
        try {
            req = parseRequest(param, CutoverReq.class);
            checkReqNotNull(req.getReportTime(), req.getLongLatitude(),req.getInfluence());
            String province = req.getProvince();
            String city = req.getCity();
            String county = req.getCounty();
            String countyNew = CheckCountyAndCity(province, city, county);
            req.setCounty(countyNew);
            KFRespData data = cutoverSer.releaseEngineerInfo(req, county);
            List<String> engiList = (List) data.getResult().get("engiList");
            if(engiList.size()==0){
                data.setRespDesc("未查询到数据！");
                data.setResult(null);
            }
            resp = KFResponseUtil.success(data);
        } catch (NscsException ex) {
            logger.warn("查询出错", ex);
            resp = KFResponseUtil.error(ex);
        } catch (Exception ex) {
            logger.warn("查询出错", ex);
            resp = KFResponseUtil.unHandleError();
        }
        addCallLog("网络全专业工程信息查询接口","releaseEngineerInfo", req, resp);
        return resp.toJsonStr();
    }

    @Override
    @WebMethod
    public String releaseFaultInfo(String param) {
        KFResponse resp = null;
        TfaAlarmReq req = null;
        try {
            req = parseRequest(param, TfaAlarmReq.class);
            checkReqNotNull(req.getReportTime(), req.getLongLatitude(),req.getInfluence());
            String province = req.getProvince();
            String city = req.getCity();
            String county = req.getCounty();
            String countyNew = CheckCountyAndCity(province, city, county);
            req.setCounty(countyNew);
            KFRespData data = alarmSer.releaseFaultInfo(req, county);
            List<String> faultList = (List) data.getResult().get("faultList");
            if(faultList.size()==0){
                data.setRespDesc("未查询到数据！");
                data.setResult(null);
            }
            resp = KFResponseUtil.success(data);
        } catch (NscsException ex) {
            logger.warn("查询出错", ex);
            resp = KFResponseUtil.error(ex);
        } catch (Exception ex) {
            logger.warn("查询出错", ex);
            resp = KFResponseUtil.unHandleError();
        }
        addCallLog("网络全专业故障信息查询接口","releaseFaultInfo", req, resp);
        return resp.toJsonStr();
    }

    @Override
    @WebMethod
    public String qryCoverInfo(String param) {
        KFResponse resp = null;
        NetCoverReq req = null;
        try {
            req = parseRequest(param, NetCoverReq.class);
            checkReqNotNull(req.getReportTime(), req.getLongLatitude(),req.getInfluence());
            String province = req.getProvince();
            String city = req.getCity();
            String county = req.getCounty();
            String countyNew = CheckCountyAndCity(province, city, county);
            req.setCounty(countyNew);
            KFRespData data = netCoverSer.qryCoverInfo(req, county);
            List<String> poorCoverList = (List) data.getResult().get("poorCoverList");
            if(poorCoverList.size()==0){
                data.setRespDesc("未查询到数据！");
                data.setResult(null);
            }
            resp = KFResponseUtil.success(data);
        } catch (NscsException ex) {
            logger.warn("查询出错", ex);
            resp = KFResponseUtil.error(ex);
        } catch (Exception ex) {
            logger.warn("查询出错", ex);
            resp = KFResponseUtil.unHandleError();
        }
        addCallLog("2\\3\\4G覆盖信息查询接口","qryCoverInfo", req, resp);
        return resp.toJsonStr();
    }

    @Override
    @WebMethod
    @RateLimit(limit = 5)  //限流
    public String qrySMSLog(String param) {
        KFResponse resp = null;
        SMSLogReq req = null;
        try {
            req = parseRequest(param, SMSLogReq.class);
            String servNumber = req.getServNumber();
            if(!(servNumber.startsWith("1")&&servNumber.length()==11)){
                throw new ParamErrorException("请输入11位手机号码，校验失败，无法查询！");
            }
            checkReqTimeType(req.getStartDate().trim(),req.getEndDate().trim());
            KFRespData data = smsSer.qrySMSLog(req);
            List<String> smsList = (List) data.getResult().get("smsList");
            if(smsList.size()==0){
                throw new NscsException(NscsExceptionEnum.REQUEST_URL_ERROR,"查询失败，点对点短信查询服务调用错误！");
            }
            resp = KFResponseUtil.success(data);
        } catch (NscsException ex) {
            logger.warn("查询出错", ex);
            resp = KFResponseUtil.error(ex);
        } catch (Exception ex) {
            logger.warn("查询出错", ex);
            resp = KFResponseUtil.unHandleError();
        }
        addCallLog("短信日志查询接口","qrySMSLog", req, resp);
        return resp.toJsonStr();
    }

    @Override
    @WebMethod
    public String qryHInfo(String param) {
        KFResponse resp = null;
        HlrReq req = null;
        try {
            req = parseRequest(param, HlrReq.class);
            String id = hlrSer.qryHlrSearch(req);
            logger.debug("qryHlrSearch id :{}", id);
            if(StrUtil.isNotBlank(id)){
                throw new ParamErrorException("10秒内同一手机号只允许调用一次，请稍候再试!");
            }else{
                hlrSer.insertHrlSearch(req);
            }
            KFRespData data = hlrSer.qryHInfo(req);
            resp = KFResponseUtil.success(data);
        } catch (NscsException ex) {
            logger.warn("查询出错", ex);
            resp = KFResponseUtil.error(ex);
        } catch (Exception ex) {
            logger.warn("查询出错", ex);
            resp = KFResponseUtil.unHandleError();
        }
        addCallLog("用户签约信息查询接口","qryHInfo", req, resp);
        return resp.toJsonStr();
    }

    @Override
    @WebMethod
    public String clearHlrHssInfo(String param) {
        KFResponse resp = null;
        HlrReq req = null;
        try {
            req = parseRequest(param, HlrReq.class);
            KFRespData data = hlrSer.clearHlrHssInfo(req);
            resp = KFResponseUtil.success(data);
        } catch (NscsException ex) {
            logger.warn("清除失败", ex);
            resp = KFResponseUtil.error(ex);
        } catch (Exception ex) {
            logger.warn("清除失败", ex);
            resp = KFResponseUtil.unHandleError();
        }
        addCallLog("HLR/HSS信息清除接口","clearHlrHssInfo", req, resp);
        return resp.toJsonStr();
    }

    @Override
    @WebMethod
    public String transferFaultInfo(String param) {
        KFResponse resp = null;
        TransferFaultReq req = null;
        try {
            req = parseRequest(param, TransferFaultReq.class);
            checkDotParam(req);
            Double longitude = Double.parseDouble(req.getLongLatitude().substring(0,req.getLongLatitude().indexOf(",")));
            Double latitude = Double.parseDouble(req.getLongLatitude().substring(req.getLongLatitude().indexOf(",")+1));
            req.setLongitude(longitude);
            req.setLatitude(latitude);
            KFRespData data = dotSer.transferFaultInfo(req);
            resp = KFResponseUtil.success(data);
        } catch (NscsException ex) {
            logger.warn("打点出错", ex);
            if(ex.getCode()==1010||ex.getCode()==1011){
                resp = KFResponseUtil.error(ex);
            }else{
                KFRespData data = new KFRespData();
                data.setResult("transactionResult","2");
                resp = KFResponseUtil.error(data);
            }
        } catch (Exception ex) {
            logger.warn("打点出错", ex);
            resp = KFResponseUtil.unHandleError();
        }
        addCallLog("打点接口","transferFaultInfo", req, resp);
        return resp.toJsonStr();
    }

    //校验省，市，区县
    public String CheckCountyAndCity(String province, String city, String county) throws Exception {
        if(StrUtil.isBlank(county)){
            throw new ParamErrorException("区县不能为空，校验失败，无法查询！");
        }
        InputStream inputStream = this.getClass().getResourceAsStream("/config/countyConfig.xml");
        Map<String, String> countyMap =  FileUtil.getKeyValue(inputStream);
        //业务字段验证
        if (!(("天津".equals(city.trim()) || "天津市".equals(city.trim())) && ("天津".equals(province.trim()) || "天津市".equals(province.trim())))) {
            throw new ParamErrorException("省份或者地市校验失败，无法查询！");
        }
        String countyNew = "";
        if (StrUtil.isNotBlank(county)) {
            Iterator<String> countyKeys = countyMap.keySet().iterator();
            boolean isCheckCounty = true;
            while (countyKeys.hasNext()) {
                String key = countyKeys.next();
                if (key.equals(county.trim())) {
                    countyNew = countyMap.get(key);
                    isCheckCounty = false;
                }
            }
            if (isCheckCounty) {
                throw new ParamErrorException("所属区县校验失败，无法查询！");
            }
        }
        return countyNew;
    }

    //检测提交时间格式
    public void checkReqTimeType(String startDate,String endDate){
        String regex = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)\\s{1}((0[0-9])|(1[0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]{1}";
        if(StrUtil.isBlank(startDate)||StrUtil.isBlank(endDate)){
            throw new ParamErrorException("提交时间不能为空，校验失败，无法查询！");
        }
        if(!Validator.isMactchRegex(regex,startDate)||!Validator.isMactchRegex(regex,endDate)){
            throw new ParamErrorException("时间格式不正确，校验失败，无法查询！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long endTime = sdf.parse(endDate).getTime();
            long startTime = sdf.parse(startDate).getTime();
            long nowTime = System.currentTimeMillis();
            if (endTime<=startTime){
                throw new ParamErrorException("开始时间必须小于结束时间，校验失败，无法查询！");
            }
            if ((endTime - startTime)/1000 > 60 * 60 * 24 * 3) {
                throw new ParamErrorException("开始时间和结束时间的范围必须小于3天，校验失败，无法查询！");
            }
            if ((nowTime - startTime)/1000 > 60 * 60 * 24 * 30) {
                throw new ParamErrorException("开始时间必须小于当前时间30天，校验失败，无法查询！");
            }
        } catch (ParseException e) {
            throw new ParamErrorException("时间格式不正确，校验失败，无法查询！");
        }
    }

    //检测必填参数
    public void checkReqNotNull(String reportTime,String longLatitude,String influence){
        String regex = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)\\s{1}((0[0-9])|(1[0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]{1}";
        if(StrUtil.isBlank(reportTime)){
            throw new ParamErrorException("故障时间不能为空，校验失败，无法查询！");
        }
        if(!Validator.isMactchRegex(regex,reportTime.trim())){
            throw new ParamErrorException("时间格式不正确，校验失败，无法查询！");
        }
        if(StrUtil.isBlank(longLatitude)){
            throw new ParamErrorException("经纬度不能为空，校验失败，无法查询！");
        }
        if(!Validator.isNumber(longLatitude.trim().replaceAll("[,.]",""))){
            throw new ParamErrorException("经纬度只能为数字，校验失败，无法查询！");
        }
        if(StrUtil.isBlank(influence)){
            throw new ParamErrorException("影响范围不能为空，校验失败，无法查询！");
        }
    }

    //检测打点参数
    public void checkDotParam(TransferFaultReq req){
        String regex = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)\\s{1}((0[0-9])|(1[0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]{1}";
        if(StrUtil.isBlank(req.getBizDate())){
            throw new ParamErrorException("受理时间不能为空，校验失败，无法打点！");
        }
        if(!Validator.isMactchRegex(regex,req.getBizDate().trim())){
            throw new ParamErrorException("时间格式不正确，校验失败，无法打点！");
        }
        if(StrUtil.isBlank(req.getTransType())){
            throw new ParamErrorException("打点方式不能为空，校验失败，无法打点！");
        }
        if(StrUtil.isBlank(req.getCity())&&StrUtil.isBlank(req.getCounty())){
            throw new ParamErrorException("投诉地市、区县不能为空，校验失败，无法打点！");
        }
        if(StrUtil.isBlank(req.getInfluence())){
            throw new ParamErrorException("影响范围不能为空，校验失败，无法打点！");
        }
        if(StrUtil.isBlank(req.getLongLatitude())){
            throw new ParamErrorException("投诉位置经纬度不能为空，校验失败，无法打点！");
        }
        if(!Validator.isNumber(req.getLongLatitude().trim().replaceAll("[,.]",""))){
            throw new ParamErrorException("经纬度只能为数字，校验失败，无法查询！");
        }
        if(!req.getTransType().trim().equals("1")&&!req.getTransType().trim().equals("2")&&!req.getTransType().trim().equals("3")){
            throw new ParamErrorException("打点方式transType只能为1；2；3！");
        }
        if ("2".equals(req.getTransType())){
            if(!"1".equals(req.getBusiType().trim())&&!"2".equals(req.getBusiType().trim())&&!"3".equals(req.getBusiType().trim())){
                throw new ParamErrorException("打点方式transType为2时，打点类型busiType只能为1；2；3！");
            }
        }else if("1".equals(req.getTransType())||"3".equals(req.getTransType())){
            if(StrUtil.isNotBlank(req.getBusiType())){
                throw new ParamErrorException("打点方式transType为1或3时，打点类型busiType只能为空！");
            }
        }
    }

}

