package com.boco.nscs.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by CC on 2017/6/6.
 * 系统配置
 */
@Component
public class AppProperties {
    @Value("${app.isDebug}")
    private boolean isDebug=false;
    @Value("${app.name}")
    private String name;
    @Value("${app.page-type}")
    private String pageType;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isDebug() {
        return isDebug;
    }
    public void setDebug(boolean debug) {
        isDebug = debug;
    }
    public String getPageType() {
        return pageType;
    }
    public void setPageType(String pageType) {
        this.pageType = pageType;
    }
}
