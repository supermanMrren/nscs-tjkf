package com.boco.nscs.core.mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by CC on 2017/4/26.
 * 数据操作Mapper
 */
public interface KFBaseMapper<T> {
    // 放一些公共的方法
    T queryOne(String id);
    T queryOne(Map<String, Object> map);
    List<T> queryList(Map<String, Object> map);

    Integer count(Map<String, Object> map);
}
