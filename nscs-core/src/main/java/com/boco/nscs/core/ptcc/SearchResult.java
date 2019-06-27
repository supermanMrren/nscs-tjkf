package com.boco.nscs.core.ptcc;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<String[]> rows;
    private int pageIndex = 1;
    private int pageCount;
    private int count;

    public List<String[]> getRows() {
        return rows;
    }

    public void setRows(List<String[]> rows) {
        this.rows = rows;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
