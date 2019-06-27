package com.boco.nscs.entity.netstat;

import java.io.Serializable;

public class CutoverInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String engiId;
    private String province;
    private String city;
    private String county;
    //影响范围
    private String influence;
    //影响业务
    private String affectBusiness;
    //解释口径
    private String interpretCaliber;
    //工程状态
    private String status;
    //开始时间
    private String startDate;
    //预计结束时间：故障结束时间
    private String endDate;
    //经度
    private String longitude;
    //纬度
    private String latitude;
    private String nename;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getEngiId() {
        return engiId;
    }

    public void setEngiId(String engiId) {
        this.engiId = engiId;
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

    public String getNename() {
        return nename;
    }

    public void setNename(String nename) {
        this.nename = nename;
    }
}
