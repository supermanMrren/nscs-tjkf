package com.boco.nscs.service.netstat.impl;

import cn.hutool.json.JSONUtil;
import com.boco.nscs.core.entity.SearchCriteria;
import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.core.entity.kf.KFRespDataUtil;
import com.boco.nscs.core.exception.NscsException;
import com.boco.nscs.core.exception.NscsExceptionEnum;
import com.boco.nscs.entity.netstat.TransferFaultReq;
import com.boco.nscs.mapper.netstat.IDotMapper;
import com.boco.nscs.service.netstat.IDotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by rc on 2019/5/15.
 */
@Service
public class DotServiceImpl implements IDotService {

    //logger
    private static final Logger logger = LoggerFactory.getLogger(DotServiceImpl.class);
    @Autowired
    private IDotMapper dotMapper;
    @Override
    @Transactional
    public KFRespData transferFaultInfo(TransferFaultReq req) {
        if (req==null){
            throw NscsExceptionEnum.REQUEST_PARAM_NULL.getException();
        }
        logger.debug("insert param:{}", JSONUtil.toJsonStr(req));
        int count = 0;
        try {
            if (req.getTransType().trim().equals("2")) {
                if ("1".equals(req.getBusiType())) {
                    count = dotMapper.qryWeakcoverById(req.getBusiId()) ;
                    if(count==0){
                        count = dotMapper.qryHotWeakcoverById(req.getBusiId());
                    }
                } else if ("2".equals(req.getBusiType())) {
                    count = dotMapper.qryNoticeById(req.getBusiId());
                    if(count==0){
                        count = dotMapper.qryAtoNoticeById(req.getBusiId());
                    }
                } else {
                    count = dotMapper.qryProStaById(req.getBusiId());
                }
                if (count == 0) {
                    throw NscsExceptionEnum.REQUEST_PARAM_Error.getException();
                }
            }
            String transType = req.getTransType().trim() + "类";
            req.setTransType(transType);
            dotMapper.insertDotInfo(req);
            return KFRespDataUtil.success("transactionResult", "1");
        }catch (NscsException ex){
            logger.warn("打点失败",ex);
            throw new NscsException(NscsExceptionEnum.REQUEST_PARAM_Error, "未查询到相关弱覆盖/故障公告/工程信息ID！");
        }catch (Exception ex){
            logger.warn("打点失败",ex);
            throw NscsExceptionEnum.INSERT_ERROR.getException();
        }
    }
}
