package com.boco.nscs.entity.netstat;

import com.boco.nscs.core.entity.kf.KFRequest;

import java.util.Date;

/**
 * Created by rc on 2019/5/13.
 */
public class TransferFaultReq extends KFRequest {

    //受理时间
    private String bizDate;
    //受理内容
    private String bizCntt;
    //打点方式
    private String transType;
    //投诉地市
    private String city;
    //投诉区县
    private String county;
    //影响范围：具体的地址
    private String influence;
    //投诉位置经纬度
    private String longLatitude;
    private Double longitude;
    private Double latitude;
    //打点类型
    private String busiType;
    //弱覆盖/故障公告/工程信息ID
    private String busiId;

    public String getBizDate() {
        return bizDate;
    }

    public void setBizDate(String bizDate) {
        this.bizDate = bizDate;
    }

    public String getBizCntt() {
        return bizCntt;
    }

    public void setBizCntt(String bizCntt) {
        this.bizCntt = bizCntt;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
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

    public String getLongLatitude() {
        return longLatitude;
    }

    public void setLongLatitude(String longLatitude) {
        this.longLatitude = longLatitude;
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

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public String getBusiId() {
        return busiId;
    }

    public void setBusiId(String busiId) {
        this.busiId = busiId;
    }
}
