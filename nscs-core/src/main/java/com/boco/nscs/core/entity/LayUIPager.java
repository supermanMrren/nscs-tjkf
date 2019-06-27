package com.boco.nscs.core.entity;

/**
 * Created by CC on 2017/8/18.
 */
public class LayUIPager {
    public LayUIPager(Integer page, Integer size) {
        this.page = page;
        this.limit = size;
    }

    public LayUIPager() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    //页数
    private Integer page;
    //每页条数
    private Integer limit;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
