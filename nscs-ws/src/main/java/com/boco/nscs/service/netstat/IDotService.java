package com.boco.nscs.service.netstat;

import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.core.service.KFBaseService;
import com.boco.nscs.entity.netstat.CutoverInfo;
import com.boco.nscs.entity.netstat.TransferFaultReq;

/**
 * Created by rc on 2019/5/15.
 */
public interface IDotService {
    KFRespData transferFaultInfo(TransferFaultReq req);
}
