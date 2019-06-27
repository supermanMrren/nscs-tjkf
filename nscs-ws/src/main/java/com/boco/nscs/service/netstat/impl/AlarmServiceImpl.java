package com.boco.nscs.service.netstat.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.boco.nscs.core.entity.SearchCriteria;
import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.core.entity.kf.KFRespDataUtil;
import com.boco.nscs.core.exception.NscsException;
import com.boco.nscs.core.exception.NscsExceptionEnum;
import com.boco.nscs.core.service.KFBaseServiceImpl;
import com.boco.nscs.entity.netstat.TfaAlarmInfo;
import com.boco.nscs.entity.netstat.TfaAlarmReq;
import com.boco.nscs.mapper.netstat.IAlarmMapper;
import com.boco.nscs.service.netstat.IAlarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AlarmServiceImpl extends KFBaseServiceImpl<IAlarmMapper,TfaAlarmInfo> implements IAlarmService {

    //logger
    private static final Logger logger = LoggerFactory.getLogger(AlarmServiceImpl.class);

    @Autowired
    private IAlarmMapper alarmMapper;

    @Override
    protected String getResultKey() {
        return "faultList";
    }

    @Cacheable(cacheNames = "alarm")
    @Override
    public KFRespData releaseFaultInfo(TfaAlarmReq req,String county) {
        if (req==null){
            throw NscsExceptionEnum.REQUEST_PARAM_NULL.getException();
        }
        SearchCriteria param = req.parse2CondBean();
        param.setIsPage(false);
        ArrayList<TfaAlarmInfo> list = new ArrayList<TfaAlarmInfo>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //查询网络故障公告
            List<TfaAlarmInfo> faultNoticeList = queryFaultNoticeList(param);
            if(faultNoticeList!=null&&faultNoticeList.size()>0){
                for(TfaAlarmInfo faultNotice : faultNoticeList){
                    faultNotice.setProvince(req.getProvince());
                    faultNotice.setCity(req.getCity());
                    if(StrUtil.isNotBlank(county)){
                        faultNotice.setCounty(county);
                    }
                    if(StrUtil.isBlank(faultNotice.getInterpretCaliber())){
                        faultNotice.setInterpretCaliber("");
                    }
                    list.add(faultNotice);
                }
            }
            logger.debug("query faultNoticeList:{}", JSONUtil.toJsonStr(faultNoticeList));
            //查询置顶公告
            List<TfaAlarmInfo> AtmoNoticeList = queryAtmoNoticeList(param);
            if(AtmoNoticeList!=null&&AtmoNoticeList.size()>0){
                for(TfaAlarmInfo atmoNotice : AtmoNoticeList){
                    atmoNotice.setProvince(req.getProvince());
                    atmoNotice.setCity(req.getCity());
                    if(StrUtil.isNotBlank(county)){
                        atmoNotice.setCounty(county);
                    }
                    atmoNotice.setAffectBusiness("其他");
                    if(StrUtil.isBlank(atmoNotice.getEndDate())){
                        atmoNotice.setStatus("2");
                    }else{
                        if(sdf.parse(atmoNotice.getEndDate()).getTime()>new Date().getTime()){
                            atmoNotice.setStatus("2");
                        }else{
                            atmoNotice.setStatus("1");
                        }
                    }
                    if(StrUtil.isBlank(atmoNotice.getInterpretCaliber())){
                        atmoNotice.setInterpretCaliber("");
                    }
                    list.add(atmoNotice);
                }
            }
            //logger.debug("query AtmoNoticeList:{}", JSONUtil.toJsonStr(AtmoNoticeList));
            return KFRespDataUtil.success(getResultKey(),list);
        }catch (Exception ex) {
            if(ex instanceof DataAccessException){
                logger.warn("数据库查询失败",ex);
                throw new NscsException(NscsExceptionEnum.SERVER_DB_ERROR,"查询失败，数据库查询错误！");
            }
            logger.warn("查询失败", ex);
            throw new NscsException(NscsExceptionEnum.Query_ERROR,"查询失败,"+ex.getMessage());
        }
    }

    private List<TfaAlarmInfo> queryFaultNoticeList(SearchCriteria param) {
        return alarmMapper.queryFaultNoticeList(param.getQueryMap());
    }

    private List<TfaAlarmInfo> queryAtmoNoticeList(SearchCriteria param) {
        return alarmMapper.queryAtmoNoticeList(param.getQueryMap());
    }
}
