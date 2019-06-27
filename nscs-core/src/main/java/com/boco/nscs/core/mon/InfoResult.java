package com.boco.nscs.core.mon;

import java.util.Map;

public class InfoResult extends BaseResult {
    private String type;
    private String host;
    private String name;
    private String getTIme;
    private Map<String,String> items;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGetTIme() {
        return getTIme;
    }

    public void setGetTIme(String getTIme) {
        this.getTIme = getTIme;
    }

    public Map<String, String> getItems() {
        return items;
    }

    public void setItems(Map<String, String> items) {
        this.items = items;
    }
}
