package com.boco.nscs.service.netstat.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.boco.nscs.common.XmlHandler;
import com.boco.nscs.common.utils.Configuration;
import com.boco.nscs.common.utils.FileUtil;
import com.boco.nscs.common.utils.HttpUtils;
import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.core.entity.kf.KFRespDataUtil;
import com.boco.nscs.core.exception.NscsException;
import com.boco.nscs.core.exception.NscsExceptionEnum;
import com.boco.nscs.core.service.KFBaseServiceImpl;
import com.boco.nscs.entity.sms.RelSmsInfo;
import com.boco.nscs.entity.sms.SMSLogInfo;
import com.boco.nscs.entity.sms.SMSLogReq;
import com.boco.nscs.mapper.sms.ISMSLogMapper;
import com.boco.nscs.service.netstat.ISMSLogService;
import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by rc on 2019/5/16.
 */
@Service
public class SMSLogServiceImpl extends KFBaseServiceImpl<ISMSLogMapper,RelSmsInfo> implements ISMSLogService {

    //logger
    private static final Logger logger = LoggerFactory.getLogger(SMSLogServiceImpl.class);
    @Override
    protected String getResultKey() {
        return "smsList";
    }

    @Autowired
    private ISMSLogMapper smsLogMapper;

    @Override
    public KFRespData qrySMSLog(SMSLogReq req) {
        if (req==null){
            throw NscsExceptionEnum.REQUEST_PARAM_NULL.getException();
        }
        List<SMSLogInfo> smsLogInfoList = new ArrayList<SMSLogInfo>();
        List<RelSmsInfo>  smsInfoList = smsLogMapper.getSmsLogInfo();
        String smcID = "";
        if(smsInfoList.size()>0){
            for (RelSmsInfo relSmsInfo : smsInfoList){
                smcID += relSmsInfo.getServiceModuleIfId()+',';
            }
            smcID = smcID.substring(0,smcID.length()-1);
        }
        String status = req.getStatus().trim();
        List<SMSLogInfo> qrysmsCalledNo = null;//查询内存中为接收号码的集合
        List<SMSLogInfo> qrysmsCalledNoDB = null;//查询数据库中为接收号码的集合
        List<SMSLogInfo> qrysmsCallNo = null;//查询内存中为发送号码的集合
        List<SMSLogInfo> qrysmsCallNoDB = null;//查询数据中为发送号码的集合
        if("1".equals(status)){
            qrysmsCallNo = qrysms(req.getServNumber(), "", req.getStartDate(), req.getEndDate(), smcID, "0");
            qrysmsCallNoDB = qrysms(req.getServNumber(),"",req.getStartDate(),req.getEndDate(),smcID,"1");
            qrysmsCalledNo = qrysms("",req.getServNumber(),req.getStartDate(),req.getEndDate(),smcID,"0");
            qrysmsCalledNoDB = qrysms("",req.getServNumber(),req.getStartDate(),req.getEndDate(),smcID,"1");
            addList(smsLogInfoList,qrysmsCallNo);
            addList(smsLogInfoList,qrysmsCallNoDB);
            addList(smsLogInfoList,qrysmsCalledNo);
            addList(smsLogInfoList,qrysmsCalledNoDB);
        }else if ("2".equals(status)){
            qrysmsCallNo = qrysms(req.getServNumber(),"",req.getStartDate(),req.getEndDate(),smcID,"0");
            qrysmsCallNoDB = qrysms(req.getServNumber(),"",req.getStartDate(),req.getEndDate(),smcID,"1");
            addList(smsLogInfoList,qrysmsCallNo);
            addList(smsLogInfoList,qrysmsCallNoDB);
        }else if ("3".equals(status)){
            qrysmsCalledNo = qrysms("",req.getServNumber(),req.getStartDate(),req.getEndDate(),smcID,"0");
            qrysmsCalledNoDB = qrysms("",req.getServNumber(),req.getStartDate(),req.getEndDate(),smcID,"1");
            addList(smsLogInfoList,qrysmsCalledNo);
            addList(smsLogInfoList,qrysmsCalledNoDB);
        }
        //logger.debug("query list:{}", JSONUtil.toJsonStr(smsLogInfoList));
        return KFRespDataUtil.success(getResultKey(), smsLogInfoList);
    }

