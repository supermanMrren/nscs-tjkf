package com.boco.nscs.core.entity;

/**
 * Created by CC on 2017/8/18.
 */
public class DtPager {
    //开始行号
    private Integer start;
    //显示行数
    private Integer length;

    public DtPager() {
    }

    public DtPager(Integer start, Integer length) {
        this.start = start;
        this.length = length;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
