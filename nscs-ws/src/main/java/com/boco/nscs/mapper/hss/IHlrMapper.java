package com.boco.nscs.mapper.hss;

import com.boco.nscs.entity.hss.HlrSearch;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by rc on 2019/6/4.
 */
@Component
public interface IHlrMapper {

    String qryHlrSearch(@Param("servNumber")String servNumber, @Param("callTime")String callTime);

    void insertHrlSearch(HlrSearch hlrSearch);
}
