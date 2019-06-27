package com.boco.nscs.mapper.netstat;

import com.boco.nscs.core.mapper.KFBaseMapper;
import com.boco.nscs.entity.common.BsInfo;
import com.boco.nscs.entity.netstat.CutoverInfo;
import com.boco.nscs.entity.netstat.CutoverInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface ICutoverMapper extends KFBaseMapper<CutoverInfo> {

    List<CutoverInfo> queryNenameList(Map<String,Object> map);

    List<BsInfo> queryListByBsid(Map<String,Object> map);
}
