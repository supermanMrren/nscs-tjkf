package com.boco.nscs.entity.sys;

import com.boco.nscs.core.entity.kf.KFRequest;

public class UserReq extends KFRequest {
    private String userId;
    private String deptId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
}
