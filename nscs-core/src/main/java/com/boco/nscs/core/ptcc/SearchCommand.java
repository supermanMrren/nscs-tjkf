package com.boco.nscs.core.ptcc;

import java.io.Serializable;

public class SearchCommand implements Serializable {
    private static final long serialVersionUID = 1L;

    private RelationEnum relation = RelationEnum.AND;
    private SearchCommandTypeEnum searchCommandType = SearchCommandTypeEnum.ColumnSearch;
    private String columnName;
    private String command;
    private String rangeStart;
    private String rangeEnd;
    private Double lng;
    private Double lat;
    private Double distanceKM;

    public SearchCommand() {
    }

    public SearchCommand(String columnName, String command) {
        this.columnName = columnName;
        this.command = command;
        this.searchCommandType = SearchCommandTypeEnum.ColumnSearch;
    }

    public SearchCommand(String columnName, String command,RelationEnum relation) {
        this.relation = relation;
        this.columnName = columnName;
        this.command = command;
        this.searchCommandType = SearchCommandTypeEnum.ColumnSearch;
    }

    public SearchCommand(String columnName, String rangeStart, String rangeEnd) {
        this.columnName = columnName;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.searchCommandType = SearchCommandTypeEnum.RangeSearch;
    }

    public SearchCommand(String columnName, String rangeStart, String rangeEnd,RelationEnum relation) {
        this.relation = relation;
        this.columnName = columnName;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.searchCommandType = SearchCommandTypeEnum.RangeSearch;
    }

    public SearchCommand(String columnName, Double lng, Double lat, Double distanceKM) {
        this.columnName = columnName;
        this.lng = lng;
        this.lat = lat;
        this.distanceKM = distanceKM;
        this.searchCommandType = SearchCommandTypeEnum.GisCircleSearch;
    }

    public SearchCommand(String columnName, Double lng, Double lat, Double distanceKM,RelationEnum relation) {
        this.relation = relation;
        this.columnName = columnName;
        this.lng = lng;
        this.lat = lat;
        this.distanceKM = distanceKM;
        this.searchCommandType = SearchCommandTypeEnum.GisCircleSearch;
    }

    public RelationEnum getRelation() {
        return relation;
    }

    public void setRelation(RelationEnum relation) {
        this.relation = relation;
    }

    public SearchCommandTypeEnum getSearchCommandType() {
        return searchCommandType;
    }

    public void setSearchCommandType(SearchCommandTypeEnum searchCommandType) {
        this.searchCommandType = searchCommandType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(String rangeStart) {
        this.rangeStart = rangeStart;
    }

    public String getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(String rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getDistanceKM() {
        return distanceKM;
    }

    public void setDistanceKM(Double distanceKM) {
        this.distanceKM = distanceKM;
    }
}
