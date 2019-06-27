package com.boco.nscs.core.entity;

import java.util.List;

/**
 * Created by CC on 2017/8/31.
 * 分页后结果
 */
public class BizPageResult extends PageResultBase {
    private Long total;
    private Integer pageIndex;
    private Integer pageSize;
    public Long getTotal() {
        return total;
    }
    public void setTotal(Long total) {
        this.total = total;
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
    public List<?> getRows() {
        return rows;
    }
    public void setRows(List<?> rows) {
        this.rows = rows;
    }
    private List<?> rows;
}
