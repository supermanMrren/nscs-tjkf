package com.boco.nscs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by CC on 2017/6/6.
 * 系统配置
 */
@Component
public class SysProperties {
    @Value("${app.version}")
    private String version;
    @Value("${app.province}")
    private String province;
    @Value("${app.province-code}")
    private String provinceCode;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
}
