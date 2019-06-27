package com.boco.nscs.service.netstat;

import cn.hutool.json.JSONUtil;
import com.boco.nscs.TestBase;
import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.entity.netstat.CutoverInfo;
import com.boco.nscs.entity.netstat.CutoverReq;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

public class ICutoverServiceTest extends TestBase{
    @Autowired
    ICutoverService srv;

    @Test
    public void testGet(){
        CutoverReq req = new CutoverReq();
        req.setInfluence("万象城");
        KFRespData<CutoverInfo> data = srv.get(req);
        assertThat(data).isNotNull();
        System.out.println(JSONUtil.toJsonStr(data));
    }
}