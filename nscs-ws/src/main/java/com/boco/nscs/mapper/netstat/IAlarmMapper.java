package com.boco.nscs.mapper.netstat;

import com.boco.nscs.core.mapper.KFBaseMapper;
import com.boco.nscs.entity.netstat.TfaAlarmInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Component
public interface IAlarmMapper extends KFBaseMapper<TfaAlarmInfo> {

    List<TfaAlarmInfo> queryFaultNoticeList(Map<String, Object> map);

    List<TfaAlarmInfo> queryAtmoNoticeList(Map<String, Object> map);

}
