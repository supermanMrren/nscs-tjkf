package com.boco.nscs.entity.sms;

import java.io.Serializable;

/**
 * Created by rc on 2019/5/31.
 */
public class RelSmsInfo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String moduleVer;
    private String moduledes;
    private String serviceModuleIfId;
    private String callTime;
    private String orderIndex;

    public String getModuleVer() {
        return moduleVer;
    }

    public void setModuleVer(String moduleVer) {
        this.moduleVer = moduleVer;
    }

    public String getModuledes() {
        return moduledes;
    }

    public void setModuledes(String moduledes) {
        this.moduledes = moduledes;
    }

    public String getServiceModuleIfId() {
        return serviceModuleIfId;
    }

    public void setServiceModuleIfId(String serviceModuleIfId) {
        this.serviceModuleIfId = serviceModuleIfId;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(String orderIndex) {
        this.orderIndex = orderIndex;
    }
}
