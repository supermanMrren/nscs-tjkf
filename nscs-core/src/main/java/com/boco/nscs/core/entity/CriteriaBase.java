package com.boco.nscs.core.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CC on 2017/4/26.
 */
public class CriteriaBase {
    private Map<String,Object> condition = new HashMap<String,Object>() ;
    public Map<String, Object> getCondition() {
        return condition;
    }
    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
    }

    public void putCriteria(String key,Object value){
        this.condition.put(key,value);
    }
}
