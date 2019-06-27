package com.boco.nscs.service.sys.impl;

import com.boco.nscs.core.entity.kf.KFRequest;
import com.boco.nscs.core.entity.kf.KFRespData;
import com.boco.nscs.core.service.KFBaseServiceImpl;
import com.boco.nscs.entity.sys.UserInfo;
import com.boco.nscs.mapper.sys.IUserMapper;
import com.boco.nscs.service.sys.IUserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends KFBaseServiceImpl<IUserMapper,UserInfo> implements IUserService {

    @Override
    protected String getResultKey() {
        return "userList";
    }

    @Cacheable(value = "user")
    @Override
    public KFRespData get(KFRequest req) {
        return super.get(req);
    }
}
