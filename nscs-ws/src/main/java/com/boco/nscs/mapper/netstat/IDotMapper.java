package com.boco.nscs.mapper.netstat;

import com.boco.nscs.entity.netstat.TransferFaultReq;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by rc on 2019/5/15.
 */
@Component
public interface IDotMapper {

    void insertDotInfo(TransferFaultReq dotInfo);

    int qryWeakcoverById(String id);

    int qryHotWeakcoverById(String id);

    int qryNoticeById(String id);

    int qryAtoNoticeById(String id);

    int qryProStaById(String id);
}
