package com.boco.nscs.core.mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by CC on 2017/4/26.
 * 数据操作Mapper
 */
public interface NscsBaseMapper<T> {
    // 放一些公共的方法
    void insert(T record);

    Integer delete(String id);
    Integer deleteByMap(Map<String, Object> map);

    Integer update(T record);

    T queryOne(String id);
    T queryOneByMap(Map<String, Object> map);

    List<T> queryList(Map<String, Object> map);
    Integer count(Map<String, Object> map);
}
