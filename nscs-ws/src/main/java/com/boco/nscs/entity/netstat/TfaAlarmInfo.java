package com.boco.nscs.entity.netstat;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class TfaAlarmInfo implements Serializable{
    private static final long serialVersionUID = 1L;
    @JSONField(ordinal = 2)
    private String province;
    @JSONField(ordinal = 3)
    private String city;
    @JSONField(ordinal = 4)
    private String county;
    //故障id
    @JSONField(ordinal = 1)
    private String faultId;
    //影响范围
    @JSONField(ordinal = 5)
    private String influence;
    //影响业务
    @JSONField(ordinal = 6)
    private String affectBusiness;
    //解释口径
    @JSONField(ordinal = 7)
    private String interpretCaliber;
    //故障状态
    @JSONField(ordinal = 8)
    private String status;
    //开始时间
    @JSONField(ordinal = 9)
    private String startDate;
    //预计结束时间：故障结束时间
    @JSONField(ordinal = 10)
    private String endDate;

    public String getFaultId() {
        return faultId;
    }

    public void setFaultId(String faultId) {
        this.faultId = faultId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getInfluence() {
        return influence;
    }

    public void setInfluence(String influence) {
        this.influence = influence;
    }

    public String getAffectBusiness() {
        return affectBusiness;
    }

    public void setAffectBusiness(String affectBusiness) {
        this.affectBusiness = affectBusiness;
    }

    public String getInterpretCaliber() {
        return interpretCaliber;
    }

    public void setInterpretCaliber(String interpretCaliber) {
        this.interpretCaliber = interpretCaliber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
