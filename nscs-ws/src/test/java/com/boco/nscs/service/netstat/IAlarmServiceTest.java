package com.boco.nscs.service.netstat;

import cn.hutool.json.JSONUtil;
import com.boco.nscs.TestBase;
import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.entity.netstat.CutoverInfo;
import com.boco.nscs.entity.netstat.CutoverReq;
import com.boco.nscs.entity.netstat.TfaAlarmInfo;
import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class IAlarmServiceTest extends TestBase {

    @Autowired
    IAlarmService srv;

    @org.junit.Test
    public void testGet(){
        CutoverReq req = new CutoverReq();
        req.setInfluence("万象城");
        KFRespData<TfaAlarmInfo> data = srv.get(req);
        assertThat(data).isNotNull();
        System.out.println(JSONUtil.toJsonStr(data));
    }
}