package com.boco.nscs.core.entity;

import java.util.List;

public class LayUIPageResult extends PageResultBase {
	private int code;
	private Long count;
	private Integer page;
	private Integer limit;
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public List<?> getData() {
		return data;
	}
	public void setData(List<?> data) {
		this.data = data;
	}
	private List<?> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
