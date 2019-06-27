package com.boco.nscs.entity.hss;

import java.io.Serializable;

/**
 * Created by rc on 2019/6/4.
 */
public class HlrSearch implements Serializable{
    private static final long serialVersionUID = 1L;
    private String staffId;
    private String callTime;
    private String incomNumber;
    private String servNumber;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getIncomNumber() {
        return incomNumber;
    }

    public void setIncomNumber(String incomNumber) {
        this.incomNumber = incomNumber;
    }

    public String getServNumber() {
        return servNumber;
    }

    public void setServNumber(String servNumber) {
        this.servNumber = servNumber;
    }
}
