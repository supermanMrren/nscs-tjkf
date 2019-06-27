package com.boco.nscs.service.sys;

import cn.hutool.json.JSONUtil;
import com.boco.nscs.TestBase;
import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.entity.sys.UserInfo;
import com.boco.nscs.entity.sys.UserReq;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class IUserServiceTest extends TestBase {
    @Autowired
    IUserService srv;

    @Test
    public void testGet(){
        UserReq req = new UserReq();
        req.setUserId("test");
        KFRespData<UserInfo> data = srv.get(req);
        assertThat(data).isNotNull();
        System.out.println(JSONUtil.toJsonStr(data));
    }
}