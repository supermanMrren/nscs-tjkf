package com.boco.nscs.entity.sys;

import java.util.Date;

public class WsCallLog {
    private String ifName;
    private String method;
    //调用工号
    private String callStaffId;
    //来电流水号
    private String callNo;
    //用户号码
    private String customerNum;
    //开始时间
    private Date startTime;
    //结束时间
    private Date finishTime;
    //调用结果 0:成功 非:0 失败
    private int resultCode;
    //失败原因
    private String resultMsg;
    //是否查询到数据
    private String isGetData;
    // 经度
    private Double longitude;
    //纬度
    private Double latitude;

    public String getIfName() {
        return ifName;
    }

    public void setIfName(String ifName) {
        this.ifName = ifName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCallStaffId() {
        return callStaffId;
    }

    public void setCallStaffId(String callStaffId) {
        this.callStaffId = callStaffId;
    }

    public String getCallNo() {
        return callNo;
    }

    public void setCallNo(String callNo) {
        this.callNo = callNo;
    }

    public String getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(String customerNum) {
        this.customerNum = customerNum;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getIsGetData() {
        return isGetData;
    }

    public void setIsGetData(String isGetData) {
        this.isGetData = isGetData;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
