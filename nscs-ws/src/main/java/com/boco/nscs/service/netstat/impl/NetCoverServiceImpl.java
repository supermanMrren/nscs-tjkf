package com.boco.nscs.service.netstat.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.boco.nscs.core.entity.SearchCriteria;
import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.core.entity.kf.KFRespDataUtil;
import com.boco.nscs.core.exception.NscsException;
import com.boco.nscs.core.exception.NscsExceptionEnum;
import com.boco.nscs.core.service.KFBaseServiceImpl;
import com.boco.nscs.entity.netstat.*;
import com.boco.nscs.mapper.netstat.IAlarmMapper;
import com.boco.nscs.mapper.netstat.INetCoverMapper;
import com.boco.nscs.service.netstat.IAlarmService;
import com.boco.nscs.service.netstat.INetCoverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class NetCoverServiceImpl extends KFBaseServiceImpl<INetCoverMapper,NetCoverInfo> implements INetCoverService {

    //logger
    private static final Logger logger = LoggerFactory.getLogger(NetCoverServiceImpl.class);

    @Override
    protected String getResultKey() {
        return "poorCoverList";
    }

    @Autowired
    private INetCoverMapper netCoverMapper;

    @Cacheable(cacheNames = "netCover")
    @Override
    public KFRespData qryCoverInfo(NetCoverReq req,String county) {
        if (req==null){
            throw NscsExceptionEnum.REQUEST_PARAM_NULL.getException();
        }
        SearchCriteria param = req.parse2CondBean();
        param.setIsPage(false);
        logger.debug("query param:{}", JSONUtil.toJsonStr(param));
        ArrayList<NetCoverInfo> list = new ArrayList<NetCoverInfo>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("E, MMM dd, yyyy");
        String realEndDate = "";
        String expEndDate = "";
        try {
            long wcStartTime = System.currentTimeMillis();
            List<NetCoverInfo> weakcoverList = queryWeakcoverList(param);
            long wcEndTime = System.currentTimeMillis();
            logger.debug("query wcCastTime:{}ms",wcEndTime-wcStartTime);
            if(weakcoverList!=null&&weakcoverList.size()>0){
                for(NetCoverInfo weakcoverInfo : weakcoverList){
                    expEndDate = weakcoverInfo.getExpEndDate();
                    weakcoverInfo.setProvince(req.getProvince());
                    weakcoverInfo.setCity(req.getCity());
                    expEndDate = convertTimetype(expEndDate, sdf, sdf2);
                    weakcoverInfo.setExpEndDate(expEndDate);
                    if(StrUtil.isNotBlank(county)){
                        weakcoverInfo.setCounty(county);
                    }
                    realEndDate = weakcoverInfo.getRealEndDate();
                    if(StrUtil.isBlank(realEndDate)){
                        weakcoverInfo.setStatus("2");
                    }else {
                        if (sdf.parse(realEndDate).getTime() > new Date().getTime()) {
                            weakcoverInfo.setStatus("2");
                        } else {
                            weakcoverInfo.setStatus("1");
                        }
                    }

                    list.add(weakcoverInfo);
                }
            }
            logger.debug("query weakcoverList:{}", JSONUtil.toJsonStr(weakcoverList));
            long wchStartTime = System.currentTimeMillis();
            List<NetCoverInfo> weakcoverHotList = queryWeakcoverHotList(param);
            long wchEndTime = System.currentTimeMillis();
            logger.debug("query wchCastTime:{}ms",wchEndTime-wchStartTime);
            if(weakcoverHotList!=null&&weakcoverHotList.size()>0){
                for(NetCoverInfo weakcoverHotInfo : weakcoverHotList){
                    weakcoverHotInfo.setProvince(req.getProvince());
                    weakcoverHotInfo.setCity(req.getCity());
                    expEndDate = weakcoverHotInfo.getExpEndDate();
                    expEndDate = convertTimetype(expEndDate, sdf, sdf2);
                    weakcoverHotInfo.setExpEndDate(expEndDate);
                    if(StrUtil.isNotBlank(county)){
                        weakcoverHotInfo.setCounty(county);
                    }
                    realEndDate = weakcoverHotInfo.getRealEndDate();
                    if(StrUtil.isBlank(realEndDate)){
                        weakcoverHotInfo.setStatus("2");
                    }else {
                        if (sdf.parse(realEndDate).getTime() > new Date().getTime()) {
                            weakcoverHotInfo.setStatus("2");
                        } else {
                            weakcoverHotInfo.setStatus("1");
                        }
                    }
                    list.add(weakcoverHotInfo);
                }
            }
            logger.debug("query weakcoverHotList:{}", JSONUtil.toJsonStr(weakcoverHotList));
            //logger.debug("query list:{}", JSONUtil.toJsonStr(list));
            return KFRespDataUtil.success(getResultKey(),list);
        }catch (Exception ex){
            if(ex instanceof DataAccessException){
                logger.warn("数据库查询失败",ex);
                throw new NscsException(NscsExceptionEnum.SERVER_DB_ERROR,"查询失败，数据库查询错误！");
            }
            logger.warn("查询失败", ex);
            throw new NscsException(NscsExceptionEnum.Query_ERROR,"查询失败,"+ex.getMessage());
        }
    }
    protected List<NetCoverInfo> queryWeakcoverList(SearchCriteria param) {
        return netCoverMapper.queryWeakcoverList(param.getQueryMap());
    }

    protected List<NetCoverInfo> queryWeakcoverHotList(SearchCriteria param) {
        return netCoverMapper.queryWeakcoverHotList(param.getQueryMap());
    }

    //转换日期格式
    public String convertTimetype(String expEndDate,SimpleDateFormat sdf,SimpleDateFormat sdf2) throws ParseException {
        //日期匹配格式yyyy-MM-dd HH:mm:ss
        String regex = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)\\s{1}((0[0-9])|(1[0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]{1}";
        expEndDate = expEndDate.replace("/", "-");
        int indexOf = -1;
        if (expEndDate.contains("星期")) {
            Date parse = sdf2.parse(expEndDate);
            expEndDate = sdf.format(parse);
        } else if (Validator.isMactchRegex(regex, expEndDate)) {
            return expEndDate;
        } else if ((indexOf = expEndDate.substring(expEndDate.indexOf("-")+1).indexOf("-")) > 0) {
            if((expEndDate.substring(expEndDate.indexOf("-")+1).substring(indexOf+2).length())>0){
                if(expEndDate.substring(expEndDate.indexOf("-")+1).substring(indexOf+2).substring(0,1)==""){
                    expEndDate = expEndDate.substring(0, indexOf + 7);
                }else{
                    expEndDate = expEndDate.substring(0, indexOf + 8);
                }
            }else{
                expEndDate = expEndDate.substring(0, indexOf + 7);
            }
            expEndDate = expEndDate + " 00:00:00";
            Date parse = sdf.parse(expEndDate);
            expEndDate = sdf.format(parse);
        } else {
            expEndDate = "";
        }
        return expEndDate;
    }
}

