package com.boco.nscs.mapper.netstat;

import com.boco.nscs.core.mapper.KFBaseMapper;
import com.boco.nscs.entity.netstat.NetCoverInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface INetCoverMapper extends KFBaseMapper<NetCoverInfo> {

    List<NetCoverInfo> queryWeakcoverList(Map<String, Object> map);

    List<NetCoverInfo> queryWeakcoverHotList(Map<String, Object> map);

}
