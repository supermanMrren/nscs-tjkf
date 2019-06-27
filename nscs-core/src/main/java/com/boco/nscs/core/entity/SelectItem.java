package com.boco.nscs.core.entity;

/**
 * Created by 操 on 2017/01/16.
 */
public class SelectItem {
    private String code;
    private String name;
    private String type;

    public SelectItem(String code, String name, String type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public SelectItem() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
