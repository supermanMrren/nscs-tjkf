package com.boco.nscs.service.netstat;

import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.core.service.KFBaseService;

import com.boco.nscs.entity.netstat.CutoverInfo;
import com.boco.nscs.entity.netstat.CutoverReq;

public interface ICutoverService extends KFBaseService<CutoverInfo> {
    KFRespData releaseEngineerInfo(CutoverReq req,String county);
}
