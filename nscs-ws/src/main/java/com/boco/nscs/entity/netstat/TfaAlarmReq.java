package com.boco.nscs.entity.netstat;

import com.boco.nscs.core.entity.kf.KFRequest;

public class TfaAlarmReq extends KFRequest {
    private String province;
    private String city;
    private String county;
    private String influence;
    //故障时间
    private String reportTime;
    private String longLatitude;

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

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getLongLatitude() {
        return longLatitude;
    }

    public void setLongLatitude(String longLatitude) {
        this.longLatitude = longLatitude;
    }
}
