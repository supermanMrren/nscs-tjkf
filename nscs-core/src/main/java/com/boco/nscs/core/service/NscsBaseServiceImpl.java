package com.boco.nscs.core.service;

import com.boco.nscs.core.entity.CriteriaBase;
import com.boco.nscs.core.entity.PageResultBase;
import com.boco.nscs.core.entity.SearchCriteria;
import com.boco.nscs.core.mapper.NscsBaseMapper;
import com.boco.nscs.core.util.SearchUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by CC on 2017/4/26.
 * 服务基类
 */
public abstract class NscsBaseServiceImpl<M extends NscsBaseMapper<T>,T>  implements NscsBaseService<T> {
    @Autowired
    protected M mapper;

    @Override
    public void add(T entity) {
        mapper.insert(entity);
    }

    @Override
    public Integer edit(T entity) {
        return mapper.update(entity);
    }


    @Override
    public boolean exists(String id) {
        CriteriaBase cond = new CriteriaBase();
        cond.putCriteria("id",id);
        int i = mapper.count(cond.getCondition());
        return i > 0;
    }

    @Override
    public long count(CriteriaBase cond){
        return mapper.count(cond.getCondition());
    }

    @Override
    public void delete(String id)  {
        mapper.delete(id);
    }

    @Override
    public void delete(CriteriaBase cond){
        mapper.deleteByMap(cond.getCondition());
    }


    @Override
    public T getOne(String id) {
        return mapper.queryOne(id);
    }
    @Override
    public T getOne(CriteriaBase cond) {
        return mapper.queryOneByMap(cond.getCondition());
    }


    @Override
    public List<T> getList(SearchCriteria cond) {
        return mapper.queryList(cond.getQueryMap());
    }

    @Override
    public PageResultBase getPage(SearchCriteria param){
        List<T> list = mapper.queryList(param.getQueryMap());
        return SearchUtil.parse(list);
    }

    @Override
    public List<T> getAll() {
        SearchCriteria cond = new SearchCriteria();
        cond.setIsPage(false);
        return mapper.queryList(cond.getQueryMap());
    }
}
