package com.boco.nscs.service.netstat;

import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.core.service.KFBaseService;
import com.boco.nscs.entity.netstat.NetCoverInfo;
import com.boco.nscs.entity.netstat.NetCoverReq;
import com.boco.nscs.entity.netstat.TfaAlarmInfo;
import com.boco.nscs.entity.netstat.TfaAlarmReq;

public interface INetCoverService extends KFBaseService<NetCoverInfo> {

    KFRespData qryCoverInfo(NetCoverReq req,String county);
}
