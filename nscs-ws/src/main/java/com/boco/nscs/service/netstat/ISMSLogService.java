package com.boco.nscs.service.netstat;

import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.core.service.KFBaseService;
import com.boco.nscs.entity.netstat.TfaAlarmInfo;
import com.boco.nscs.entity.netstat.TransferFaultReq;
import com.boco.nscs.entity.sms.RelSmsInfo;
import com.boco.nscs.entity.sms.SMSLogInfo;
import com.boco.nscs.entity.sms.SMSLogReq;

/**
 * Created by rc on 2019/5/15.
 */
public interface ISMSLogService extends KFBaseService<RelSmsInfo> {
    KFRespData qrySMSLog(SMSLogReq req);
}
