package com.boco.nscs.core.entity;

import java.util.List;

/**
 * Created by CC on 2017/8/11.
 * Datatables 分页处理
 */
public class DtPageResult extends PageResultBase {
    private String sEcho;
    //总数据条数
    private long iTotalRecords;
    private long iTotalDisplayRecords;
    //数据
    public long getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(long iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public List<?> getAaData() {
        return aaData;
    }

    public void setAaData(List<?> aaData) {
        this.aaData = aaData;
    }

    private List<?> aaData;

    public long getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }
}
