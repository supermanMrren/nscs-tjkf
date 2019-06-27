package com.boco.nscs.service.netstat;

import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.core.service.KFBaseService;
import com.boco.nscs.entity.netstat.TfaAlarmInfo;
import com.boco.nscs.entity.netstat.TfaAlarmReq;

public interface IAlarmService extends KFBaseService<TfaAlarmInfo> {

    KFRespData releaseFaultInfo(TfaAlarmReq req,String county);
}
