package com.boco.nscs.entity.netstat;

import cn.hutool.core.bean.BeanUtil;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class CutoverReqTest {
    @Test
    public void bean2Map(){
        CutoverReq req = new CutoverReq();
        req.setCity("成都");

        req.getPubInfo().setOrgId("20190001");

        Map<String, Object> map = BeanUtil.beanToMap(req);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String val ="";
            if (entry.getValue()!=null){
                val = entry.getValue().toString();
            }
            System.out.println(entry.getKey()+":"+val);
        }
    }
}