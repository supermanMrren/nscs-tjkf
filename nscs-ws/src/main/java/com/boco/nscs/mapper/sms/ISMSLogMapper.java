package com.boco.nscs.mapper.sms;

import com.boco.nscs.core.mapper.KFBaseMapper;
import com.boco.nscs.entity.sms.RelSmsInfo;
import com.boco.nscs.entity.sms.SMSLogInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by rc on 2019/5/16.
 */
@Component
public interface ISMSLogMapper extends KFBaseMapper<RelSmsInfo> {
    List<RelSmsInfo> getSmsLogInfo();
}
