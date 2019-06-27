package com.boco.nscs.service.hss;

import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.entity.hss.HlrReq;

public interface IHlrService {
    KFRespData qryHInfo(HlrReq req);

    KFRespData clearHlrHssInfo(HlrReq req);

    String qryHlrSearch(HlrReq req);

    void insertHrlSearch(HlrReq req);
}
