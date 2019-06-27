package com.boco.nscs.core.entity;


/**
 * Created by CC on 2017/8/11.
 * 分页查询条件
 */
public class BizPager {
    private Integer pageIndex =1;
    private Integer pageSize =10;

    public BizPager() {
    }

    public BizPager(Integer pageIndex, Integer pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
