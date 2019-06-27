package com.boco.nscs.entity.hss;

import com.boco.nscs.core.entity.kf.KFRequest;

public class HlrReq extends KFRequest {
    //用户手机号
    private String servNumber;

    public String getServNumber() {
        return servNumber;
    }

    public void setServNumber(String servNumber) {
        this.servNumber = servNumber;
    }

}
