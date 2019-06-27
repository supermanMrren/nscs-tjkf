package com.boco.nscs.core.entity;

/**
 * Created by CC on 2017/8/18.
 */
public class EasyUIPager  {
    public EasyUIPager(Integer page, Integer rows) {
        this.page = page;
        this.rows = rows;
    }

    public EasyUIPager() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    //页数
    private Integer page;
    //每页条数
    private Integer rows;
}
