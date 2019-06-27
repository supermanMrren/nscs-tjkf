package com.boco.nscs.service.hss.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.boco.nscs.common.XmlHandler;
import com.boco.nscs.common.utils.Configuration;
import com.boco.nscs.common.utils.FileUtil;
import com.boco.nscs.common.utils.HttpUtils;
import com.boco.nscs.core.entity.SearchCriteria;
import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.core.entity.kf.KFRespDataUtil;
import com.boco.nscs.core.exception.NscsException;
import com.boco.nscs.core.exception.NscsExceptionEnum;
import com.boco.nscs.entity.hss.HlrReq;
import com.boco.nscs.entity.hss.HlrSearch;
import com.boco.nscs.mapper.hss.IHlrMapper;
import com.boco.nscs.service.hss.IHlrService;
import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HlrServiceImpl implements IHlrService {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(HlrServiceImpl.class);

    @Autowired
    IHlrMapper hlrMapper;

    @Override
    public KFRespData qryHInfo(HlrReq req) {

            String hlrUrl = Configuration.getInstance().getValue("hlrServiceUrl");
            if (StrUtil.isBlank(hlrUrl)) {
                throw NscsExceptionEnum.Config_Error.getException();
            }
            logger.debug("HLR服务地址:{}", hlrUrl);
            String requestXml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <tem:HlrQuery>\n" +
                    "         <tem:p>\n" +
                    "            <tem:strOriNumber>"+req.getServNumber()+"</tem:strOriNumber>\n" +
                    "         </tem:p>\n" +
                    "      </tem:HlrQuery>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";
            logger.debug("获取hlr查询服务发送报文：{}",requestXml);
        try {
            long hlrStartTime = System.currentTimeMillis();
            String soapAction = "http://tempuri.org/HlrQuery";
            String respHlrString = HttpUtils.requesrHttpPost(hlrUrl, requestXml, soapAction);       //调用接口,传递参数

            //String respHlrString = FileUtil.getTxtInfo("config/hlrQuerySoap.txt");//测试用

            long hlrEndTime = System.currentTimeMillis();
            logger.debug("获取hlr查询服务返回报文：{},hlr查询服务耗时{}ms", respHlrString, hlrEndTime - hlrStartTime);
            if (StrUtil.isBlank(respHlrString)){
                throw new NscsException(NscsExceptionEnum.Query_ERROR,"查询失败，hlr查询服务数据为空！");
            }
            Document docReturn = HttpUtils.strXmlToDocument(respHlrString);                //解析成dom对象
            List<Map<String, String>> resultData = XmlHandler.getSoapResponse(docReturn);
            JSONArray hlrJsonArr = JSONUtil.parseArray(resultData);
            logger.debug("获取hlr查询服务hlrJsonArr：{}", hlrJsonArr);
            Map<String, Object> hssResp = getHssInfo(hlrJsonArr);
            //logger.debug("HLR服务返回结果：{}", hssResp);
            return KFRespDataUtil.success(hssResp);
        }catch (NscsException ex){
            if("文件读取失败!".equals(ex.getMsg())){
                throw new NscsException(NscsExceptionEnum.FILE_READING_ERROR,"查询失败，端局配置文件读取错误！");
            }else{
                throw new NscsException(NscsExceptionEnum.REQUEST_URL_ERROR,"查询失败，hlr查询服务调用错误！");
            }
        }catch (Exception ex){
            logger.warn("查询失败",ex);
            throw new NscsException(NscsExceptionEnum.Query_ERROR,"查询失败,"+ex.getMessage());
        }
    }

    @Override
    public KFRespData clearHlrHssInfo(HlrReq req) {
        String clearResult ="";
        try {
            String vlrUrl = Configuration.getInstance().getValue("vlrQueryUrl");
            if (StrUtil.isBlank(vlrUrl)) {
                throw NscsExceptionEnum.Config_Error.getException();
            }
            logger.debug("HLR服务地址:{}", vlrUrl);
            String servNumber = req.getServNumber();
            if(!servNumber.startsWith("86")){
                servNumber = "86"+servNumber;
            }
            String requestXml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <tem:GetVlrResult>\n" +
                    "         <tem:phone>"+servNumber+"</tem:phone>\n" +
                    "      </tem:GetVlrResult>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";
            long vlrStartTime = System.currentTimeMillis();
            logger.debug("获取vlr清除功能服务发送报文：{}",requestXml);
            String soapAction = "http://tempuri.org/GetVlrResult";
            String respVlrString = HttpUtils.requesrHttpPost(vlrUrl, requestXml,soapAction);       //调用接口,传递参数
            long vlrEndTime = System.currentTimeMillis();
            logger.debug("获取vlr清除功能服务返回报文：{},vlr清除功能服务耗时{}ms",respVlrString,vlrEndTime-vlrStartTime);
            if (StrUtil.isBlank(respVlrString)){
                throw new NscsException(NscsExceptionEnum.Query_ERROR,"查询失败，vlr清除功能服务调用数据为空！");
            }
            if(StrUtil.isNotBlank(respVlrString)){
                if(respVlrString.indexOf("Operation is successful")>=0 ||respVlrString.indexOf("清除成功")>=0 || respVlrString.indexOf("指令执行完毕")>=0){
                    clearResult = "1";
                }else{
                    clearResult = "2";
                }
            }else{
                throw NscsExceptionEnum.NotFound.getException();
            }
            return KFRespDataUtil.success("clearResult",clearResult);
        }catch (NscsException ex){
            throw new NscsException(NscsExceptionEnum.REQUEST_URL_ERROR,"查询失败，vlr清除功能服务调用错误！");
        }catch (Exception ex){
            logger.warn("查询失败",ex);
            throw new NscsException(NscsExceptionEnum.Query_ERROR,"查询失败,"+ex.getMessage());
        }
    }

    //查询十秒之内有无该号码的调用记录
    @Override
    public String qryHlrSearch(HlrReq req) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long currentTimeMillis = System.currentTimeMillis();
        long beforeTime = currentTimeMillis - 10000L;
        Date date = new Date(beforeTime);
        return hlrMapper.qryHlrSearch(req.getServNumber(),sdf.format(date));
    }

    //插入查询hlr接口记录
    @Override
    public void insertHrlSearch(HlrReq req) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HlrSearch hlrSearch = new HlrSearch();
        hlrSearch.setStaffId(req.getPubInfo().getStaffId());
        hlrSearch.setCallTime(sdf.format(new Date()));
        hlrSearch.setIncomNumber(req.getPubInfo().getIncomNumber());
        hlrSearch.setServNumber(req.getServNumber());
        hlrMapper.insertHrlSearch(hlrSearch);
    }

    //封装hlr查询服务中的数据
    public Map<String,Object> getHssInfo(JSONArray hlrJsonArr){
        String statusInfoStr = "";
        boolean iSBEpsLocState = false; //vlr是否已经附着4G网络
        boolean iS4GUser = false; //vlr是否4G签约用户
        boolean isAPN4G = false;
        int volteStatusCount = 0;//VOLTE数据影响因素计数
        int indexOf = -1;
        boolean isEOInfo = true;//是否为本省用户登记在本省的交换机上
        boolean isCalReminder = false;//是否有来电提醒
        boolean isTasCalReminder = false;
        InputStream inputStream = this.getClass().getResourceAsStream("/config/EOInfoConfig.xml");
        Map<String, String> EOInfo = FileUtil.getKeyValue(inputStream);
        if(EOInfo==null){
            throw NscsExceptionEnum.FILE_READING_ERROR.getException();
        }
        Map<String,Object> map = new LinkedHashMap<String,Object>();
        //欠费停机状态
        map.put("status","");
        //主动停机;用户处于欠费警告状态(限制呼出)
        map.put("statusInfo","");
        map.put("VLRinfo","3");
        //GPRS业务
        map.put("GPRS","");
        //vlr是否4G签约用户
        map.put("is4G","");
        //上网是否正常
        map.put("4GNomal","");
        //APN业务(4G)
        map.put("apnService","2");
        //上行速率(kbps)
        map.put("apnUplink","");
        //下行速率(kbps)
        map.put("apnDnlink","");
        //SGS关联状态
        map.put("sgsService","");
        //VOLTE是否开通
        map.put("Volte","");
        //判断是否是VOLTE通话
        map.put("VolteConnect","");
        //国内长途：是否正常列表
        map.put("nationLong",new LinkedHashMap<String,String>());
        LinkedHashMap nationLong = (LinkedHashMap) map.get("nationLong");
        nationLong.put("2G","2");
        nationLong.put("4G","");
        //国际长途：是否正常列表
        map.put("IDD",new LinkedHashMap<String,String>());
        LinkedHashMap idd = (LinkedHashMap) map.get("IDD");
        idd.put("2G","2");
        idd.put("4G","2");
        //国内漫游是否开通列表
        map.put("provinceroma",new LinkedHashMap<String,String>());
        LinkedHashMap provinceroma = (LinkedHashMap) map.get("provinceroma");
        provinceroma.put("2G","2");
        provinceroma.put("4G","2");
        //国内漫游上网是否开通列表
        map.put("provinceNetPlay",new LinkedHashMap<String,String>());
        LinkedHashMap provinceNetPlay = (LinkedHashMap) map.get("provinceNetPlay");
        provinceNetPlay.put("2G","2");
        provinceNetPlay.put("4G","2");
        //国际漫游是否开通
        map.put("internationalRoam",new LinkedHashMap<String,String>());
        LinkedHashMap internationalRoam = (LinkedHashMap) map.get("internationalRoam");
        internationalRoam.put("2G","2");
        internationalRoam.put("4G","2");
        //国际漫游上网是否开通
        map.put("internationalNetPlay",new LinkedHashMap<String,String>());
        LinkedHashMap internationalNetPlay = (LinkedHashMap) map.get("internationalNetPlay");
        internationalNetPlay.put("2G","2");
        internationalNetPlay.put("4G","2");
        //呼入是否限制
        map.put("Bbaoc",new LinkedHashMap<String,String>());
        LinkedHashMap bbaoc = (LinkedHashMap) map.get("Bbaoc");
        bbaoc.put("2G","2");
        bbaoc.put("4G","1");
        //呼出是否限制
        map.put("BBAIC",new LinkedHashMap<String,String>());
        LinkedHashMap bbaic = (LinkedHashMap) map.get("BBAIC");
        bbaic.put("2G","2");
        bbaic.put("4G","1");
        //多方通话：是否开通
        map.put("conferenceCal",new LinkedHashMap<String,String>());
        LinkedHashMap conferenceCal = (LinkedHashMap) map.get("conferenceCal");
        conferenceCal.put("2G","2");
        conferenceCal.put("4G","2");
        //呼叫等待：是否开通
        map.put("callsWaiting",new LinkedHashMap<String,String>());
        LinkedHashMap callsWaiting = (LinkedHashMap) map.get("callsWaiting");
        callsWaiting.put("2G","2");
        callsWaiting.put("4G","2");
        //呼叫保持：是否开通
        map.put("callHold",new LinkedHashMap<String,String>());
        LinkedHashMap callHold = (LinkedHashMap) map.get("callHold");
        callHold.put("2G","2");
        callHold.put("4G","2");
        //无条件转移：是否开通列表
        map.put("uncondTransfer",new LinkedHashMap<String,String>());
        LinkedHashMap uncondTransfer = (LinkedHashMap) map.get("uncondTransfer");
        uncondTransfer.put("2G","2");
        uncondTransfer.put("4G","2");
        //遇忙转移：是否开通列表
        map.put("busyTransfer",new LinkedHashMap<String,String>());
        LinkedHashMap busyTransfer = (LinkedHashMap) map.get("busyTransfer");
        busyTransfer.put("2G","2");
        busyTransfer.put("4G","2");
        //无应答呼叫转移：是否开通列表
        map.put("nackTransfer",new LinkedHashMap<String,String>());
        LinkedHashMap nackTransfer = (LinkedHashMap) map.get("nackTransfer");
        nackTransfer.put("2G","2");
        nackTransfer.put("4G","2");
        //用户不可及呼叫转移：是否开通列表
        map.put("impossibleTransfer",new LinkedHashMap<String,String>());
        LinkedHashMap impossibleTransfer = (LinkedHashMap) map.get("impossibleTransfer");
        impossibleTransfer.put("2G","2");
        impossibleTransfer.put("4G","2");
        //无条件转移：是否设置列表
        map.put("callTransferSet",new LinkedHashMap<String,String>());
        LinkedHashMap callTransferSet = (LinkedHashMap) map.get("callTransferSet");
        callTransferSet.put("2G","2");
        callTransferSet.put("4G","2");
        //遇忙转移：是否设置列表
        map.put("callWaitingT",new LinkedHashMap<String,String>());
        LinkedHashMap callWaitingT = (LinkedHashMap) map.get("callWaitingT");
        callWaitingT.put("2G","2");
        callWaitingT.put("4G","2");
        //无应答呼叫转移：是否设置列表
        map.put("callTransfer",new LinkedHashMap<String,String>());
        LinkedHashMap callTransfer = (LinkedHashMap) map.get("callTransfer");
        callTransfer.put("2G","2");
        callTransfer.put("4G","2");
        //用户不可及呼叫转移:是否设置列表
        map.put("impossiblesfer",new LinkedHashMap<String,String>());
        LinkedHashMap impossiblesfer = (LinkedHashMap) map.get("impossiblesfer");
        impossiblesfer.put("2G","2");
        impossiblesfer.put("4G","2");
        //短信接收业务：是否正常列表
        map.put("SMSRec",new LinkedHashMap<String,String>());
        LinkedHashMap smsRec = (LinkedHashMap) map.get("SMSRec");
        smsRec.put("2G","2");
        smsRec.put("4G","2");
        //短信发送业务：是否正常列表
        map.put("SMSSend",new LinkedHashMap<String,String>());
        LinkedHashMap smsSend = (LinkedHashMap) map.get("SMSSend");
        smsSend.put("2G","2");
        smsSend.put("4G","2");
        //彩铃业务:是否开通列表
        map.put("CRBT",new LinkedHashMap<String,String>());
        LinkedHashMap crbt = (LinkedHashMap) map.get("CRBT");
        crbt.put("2G","2");
        crbt.put("4G","2");
        //来电显示业务：是否正常列表
        map.put("callerDisplay",new LinkedHashMap<String,String>());
        LinkedHashMap callerDisplay = (LinkedHashMap) map.get("callerDisplay");
        callerDisplay.put("2G","2");
        callerDisplay.put("4G","2");
        //来电提醒是否正常：是否正常列表
        map.put("callRemind",new LinkedHashMap<String,String>());
        LinkedHashMap callRemind = (LinkedHashMap) map.get("callRemind");
        callRemind.put("2G","");
        callRemind.put("4G","");
        if(hlrJsonArr!=null&&hlrJsonArr.size()>0) {
            for (int i = 0; i < hlrJsonArr.size(); i++) {
                JSONObject tj = hlrJsonArr.getJSONObject(i);
                String key = tj.getStr("key");
                String value = tj.getStr("value");
                switch (key) {
                    //tas相关
                    case "TAS_cBOIC_OUT":
                        if ("有国际长途功能".equals(value)) {
                            idd.put("4G","1");
                        } else {
                            idd.put("4G","2");
                        }
                        break;
                    case "TAS_bBAIC_TJ":
                        if ("无限制".equals(value)) {
                            bbaoc.put("4G","2");
                        } else {
                            bbaoc.put("4G","1");
                        }
                        break;
                    case "TAS_bBAOC_TJ":
                        if ("无限制".equals(value)) {
                            bbaic.put("4G","2");
                        } else {
                            bbaic.put("4G","1");
                        }
                        break;
                    case "TAS_bHOLD_MPTY":
                        if ("FALSE".equals(value)) {
                            conferenceCal.put("4G","2");
                        } else {
                            conferenceCal.put("4G","1");
                        }
                        break;
                    case "TAS_cCallWait":
                        if (value.contains("已开启")) {
                            callsWaiting.put("4G","1");
                        } else {
                            callsWaiting.put("4G","2");
                        }
                        break;
                    case "TAS_bHOLD":
                        if ("TRUE".equals(value)) {
                            callHold.put("4G","1");
                        } else if("FALSE".equals(value)) {
                            callHold.put("4G","2");
                        }
                        break;
                    case "TAS_cCFU_TJ":
                        uncondTransfer.put("4G","2");
                        callTransferSet.put("4G","2");
                        if(value.contains("13800220309")){
                            isTasCalReminder = true;
                        }
                        if (value.contains("有此项功能")) {
                            uncondTransfer.put("4G","1");
                        } else if(value.contains("未设置呼转号码")){
                            callTransferSet.put("4G","1");
                        }
                        break;
                    case "TAS_cCFB_TJ":
                        busyTransfer.put("4G","2");
                        callWaitingT.put("4G","2");
                        if(value.contains("13800220309")){
                            isTasCalReminder = true;
                        }
                        if (value.contains("有此项功能")) {
                            busyTransfer.put("4G","1");
                        } else if(value.contains("未设置呼转号码")){
                            callWaitingT.put("4G","1");
                        }
                        break;
                    case "TAS_cCFNRY_TJ":
                        nackTransfer.put("4G","2");
                        callTransfer.put("4G","2");
                        if(value.contains("13800220309")){
                            isTasCalReminder = true;
                        }
                        if (value.contains("有此项功能")) {
                            nackTransfer.put("4G","1");
                        } else if(value.contains("未设置呼转号码")) {
                            callTransfer.put("4G","1");
                        }
                        break;
                    case "TAS_cCFNRC_TJ":
                        impossibleTransfer.put("4G","2");
                        impossiblesfer.put("4G","2");
                        if(value.contains("13800220309")){
                            isTasCalReminder = true;
                        }
                        if (value.contains("有此项功能")) {
                            impossibleTransfer.put("4G","1");
                        } else if(value.contains("未设置呼转号码")) {
                            impossiblesfer.put("4G","1");
                        }
                        break;
                    case "TAS_bCLIP":
                        if ("TRUE".equals(value)) {
                            callerDisplay.put("4G","1");
                        } else {
                            callerDisplay.put("4G","2");
                        }
                        break;
                    //hss相关
                    case "bBAOCbBAIC":
                        if ("FALSE".equals(value)){
                            map.put("status","1");
                        }else if("TRUE".equals(value)){
                            map.put("status","2");
                        }
                        break;
                    case "nSelfCacel":
                        if ("1".equals(value)){
                            statusInfoStr += "主动停机;";
                        }
                        break;
                    case "bBAOCbNOBAR":
                        if ("TRUE".equals(value)){
                            statusInfoStr += "用户处于欠费警告状态(限制呼出);";
                        }
                        break;
                    case "bGPRSFun":
                        if ("TRUE".equals(value)){
                            map.put("GPRS","1");
                        }else{
                            map.put("GPRS","2");
                        }
                        break;
                    case "cODBROAM":
                        provinceroma.put("2G","2");
                        internationalRoam.put("2G","2");
                        if(value.contains("国内漫游") || value.contains("国际漫游")){
                            provinceroma.put("2G","1");
                        }
                        if("国际漫游".equals(value)) {
                            internationalRoam.put("2G","1");
                        }
                        break;
                    case "cVOLTE_APN_ROAM":
                        provinceroma.put("4G","2");
                        internationalRoam.put("4G","2");
                        if (value.contains("国内漫游") || value.contains("国际漫游")){
                            provinceroma.put("4G","1");
                        }
                        if(value.contains("国际漫游")) {
                            internationalRoam.put("4G","1");
                        }
                        break;
                    case "cAPN_23G_ROAM":
                        provinceNetPlay.put("2G","2");
                        internationalNetPlay.put("2G","2");
                        if (value.contains("国内漫游") || value.contains("国际漫游")){
                            provinceNetPlay.put("2G","1");
                        }
                        if(value.contains("国际漫游")){
                            internationalNetPlay.put("2G","1");
                        }
                        break;
                    case "cAPN_4G_ROAM":
                        internationalNetPlay.put("4G","2");
                        provinceNetPlay.put("4G","2");
                        if (value.contains("国际漫游") ){
                            internationalNetPlay.put("4G","1");
                        }
                        if(value.contains("国内漫游") || value.contains("国际漫游")){
                            provinceNetPlay.put("4G","1");
                        }
                        break;
                    case "bEpsLocState":
                        if ("TRUE".equals(value)){
                            iSBEpsLocState = true;
                        }
                        break;
                    case "cEPS_USER":
                        if("是".equals(value.substring(0,1))){
                            iS4GUser = true;
                            map.put("is4G","1");
                        }else{
                            map.put("is4G","2");
                        }
                        break;
                    case "cAPN_4G_INFO":
                        if(value.toUpperCase().indexOf("CMNET")>0||value.toUpperCase().indexOf("CMWAP")>0){
                            isAPN4G = true;
                            map.put("apnService","1");
                        }else{
                            map.put("apnService","2");
                        }
                        break;
                    case "is_volte_apn":
                        if("TRUE".equals(value)){
                            volteStatusCount++;
                        }
                        break;
                    case "is_volte_ims":
                        if("TRUE".equals(value)){
                            volteStatusCount++;
                        }
                        break;
                    case "is_volte_anchored":
                        if("TRUE".equals(value)){
                            volteStatusCount++;
                        }
                        break;
                    case "ENUM_Resource":
                        if(StrUtil.isNotBlank(value)){
                            volteStatusCount++;
                        }
                        break;
                    case "cBOIC_IN":
                        if("可以拨打国内长途".equals(value)){
                            nationLong.put("2G","1");
                        }else if ("不可以拨打国内长途".equals(value)){
                            nationLong.put("2G","2");
                        }
                        break;
                    case "cBOIC_OUT":
                        if("可以拨打国际长途".equals(value)){
                            idd.put("2G","1");
                        }else if ("不可以拨打国际长途".equals(value)){
                            idd.put("2G","2");
                        }
                        break;
                    case "bBAIC_TJ":
                        if ("FALSE".equals(value)){
                            bbaoc.put("2G","2");
                        }else if("TRUE".equals(value)){
                            bbaoc.put("2G","1");
                        }
                        break;
                    case "bBAOC_TJ":
                        if ("FALSE".equals(value)){
                            bbaic.put("2G","2");
                        }else if("TRUE".equals(value)){
                            bbaic.put("2G","1");
                        }
                        break;
                    case "bHOLD_MPTY":
                        if ("FALSE".equals(value)){
                            conferenceCal.put("2G","2");
                        }else if("TRUE".equals(value)){
                            conferenceCal.put("2G","1");
                        }
                        break;
                    case "cMessage":
                        smsRec.put("2G","2");
                        smsSend.put("2G","2");
                        if("可以收短信,可以发短信".equals(value)){
                            smsRec.put("2G","1");
                            smsSend.put("2G","1");
                        }else if("可以收短信,不可以发短信".equals(value)){
                            smsRec.put("2G","1");
                        }else if("不可以收短信,可以发短信".equals(value)){
                            smsSend.put("2G","1");
                        }
                        break;
                    case "is_volte_high":
                        if(value.contains("不支持VoLTE短信业务")){
                            smsRec.put("4G","2");
                            smsSend.put("4G","2");
                        }else if(value.contains("支持VoLTE短信业务")){
                            smsRec.put("4G","1");
                            smsSend.put("4G","1");
                        }
                        if(value.contains("不支持VoLTE彩铃业务")){
                            crbt.put("4G","2");
                        }else if(value.contains("支持VoLTE彩铃业务")){
                            crbt.put("4G","1");
                        }
                        break;
                    case "bCRBT":
                        if ("FALSE".equals(value)){
                            crbt.put("2G","2");
                        }else if("TRUE".equals(value)) {
                            crbt.put("2G","1");
                        }
                        break;
                    case "bCLIP":
                        if ("FALSE".equals(value)){
                            callerDisplay.put("2G","2");
                        }else if("TRUE".equals(value)) {
                            callerDisplay.put("2G","1");
                        }
                        break;
                    case "cCallWait":
                        if (value.contains("已开通")||value.contains("已开启")){
                            callsWaiting.put("2G","1");
                        }else{
                            callsWaiting.put("2G","2");
                        }
                        break;
                    case "bHOLD":
                        if ("TRUE".equals(value)){
                            callHold.put("2G","1");
                        }else if("FALSE".equals(value)){
                            callHold.put("2G","2");
                        }
                        break;
                    case "cCFU_TJ":
                        uncondTransfer.put("2G","2");
                        callTransferSet.put("2G","2");
                        if(value.contains("13800220309")){
                            isCalReminder = true;
                        }
                        if (value.contains("有此项功能")||value.contains("已开通")||value.contains("已经开启")){
                            uncondTransfer.put("2G","1");
                        }else if(value.contains("呼转号码为")||"已开通,已激活".equals(value)||value.contains("转移的号码是")){
                            callTransferSet.put("2G","1");;
                        }
                        break;
                    case "cCFB_TJ":
                        busyTransfer.put("2G","2");
                        callWaitingT.put("2G","2");
                        if(value.contains("13800220309")){
                            isCalReminder = true;
                        }
                        if (value.contains("有此项功能")||value.contains("已开通")||value.contains("已经开启")){
                            busyTransfer.put("2G","1");
                        }else if(value.contains("呼转号码为")||"已开通,已激活".equals(value)||value.contains("转移的号码是")){
                            callWaitingT.put("2G","1");
                        }
                        break;
                    case "cCFNRY_TJ":
                        nackTransfer.put("2G","2");
                        callTransfer.put("2G","2");
                        if(value.contains("13800220309")){
                            isCalReminder = true;
                        }
                        if (value.contains("有此项功能")||value.contains("已开通")||value.contains("已经开启")){
                            nackTransfer.put("2G","1");
                        }else if(value.contains("呼转号码为")||"已开通,已激活".equals(value)||value.contains("转移的号码是")){
                            callTransfer.put("2G","1");
                        }
                        break;
                    case "cCFNRC_TJ":
                        impossibleTransfer.put("2G","2");
                        impossiblesfer.put("2G","2");
                        if(value.contains("13800220309")){
                            isCalReminder = true;
                        }
                        if (value.contains("有此项功能")||value.contains("已开通")||value.contains("已经开启")){
                            impossibleTransfer.put("2G","1");
                        }else if(value.contains("呼转号码为")||"已开通,已激活".equals(value)||value.contains("转移的号码是")){
                            impossiblesfer.put("2G","1");
                        }
                        break;
                    case "cVlrAddr":
                        if ((indexOf=value.indexOf("861"))>=0 && value.substring(indexOf,indexOf+10).length()==10){
                            String valueStr = value.substring(indexOf,indexOf+10);
                            Iterator<String> EOInfos = EOInfo.keySet().iterator();
                            while (EOInfos.hasNext()) {
                                String name = EOInfos.next();
                                if (EOInfo.get(name).equals(valueStr)) {
                                    map.put("VLRinfo","1");
                                    isEOInfo = false;
                                }
                            }
                            if(isEOInfo){
                                map.put("VLRinfo","2");
                            }
                        }else{
                            map.put("VLRinfo","2");
                        }
                        break;
                }
            }
        }
        if (iSBEpsLocState&&iS4GUser&&isAPN4G){
            map.put("4GNomal","1");
        }else{
            map.put("4GNomal","2");
        }
        if (volteStatusCount==4){
            map.put("Volte","1");
        }else{
            map.put("Volte","2");
        }
        if(isCalReminder){
            callRemind.put("2G","1");
        }else{
            callRemind.put("2G","2");
        }
        if(isTasCalReminder){
            callRemind.put("4G","1");
        }else{
            callRemind.put("4G","2");
        }
        map.put("statusInfo",statusInfoStr);
        return map;
    }
}
