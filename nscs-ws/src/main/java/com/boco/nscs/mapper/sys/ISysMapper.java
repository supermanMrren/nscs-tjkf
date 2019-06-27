package com.boco.nscs.mapper.sys;

import com.boco.nscs.entity.sys.WsCallLog;
import org.springframework.stereotype.Component;

@Component
public interface ISysMapper {
    void insertWsCallLog(WsCallLog log);
}
