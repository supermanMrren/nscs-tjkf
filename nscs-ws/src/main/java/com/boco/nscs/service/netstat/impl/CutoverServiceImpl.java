package com.boco.nscs.service.netstat.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.boco.nscs.core.entity.SearchCriteria;
import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.core.entity.kf.KFRespDataUtil;
import com.boco.nscs.core.exception.NscsException;
import com.boco.nscs.core.exception.NscsExceptionEnum;
import com.boco.nscs.core.ptcc.PTCCClient;
import com.boco.nscs.core.ptcc.SearchCommand;
import com.boco.nscs.core.ptcc.SearchParameterSingle;
import com.boco.nscs.core.ptcc.SearchResultSingle;
import com.boco.nscs.core.service.KFBaseServiceImpl;
import com.boco.nscs.entity.common.BsInfo;
import com.boco.nscs.entity.netstat.CutoverInfo;
import com.boco.nscs.entity.netstat.CutoverInfoVo;
import com.boco.nscs.entity.netstat.CutoverReq;
import com.boco.nscs.mapper.netstat.ICutoverMapper;
import com.boco.nscs.service.netstat.ICutoverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CutoverServiceImpl extends KFBaseServiceImpl<ICutoverMapper,CutoverInfo> implements ICutoverService {

    private static final Logger logger = LoggerFactory.getLogger(CutoverServiceImpl.class);
    @Override
    protected String getResultKey() {
        return "engiList";
    }

    @Value("${tjUrl}")
    private String url;

    @Value("${tjTableName}")
    private String tableName;

    @Value("${distanceKM}")
    private String distanceKM;

    @Value("${pageSize}")
    private String pageSize;

    @Cacheable(cacheNames = "cutcover")  //缓存
    @Override
    public KFRespData<CutoverInfoVo> releaseEngineerInfo(CutoverReq req,String county) {
        if (req==null){
            throw NscsExceptionEnum.REQUEST_PARAM_NULL.getException();
        }
        SearchCriteria param = req.parse2CondBean();
        param.setIsPage(false);
        ArrayList<CutoverInfoVo> list = new ArrayList<CutoverInfoVo>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long qnStartTime = System.currentTimeMillis();
            List<CutoverInfo> queryList = mapper.queryNenameList(param.getQueryMap());
            if(queryList==null){
                throw new NscsException(NscsExceptionEnum.Query_ERROR,"查询失败，查询数据为null！");
            }
            logger.debug("query queryList:{}", JSONUtil.toJsonStr(queryList));
            long qnEndTime = System.currentTimeMillis();
            logger.debug("queryQnTable  qnCastTime:{}ms", qnEndTime - qnStartTime);
            List<String> neNameIdList = new ArrayList<String>();
            //获取网元名称中的所有数字id
            for (CutoverInfo cutoverInfo : queryList) {
                String neNameNum = getNum(cutoverInfo.getNename());
                if (neNameNum != null && neNameNum != "" && !neNameIdList.contains(neNameNum)) {
                    neNameIdList.add(neNameNum);
                }
            }
            logger.debug("query neNameIdList:{}", JSONUtil.toJsonStr(neNameIdList));
            List<BsInfo> bsQueryList = new ArrayList<BsInfo>();
            String bsid = "";
            long bsEndTime = 0;
            if (neNameIdList != null && neNameIdList.size() > 0) {
                for (String id : neNameIdList) {
                    bsid += "bsid = '" + id + "' or ";
                }
                bsid = bsid.substring(0, bsid.lastIndexOf("or"));
                bsid = "(" + bsid + ")";
                param.getQueryMap().put("bsid", bsid);
                long bsStartTime = System.currentTimeMillis();
                //获取基站表中bsid在网元名称能对应上的数字的所有基站信息
                bsQueryList = mapper.queryListByBsid(param.getQueryMap());
                bsEndTime = System.currentTimeMillis();
                logger.debug("queryBsTable bsCastTime:{}ms", bsEndTime - bsStartTime);
            }
            for (CutoverInfo cutoverInfo : queryList) {
                cutoverInfo.setProvince(req.getProvince());
                cutoverInfo.setCity(req.getCity());
                cutoverInfo.setCounty(county);
                if (StrUtil.isBlank(cutoverInfo.getEndDate())) {
                    cutoverInfo.setStatus("2");
                } else {
                    if (sdf.parse(cutoverInfo.getEndDate()).getTime() > System.currentTimeMillis()) {
                        cutoverInfo.setStatus("2");
                    } else {
                        cutoverInfo.setStatus("1");
                    }
                }
                cutoverInfo.setInterpretCaliber("");
                CutoverInfoVo cutoverInfoVo = new CutoverInfoVo();
                boolean flag = true;
                for (BsInfo bsInfo : bsQueryList) {
                    //判断基站表与网元表对应上的数据
                    if (getNum(cutoverInfo.getNename()).equals(bsInfo.getId())) {
                        flag = false;
                        //筛选区县匹配上的数据
                        if (bsInfo.getCounty().equals(req.getCounty())) {
                            String longitude = bsInfo.getLongitude();
                            String latitude = bsInfo.getLatitude();
                            String influence = getInfluence(longitude, latitude);
                            if (StrUtil.isNotBlank(influence)) {
                                cutoverInfo.setInfluence(influence);
                            } else {
                                cutoverInfo.setInfluence("");
                            }
                            BeanUtils.copyProperties(cutoverInfo, cutoverInfoVo);
                            list.add(cutoverInfoVo);
                        }
                    }
                }
                if (flag) {//未在基站表匹配上的数据
                    cutoverInfo.setInfluence(cutoverInfo.getNename());
                    BeanUtils.copyProperties(cutoverInfo, cutoverInfoVo);
                    list.add(cutoverInfoVo);
                }
            }
            return KFRespDataUtil.success(getResultKey(), list);
        }catch (NscsException ex){
            throw new NscsException(NscsExceptionEnum.REQUEST_URL_ERROR,"查询失败，连接检索平台错误！");
        }catch (Exception ex) {
            if(ex instanceof DataAccessException){
                logger.warn("数据库查询失败",ex);
                throw new NscsException(NscsExceptionEnum.SERVER_DB_ERROR,"查询失败，数据库查询错误！");
            }
            logger.warn("查询失败", ex);
            throw new NscsException(NscsExceptionEnum.Query_ERROR,"查询失败,"+ex.getMessage());
        }
    }
    public List<CutoverInfo> queryList(SearchCriteria param){
        return mapper.queryList(param.getQueryMap());
    }

    private String getInfluence(String longitude,String latitude) throws NscsException{
        SearchParameterSingle searchParam = new SearchParameterSingle();
        if(longitude!=null&&longitude!=""&&latitude!=null&&latitude!=""){
            searchParam.getSearchCommands().add(new SearchCommand("XY",Double.parseDouble(longitude),Double.parseDouble(latitude),Double.parseDouble(distanceKM)));
        }
        searchParam.setSortColumns(new String[]{"CITYCODE"});
        searchParam.setPageSize(Integer.parseInt(pageSize));
        int count=0;
        List<String[]> rows=null;
        while (count<2) {
            try {
                PTCCClient.init(url);
                SearchResultSingle result = PTCCClient.singleSearch(tableName, searchParam);
                rows = result.getRows();
                break;
            } catch (Exception e) {
                if(count==1){
                    logger.debug("连接检索平台错误:{}", e.getMessage());
                    throw NscsExceptionEnum.REQUEST_URL_ERROR.getException();
                }
                count++;
            }
        }
        String influtence = "";
        for (String[] influArr : rows){
            influtence += influArr[2] + ";";
        }
        return influtence;
    }

    private String getNum(String neName) {
        String neNameNum = "";
        String numStr = "";
        String strOld="";
        String strNew="";
        if(neName.indexOf("[")>=0){
            neNameNum = neName.substring(neName.indexOf("[")+1,neName.indexOf("]"));
        }else if(neName.indexOf("_")>0){
            if(neName.substring(neName.indexOf("_")-1,neName.indexOf("_")).matches("[0-9]")){
                numStr = neName.substring(0,neName.indexOf("_"));
                for (int i = (numStr.length() - 1); i >= 0; i--) {
                    strOld += (numStr.charAt(i));
                }
                strNew = getNumbers(strOld);
                for (int i = (strNew.length() - 1); i >= 0; i--) {
                    neNameNum += (strNew.charAt(i));
                }
            }else if(neName.substring(neName.indexOf("_")+1,neName.indexOf("_")+2).matches("[0-9]")){
                numStr = neName.substring(neName.indexOf("_"));
                neNameNum = getNumbers(numStr);
            }
        }
        return neNameNum;
    }

    private String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

}
