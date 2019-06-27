package com.boco.nscs.core.entity;

import java.util.Map;

public class SearchCriteria extends CriteriaBase {
	private Integer pageIndex =1;
	private Integer pageSize=50;
    private Integer skip=0;
	private Boolean isPage =true;  //是否对结果分页 默认为true

	public Integer getPageIndex() {
	    return pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}
	public Integer getSkip(){
        return skip;
	}

	public Boolean getIsPage() {
		return isPage;
	}
	public void setIsPage(Boolean isPage) {
		this.isPage = isPage;
	}

	//分页条件
	public void setPager(DtPager pager) {
		if (pager!=null) {
			this.skip = pager.getStart();
			this.pageSize = pager.getLength();
			this.pageIndex =(this.skip / this.pageSize)+1;
		}
	}
	public void setPager(LayUIPager pager) {
		if (pager!=null) {
			this.pageIndex = pager.getPage();
			this.pageSize = pager.getLimit();
			this.skip = (this.pageIndex - 1) * this.pageSize;
		}
	}
    public void setPager(EasyUIPager pager) {
		if (pager!=null) {
			this.pageIndex = pager.getPage();
			this.pageSize = pager.getRows();
			this.skip = (this.pageIndex - 1) * this.pageSize;
		}
    }
    public void setPager(BizPager  pager){
		if (pager!=null) {
			this.pageIndex = pager.getPageIndex();
			this.pageSize = pager.getPageSize();
			this.skip = (this.pageIndex - 1) * this.pageSize;
		}
	}

    //生成查询条件
    public Map<String,Object> getQueryMap(){
        if(this.isPage){
            getCondition().put("pageIndex", this.pageIndex);
            getCondition().put("pageSize", this.pageSize);
			getCondition().put("skip", this.skip);
        }
        return getCondition();
    }
}
