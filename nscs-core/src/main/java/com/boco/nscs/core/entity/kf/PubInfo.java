package com.boco.nscs.core.entity.kf;

public class PubInfo {
    //调用工号
    private String staffId;
    //组织id
    private String orgId;
    //来电流水号
    private String incomNumber;
    private String cityCode;
    private String countryCode;
    private Integer paging;
    private Integer rowsPerPage;
    private Integer pageNum;

    public PubInfo() {
        paging=1;
        rowsPerPage=10;
        pageNum=1;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getIncomNumber() {
        return incomNumber;
    }

    public void setIncomNumber(String incomNumber) {
        this.incomNumber = incomNumber;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getPaging() {
        return paging;
    }

    public void setPaging(Integer paging) {
        this.paging = paging;
    }

    public Integer getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(Integer rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
