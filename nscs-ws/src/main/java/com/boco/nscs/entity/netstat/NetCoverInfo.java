package com.boco.nscs.entity.netstat;

import com.alibaba.fastjson.annotation.JSONField;

public class NetCoverInfo {
    private static final long serialVersionUID = 1L;
    private String id;
    @JSONField(ordinal = 2)
    private String province;
    @JSONField(ordinal = 3)
    private String city;
    @JSONField(ordinal = 4)
    private String county;
    //弱覆盖Id
    @JSONField(ordinal = 1)
    private String poorCoverId;
    //具体位置
    @JSONField(ordinal = 5)
    private String influence = "";
    //解释口径
    @JSONField(ordinal = 6)
    private String interpretCaliber="";
    //故障状态
    @JSONField(ordinal = 7)
    private String status;
    //最早发生时间
    @JSONField(ordinal = 8)
    private String startDate = "";
    //计划解决时间
    @JSONField(ordinal = 9)
    private String expEndDate = "";
    //实际解决时间
    @JSONField(ordinal = 10)
    private String realEndDate = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoorCoverId() {
        return poorCoverId;
    }

    public void setPoorCoverId(String poorCoverId) {
        this.poorCoverId = poorCoverId;
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

    public String getExpEndDate() {
        return expEndDate;
    }

    public void setExpEndDate(String expEndDate) {
        this.expEndDate = expEndDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRealEndDate() {
        return realEndDate;
    }

    public void setRealEndDate(String realEndDate) {
        this.realEndDate = realEndDate;
    }
}
