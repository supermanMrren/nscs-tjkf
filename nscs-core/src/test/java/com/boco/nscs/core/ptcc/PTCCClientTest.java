package com.boco.nscs.core.ptcc;

import cn.hutool.json.JSONUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class PTCCClientTest {
    String url="http://192.168.10.40:8700/ClientController";
    String tableName="GOOGLEMAPS_TJ";

    @Test
    public void singleSearch() throws Exception {
        PTCCClient.init(url);

        SearchParameterSingle param = new SearchParameterSingle();
        //param.getSearchCommands().add(new SearchCommand("NAME","黑龙江"));
        //126.334921,52.252701
        param.getSearchCommands().add(new SearchCommand("XY",117.0388930000,39.1058940000,1.0));
        param.setSortColumns(new String[]{"CITYCODE"});
        SearchResultSingle result = PTCCClient.singleSearch(tableName, param);
        System.out.println(JSONUtil.toJsonPrettyStr(result));
    }

    @Test
    public void getTotalCount() throws Exception {
        PTCCClient.init(url);
        Integer count = PTCCClient.GetTotalCount(tableName);
        System.out.println(count);
    }

}