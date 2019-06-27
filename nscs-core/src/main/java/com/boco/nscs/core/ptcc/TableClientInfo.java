package com.boco.nscs.core.ptcc;

import java.util.List;

public class TableClientInfo {
    private String tableName;
    private List<String> serverList;
    private List<String> columns;
    private List<Integer> columnsTypes;
    private List<String> rtnCols;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getServerList() {
        return serverList;
    }

    public void setServerList(List<String> serverList) {
        this.serverList = serverList;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<Integer> getColumnsTypes() {
        return columnsTypes;
    }

    public void setColumnsTypes(List<Integer> columnsTypes) {
        this.columnsTypes = columnsTypes;
    }

    public List<String> getRtnCols() {
        return rtnCols;
    }

    public void setRtnCols(List<String> rtnCols) {
        this.rtnCols = rtnCols;
    }
}
