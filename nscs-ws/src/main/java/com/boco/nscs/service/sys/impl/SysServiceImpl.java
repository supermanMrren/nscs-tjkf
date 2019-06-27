package com.boco.nscs.service.sys.impl;

import cn.hutool.json.JSONUtil;
import com.boco.nscs.entity.sys.WsCallLog;
import com.boco.nscs.mapper.sys.ISysMapper;
import com.boco.nscs.service.sys.ISysService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysServiceImpl implements ISysService {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(SysServiceImpl.class);
    @Autowired
    ISysMapper mapper;
    @Override
    public void addWsCallLog(WsCallLog log) {
        //todo 记录日志到数据库
        mapper.insertWsCallLog(log);
    }
}
