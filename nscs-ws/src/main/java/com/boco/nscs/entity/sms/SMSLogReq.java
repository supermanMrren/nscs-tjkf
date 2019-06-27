package com.boco.nscs.entity.sms;

import com.boco.nscs.core.entity.kf.KFRequest;

/**
 * Created by rc on 2019/5/16.
 */
public class SMSLogReq extends KFRequest {
    //受理号码
    private String servNumber;
    //收发类型
    private String status;
    private String startDate;
    private String endDate;

    public String getServNumber() {
        return servNumber;
    }

    public void setServNumber(String servNumber) {
        this.servNumber = servNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
