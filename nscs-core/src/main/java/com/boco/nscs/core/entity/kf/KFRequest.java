package com.boco.nscs.core.entity.kf;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.boco.nscs.core.entity.BizPager;
import com.boco.nscs.core.entity.SearchCriteria;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 在线客服接口请求参数基类
 * 其他参数继承实现
 */
public class KFRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private PubInfo pubInfo;
    private Date _startTime;
    private String servNumber;

    public KFRequest() {
        pubInfo = new PubInfo();
        _startTime = new Date();
    }

    public PubInfo getPubInfo() {
        return pubInfo;
    }

    public void setPubInfo(PubInfo pubInfo) {
        this.pubInfo = pubInfo;
    }

    public Date get_startTime() {
        return _startTime;
    }

    public void set_startTime(Date _startTime) {
        this._startTime = _startTime;
    }

    public String getServNumber() {
        return servNumber;
    }

    public void setServNumber(String servNumber) {
        this.servNumber = servNumber;
    }

    public String toJsonStr(){
        return JSONUtil.toJsonStr(this);
    }

    public SearchCriteria parse2CondBean(){
        SearchCriteria param = new SearchCriteria();
        if(this.getPubInfo()!=null && this.getPubInfo().getPaging()==1){
            //分页
            BizPager pager = new BizPager();
            pager.setPageIndex(this.getPubInfo().getPageNum());
            pager.setPageSize(this.getPubInfo().getRowsPerPage());
            param.setIsPage(true);
            param.setPager(pager);
        } else{
            param.setIsPage(false);
        }
        Map<String, Object> map = BeanUtil.beanToMap(this);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equalsIgnoreCase("pubInfo")){
                continue;
            }
            param.putCriteria(entry.getKey(),entry.getValue());
        }
        return param;
    }
}
