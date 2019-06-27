package com.boco.nscs.core.ptcc;

import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchParameterSingle implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer pageSize = 5;
    private Integer pageIndex = 1;
    private String[] sortColumns;
    private Boolean[] sortColumns_IsAscend;
    private List<SearchCommand> searchCommands;

    public SearchParameterSingle() {
        searchCommands = new ArrayList<>();
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String[] getSortColumns() {
        return sortColumns;
    }

    public void setSortColumns(String[] sortColumns) {
        this.sortColumns = sortColumns;
    }

    public Boolean[] getSortColumns_IsAscend() {
        return sortColumns_IsAscend;
    }

    public void setSortColumns_IsAscend(Boolean[] sortColumns_IsAscend) {
        this.sortColumns_IsAscend = sortColumns_IsAscend;
    }

    public List<SearchCommand> getSearchCommands() {
        return searchCommands;
    }

    public void setSearchCommands(List<SearchCommand> searchCommands) {
        this.searchCommands = searchCommands;
    }

    public void SortByInput(Boolean isAscend) {
        this.sortColumns = new String[] { "PTCC_ID" };
        this.sortColumns_IsAscend = new Boolean[] { isAscend };
    }

    String CNT_cmd ="<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
            "<s:Body>" +
            "<Search xmlns=\"http://tempuri.org/\">" +
            "<searchParameterPro xmlns:a=\"http://schemas.datacontract.org/2004/07/PTCC.Interface\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">" +
            "       <a:pageIndex>{}</a:pageIndex>" +
            "       <a:pageSize>{}</a:pageSize>" +
            "       <a:searchCommands>{}</a:searchCommands>" +
            "       <a:sortColumns xmlns:b=\"http://schemas.microsoft.com/2003/10/Serialization/Arrays\">{}</a:sortColumns>" +
            "       <a:sortColumns_IsAscend xmlns:b=\"http://schemas.microsoft.com/2003/10/Serialization/Arrays\">{}</a:sortColumns_IsAscend>" +
            "</searchParameterPro>" +
            "</Search>" +
            "</s:Body></s:Envelope>";

    String CNT_searCmdTpl =
            "<a:SearchParameterSingle.SearchCommand>\n" +
            "    <a:columnName>{}</a:columnName>\n" +
            "    <a:command>{}</a:command>\n" +
            "    <a:distanceKM>{}</a:distanceKM>\n" +
            "    <a:lat>{}</a:lat>\n" +
            "    <a:lng>{}</a:lng>\n" +
            "    <a:rangeEnd>{}</a:rangeEnd>\n" +
            "    <a:rangeStart>{}</a:rangeStart>\n" +
            "    <a:relation>{}</a:relation>\n" +
            "    <a:searchCommandType>{}</a:searchCommandType>\n" +
            "</a:SearchParameterSingle.SearchCommand>";

    public String GenSeachCmd(){

        String cmdStr ="";
        for (SearchCommand command : searchCommands) {
            cmdStr+= StrUtil.format(CNT_searCmdTpl,
                    command.getColumnName(),
                    command.getCommand(),
                    command.getDistanceKM()==null?"0":command.getDistanceKM(),
                    command.getLat()==null?"0":command.getLat(),
                    command.getLng()==null?"0":command.getLng(),
                    command.getRangeEnd()==null?"":command.getRangeEnd(),
                    command.getRangeStart()==null?"":command.getRangeStart(),
                    command.getRelation().toString(),
                    command.getSearchCommandType().name()
            );
        }
        String sortStr ="";
        String sortAscStr ="";
        if (sortColumns!=null) {
            for (String column : sortColumns) {
                sortStr += "<b:string>" + column + "</b:string>";
                sortAscStr += "<b:boolean>false</b:boolean>";  //默认降序
            }
        }
        if(sortColumns_IsAscend!=null) {
            for (Boolean aBoolean : sortColumns_IsAscend) {
                sortAscStr += "<b:boolean>" + aBoolean.toString() + "</b:boolean>";
            }
        }
        return StrUtil.format(CNT_cmd,pageIndex,pageSize, cmdStr,sortStr,sortAscStr);
    }
}
