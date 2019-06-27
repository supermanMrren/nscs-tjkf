package com.boco.nscs.entity.sms;


import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by rc on 2019/5/16.
 */
public class SMSLogInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    //发送短信号码
    @JSONField(ordinal = 1)
    private String sendNumber;
    //接收短信号码
    @JSONField(ordinal = 2)
    private String receNumber;
    //是否成功	1：成功 2：失败
    @JSONField(ordinal = 3)
    private String downState;
    //失败原因
    @JSONField(ordinal = 4)
    private String failureReason;
    //提交时间
    @JSONField(ordinal = 5)
    private String smsSubmit = "";
    //下发短信时间
    @JSONField(ordinal = 6)
    private String downDate;
    //尝试下发次数
    @JSONField(ordinal = 7)
    private String downCount;
    //短信内容长度
    @JSONField(ordinal = 8)
    private String smsLength;

    public String getSendNumber() {
        return sendNumber;
    }

    public void setSendNumber(String sendNumber) {
        this.sendNumber = sendNumber;
    }

    public String getReceNumber() {
        return receNumber;
    }

    public void setReceNumber(String receNumber) {
        this.receNumber = receNumber;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getSmsSubmit() {
        return smsSubmit;
    }

    public void setSmsSubmit(String smsSubmit) {
        this.smsSubmit = smsSubmit;
    }

    public String getDownDate() {
        return downDate;
    }

    public void setDownDate(String downDate) {
        this.downDate = downDate;
    }

    public String getDownState() {
        return downState;
    }

    public void setDownState(String downState) {
        this.downState = downState;
    }

    public String getDownCount() {
        return downCount;
    }

    public void setDownCount(String downCount) {
        this.downCount = downCount;
    }

    public String getSmsLength() {
        return smsLength;
    }

    public void setSmsLength(String smsLength) {
        this.smsLength = smsLength;
    }
}
