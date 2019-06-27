package com.boco.nscs.core.service;

import cn.hutool.json.JSONUtil;
import com.boco.nscs.core.entity.SearchCriteria;
import com.boco.nscs.core.entity.kf.KFRequest;
import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.core.entity.kf.KFRespDataUtil;
import com.boco.nscs.core.exception.NscsExceptionEnum;
import com.boco.nscs.core.mapper.KFBaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by CC on 2017/4/26.
 * 服务基类
 */
public abstract class KFBaseServiceImpl<M extends KFBaseMapper<T>,T>  implements KFBaseService<T> {
    @Autowired
    protected M mapper;
    //logger
    private static final Logger logger = LoggerFactory.getLogger(KFBaseServiceImpl.class);

    /**
     * 设置返回数据key
     */
    protected abstract String getResultKey();

    /**
     * 数据查询
     * @param req
     * @return
     */
    @Override
    public KFRespData get(KFRequest req) {
        if (req==null){
            throw NscsExceptionEnum.REQUEST_PARAM_NULL.getException();
        }
        SearchCriteria param = req.parse2CondBean();
        logger.debug("query param:{}", JSONUtil.toJsonStr(param));
        try {
            List<T> list = queryList(param);
            return KFRespDataUtil.success(getResultKey(),list);
        }catch (Exception ex){
            logger.warn("查询失败",ex);
            throw NscsExceptionEnum.Query_ERROR.getException();
        }
    }

    @Override
    public KFRespData getOne(KFRequest req) {
        if (req==null){
            throw NscsExceptionEnum.REQUEST_PARAM_NULL.getException();
        }
        SearchCriteria param = req.parse2CondBean();
        logger.debug("query one param:{}", JSONUtil.toJsonStr(param));
        try {
            T one = mapper.queryOne(param.getQueryMap());
            return KFRespDataUtil.signle(one);
        }catch (Exception ex){
            logger.warn("查询失败",ex);
            throw NscsExceptionEnum.Query_ERROR.getException();
        }
    }
    /**
     * 数据查询处理
     * 默认调用数据库mapper查询 可重写
     * @param param
     * @return
     */
    protected List<T> queryList(SearchCriteria param){
        return mapper.queryList(param.getQueryMap());
    }
}
