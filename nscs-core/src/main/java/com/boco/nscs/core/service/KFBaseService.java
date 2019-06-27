package com.boco.nscs.core.service;

import com.boco.nscs.core.entity.CriteriaBase;
import com.boco.nscs.core.entity.PageResultBase;
import com.boco.nscs.core.entity.SearchCriteria;
import com.boco.nscs.core.entity.kf.KFRequest;
import com.boco.nscs.core.entity.kf.KFRespData;

import java.util.List;

/**
 * Created by CC on 2017/4/26.
 * 客服查询服务基类
 */
public interface KFBaseService<T> {
//    T getOne(String id);
//    T getOne(CriteriaBase cond);
//    List<T> getList(SearchCriteria param);
//    PageResultBase getPage(SearchCriteria param);

    //KFRespData<T> get(KFRequest req);
    KFRespData get(KFRequest req);
    KFRespData getOne(KFRequest req);
}
