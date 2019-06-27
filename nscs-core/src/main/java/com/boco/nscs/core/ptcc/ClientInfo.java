package com.boco.nscs.core.ptcc;

import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;

public class ClientInfo {
    public ClientInfo() {
        tableNames = new ArrayList<>();
        tablesServerList = new ArrayList<>();
        tablesColumns = new ArrayList<>();
        tablesReturnColumns  = new ArrayList<>();
        tablesColumnsType = new ArrayList<>();

        weakconverServerList = new ArrayList<>();
        weakconverReturnColumns = new ArrayList<>();
        noticeServerList = new ArrayList<>();
        noticeReturnColumns = new ArrayList<>();
    }

    private List<String> tableNames;

    private List<List<String>> tablesServerList;

    private List<List<String>> tablesColumns;

    private List<List<String>> tablesReturnColumns;

    //type 0:String 1:Int 2 Long
    private List<List<Integer>> tablesColumnsType;

    //天津专用
    private List<String> weakconverServerList;
    private List<String> weakconverReturnColumns;
    private List<String> noticeServerList;
    private List<String> noticeReturnColumns;

    public List<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
    }

    public List<List<String>> getTablesServerList() {
        return tablesServerList;
    }

    public void setTablesServerList(List<List<String>> tablesServerList) {
        this.tablesServerList = tablesServerList;
    }

    public List<List<String>> getTablesColumns() {
        return tablesColumns;
    }

    public void setTablesColumns(List<List<String>> tablesColumns) {
        this.tablesColumns = tablesColumns;
    }

    public List<List<String>> getTablesReturnColumns() {
        return tablesReturnColumns;
    }

    public void setTablesReturnColumns(List<List<String>> tablesReturnColumns) {
        this.tablesReturnColumns = tablesReturnColumns;
    }

    public List<List<Integer>> getTablesColumnsType() {
        return tablesColumnsType;
    }

    public void setTablesColumnsType(List<List<Integer>> tablesColumnsType) {
        this.tablesColumnsType = tablesColumnsType;
    }

    public List<String> getWeakconverServerList() {
        return weakconverServerList;
    }

    public void setWeakconverServerList(List<String> weakconverServerList) {
        this.weakconverServerList = weakconverServerList;
    }

    public List<String> getWeakconverReturnColumns() {
        return weakconverReturnColumns;
    }

    public void setWeakconverReturnColumns(List<String> weakconverReturnColumns) {
        this.weakconverReturnColumns = weakconverReturnColumns;
    }

    public List<String> getNoticeServerList() {
        return noticeServerList;
    }

    public void setNoticeServerList(List<String> noticeServerList) {
        this.noticeServerList = noticeServerList;
    }

    public List<String> getNoticeReturnColumns() {
        return noticeReturnColumns;
    }

    public void setNoticeReturnColumns(List<String> noticeReturnColumns) {
        this.noticeReturnColumns = noticeReturnColumns;
    }

    public static String GetColumnsTypeName(Integer type){
        if (type==0){
            return "String";
        } else if (type==1){
            return "Int";
        }else if (type==2){
            return "Long";
        } else {
            return type.toString();
        }
    }

    public String getDesc(){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.getTableNames().size(); i++) {
            sb.append("Table:"+(i+1)+" ");
            sb.append(this.getTableNames().get(i));
            sb.append("\n");
            for (String o : this.getTablesServerList().get(i)) {
                sb.append(">>Server:"+o);
                sb.append("\n");
            }
            for (int j = 0; j < this.getTablesColumns().get(i).size(); j++) {
                sb.append(">>Col:"+this.getTablesColumns().get(i).get(j) +" "+this.getTablesColumnsType().get(i).get(j));
                sb.append("\n");
            }
            for (String o : this.getTablesReturnColumns().get(i)) {
                sb.append(">>Rtn:"+o);
                sb.append("\n");
            }
            sb.append("\n");
        }
        //天津部分输出
        if (weakconverServerList.size()>0){
            sb.append("WeakCover:\n");
            for (String o : this.weakconverServerList) {
                sb.append(">>Server:"+o);
                sb.append("\n");
            }
            for (String o : this.weakconverReturnColumns) {
                sb.append(">>Rtn:"+o);
                sb.append("\n");
            }
            sb.append("\n");
        }
        if (noticeServerList.size()>0){
            sb.append("Notice:\n");
            for (String o : this.noticeServerList) {
                sb.append(">>Server:"+o);
                sb.append("\n");
            }
            for (String o : this.noticeReturnColumns) {
                sb.append(">>Rtn:"+o);
                sb.append("\n");
            }
        }

        return sb.toString();
    }


    public TableClientInfo getTableClientInfo(String tableName){
        if (StrUtil.isBlank(tableName))
            return null;

        Integer idx=-1;
        for (int i = 0; i < tableNames.size(); i++) {
            if (tableName.equals(tableNames.get(i))){
                idx=i;
                break;
            }
        }
        if (idx<=0){
            return null;
        }

        TableClientInfo clientInfo = new TableClientInfo();
        clientInfo.setTableName(tableName);
        clientInfo.setColumns(tablesColumns.get(idx));
        clientInfo.setColumnsTypes(tablesColumnsType.get(idx));
        clientInfo.setRtnCols(tablesReturnColumns.get(idx));
        clientInfo.setServerList(tablesServerList.get(idx));
        return clientInfo;
    }
}
