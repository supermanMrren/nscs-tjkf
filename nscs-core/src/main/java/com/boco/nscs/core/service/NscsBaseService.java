package com.boco.nscs.core.service;

import com.boco.nscs.core.entity.CriteriaBase;
import com.boco.nscs.core.entity.SearchCriteria;
import com.boco.nscs.core.entity.PageResultBase;

import java.util.List;

/**
 * Created by CC on 2017/4/26.
 * 服务基类
 */
public interface NscsBaseService<T> {
    void add(T entity);
    Integer edit(T entity);
    boolean exists(String id);
    long count(CriteriaBase cond);
    void delete(String id);
    void delete(CriteriaBase cond);

    T getOne(String id);
    T getOne(CriteriaBase cond);
    List<T> getList(SearchCriteria param);
    PageResultBase getPage(SearchCriteria param);
    List<T> getAll();


}