    public List<SMSLogInfo> qrysms(String callNo,String calledNo,String startTime,String endTime,String smcID,String flag){
        try {
            String smsQueryUrl = Configuration.getInstance().getValue("smsQueryUrl");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (StrUtil.isBlank(smsQueryUrl)) {
                throw NscsExceptionEnum.Config_Error.getException();
            }
            logger.debug("点对点短信查询smsQueryUrl:{}", smsQueryUrl);
            if(StrUtil.isNotBlank(callNo)){
                callNo = "86"+callNo;
            }
            if (StrUtil.isNotBlank(calledNo)){
                calledNo = "86"+calledNo;
            }
            if("0".equals(flag)){
                startTime = "";
                endTime = "";
            }else{
                startTime = sdf.format(sdf2.parse(startTime));
                endTime = sdf.format(sdf2.parse(endTime));
                smcID = "01,02,03,04,05";
            }
            String requestXml ="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sms=\"http://sms.huawei.com/smsinterface\">\n" +
                    "    <soapenv:Header>\n" +
                    "        <sms:Password>LRKWI0/PtOVZcAPG7ug9EA==</sms:Password>\n" +
                    "        <sms:Username>kefuceshi</sms:Username>\n" +
                    "    </soapenv:Header>\n" +
                    "    <soapenv:Body>\n" +
                    "        <sms:SmsLogRecordQuery>\n" +
                    "            <sms:SmsLogRecordQueryRequest>\n" +
                    "                <sms:RequestMessage>\n" +
                    "                    <sms:OrgAddr>"+callNo+"</sms:OrgAddr>\n" +
                    "                    <sms:DestAddr>"+calledNo+"</sms:DestAddr>\n" +
                    "                    <sms:Flag>"+flag+"</sms:Flag>\n" +
                    "                    <sms:StartTime>"+startTime+"</sms:StartTime>\n" +
                    "                    <sms:EndTime>"+endTime+"</sms:EndTime>\n" +
                    "                    <sms:SmcID>"+smcID+"</sms:SmcID>\n" +
                    "                    <sms:OperatorID></sms:OperatorID>\n" +
                    "                </sms:RequestMessage>\n" +
                    "            </sms:SmsLogRecordQueryRequest>\n" +
                    "        </sms:SmsLogRecordQuery>\n" +
                    "    </soapenv:Body>\n" +
                    "</soapenv:Envelope>";
            logger.debug("点对点短信查询服务发送报文：{}",requestXml);
            long smsStartTime = System.currentTimeMillis();
            String soapAction = "http://sms.huawei.com/smsinterface/SmsLogRecordQuery";

            String respSmsLogString = HttpUtils.requesrHttpPost(smsQueryUrl, requestXml, soapAction);       //调用接口,传递参数

            //String respSmsLogString = FileUtil.getTxtInfo("config/smsQuerySoap.txt");//测试用

            long smsEndTime = System.currentTimeMillis();
            logger.debug("点对点短信查询服务返回报文：{},点对点短信查询服务耗时{}ms", respSmsLogString, smsEndTime - smsStartTime);
            //if (StrUtil.isBlank(respSmsLogString)){
                //throw new NscsException(NscsExceptionEnum.Query_ERROR,"查询失败，点对点短信查询服务数据为空！");
            //}
            Document docReturn = HttpUtils.strXmlToDocument(respSmsLogString);                //解析成dom对象
            List<Map<String, String>> resultData = XmlHandler.getSoapResponse(docReturn);
            JSONArray smsJsonArr = JSONUtil.parseArray(resultData);
            logger.debug("点对点短信查询服务smsJsonArr：{}", smsJsonArr);
            String messageStatus = "";
            String gsmCode = "";
            String finalDate = "";
            String Retries = "";
            String messageLength = "";
            List<SMSLogInfo> smsLogInfoList = new ArrayList<SMSLogInfo>();
            if(smsJsonArr!=null&&smsJsonArr.size()>0) {
                for (int i = 0; i < smsJsonArr.size(); i++) {
                    JSONObject tj = smsJsonArr.getJSONObject(i);
                    callNo = tj.getStr("CallNo");
                    calledNo = tj.getStr("CalledNo");
                    messageStatus = tj.getStr("message_status");
                    gsmCode = tj.getStr("GSM_code");
                    finalDate = tj.getStr("final_date");
                    Retries = tj.getStr("Retries");
                    messageLength = tj.getStr("message_length");
                    SMSLogInfo smsLogInfo = new SMSLogInfo();
                    if(callNo.startsWith("861")){
                        callNo = callNo.substring(0,5)+"****"+callNo.substring(9);
                    }else if (callNo.length()==11){
                        callNo = callNo.substring(0,3)+"****"+callNo.substring(7);
                    }else if (callNo.length()<11&&callNo.length()>=5){
                        callNo = callNo.substring(0,1)+"****"+callNo.substring(5);
                    }else if (callNo.length()>11){
                        callNo = callNo.substring(0,callNo.length()/2-2)+"****"+callNo.substring(callNo.length()/2+2);
                    }
                    if(calledNo.startsWith("861")){
                        calledNo = calledNo.substring(0,5)+"****"+calledNo.substring(9);
                    }else if (calledNo.length()==11){
                        calledNo = calledNo.substring(0,3)+"****"+calledNo.substring(7);
                    }else if (calledNo.length()<11&&calledNo.length()>=5){
                        calledNo = calledNo.substring(0,1)+"****"+calledNo.substring(5);
                    }else if (calledNo.length()>11){
                        calledNo = calledNo.substring(0,calledNo.length()/2-2)+"****"+calledNo.substring(calledNo.length()/2+2);
                    }
                    smsLogInfo.setSendNumber(callNo);
                    smsLogInfo.setReceNumber(calledNo);
                    if(messageStatus.equals("23")){
                        smsLogInfo.setDownState("2");
                    }else{
                        smsLogInfo.setDownState("1");
                    }
                    smsLogInfo.setFailureReason(gsmCode);
                    smsLogInfo.setDownDate(finalDate);
                    smsLogInfo.setDownCount(Retries);
                    smsLogInfo.setSmsLength(messageLength);
                    smsLogInfoList.add(smsLogInfo);
                }
            }
            logger.debug("点对点短信查询服务返回结果：{}", JSONUtil.toJsonStr(smsLogInfoList));
            return smsLogInfoList;
        //}catch (NscsException ex){
            //throw new NscsException(NscsExceptionEnum.REQUEST_URL_ERROR,"查询失败，点对点短信查询服务调用错误！");
        }catch (Exception ex){
            logger.warn("查询异常",ex);
            //throw new NscsException(NscsExceptionEnum.Query_ERROR,"查询失败,"+ex.getMessage());
            return null;
        }
    }

    public void addList(List<SMSLogInfo> listAll,List<SMSLogInfo> list){
        if(list!=null&&list.size()>0){
            listAll.addAll(list);
        }
    }
}
