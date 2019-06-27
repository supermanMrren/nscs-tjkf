package com.boco.nscs.core.entity;

import java.util.List;

/**
 * Created by CC on 2017/1/19.
 */
public class BizTree {
    private String id;
    private String name;
    private List<BizTree> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BizTree> getChildren() {
        return children;
    }

    public void setChildren(List<BizTree> children) {
        this.children = children;
    }
}
