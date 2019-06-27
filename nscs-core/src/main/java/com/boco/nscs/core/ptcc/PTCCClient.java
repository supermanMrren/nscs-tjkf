package com.boco.nscs.core.ptcc;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PTCCClient {
    static Log logger = LogFactory.get();

    private static String controllerAddress;
    private static ClientInfo clientInfo;

    public static void init(String address) throws Exception {
        controllerAddress = address;
        if (StrUtil.isBlank(controllerAddress)) {
            throw new Exception("控制器地址不能为空");
        }
        if (clientInfo != null) {
            return;
        }
        clientInfo = doinit();
    }

    public static SearchResultSingle singleSearch(String tableName, SearchParameterSingle param) throws Exception {
        TableClientInfo info = getTableInfo(tableName);
        String serAdd = getServiceAddress(info);

        SearchResultSingle result = doSingleSearch(serAdd, param);
        return result;
    }
    private static TableClientInfo getTableInfo(String tableName) throws Exception {
        if (clientInfo == null) {
            throw new Exception("未初始化控制器");
        }

        //检查是否存在对应检索服务
        TableClientInfo info = clientInfo.getTableClientInfo(tableName);
        if (info == null || info.getServerList() == null || info.getServerList().size() == 0) {
            throw new Exception("未找到对应的检索服务");
        }
        return info;
    }
    private static String getServiceAddress(TableClientInfo info){
        //获取随机服务地址
        String serAdd = "";
        if (info.getServerList().size() == 1) {
            serAdd = info.getServerList().get(0);
        } else {
            //随机
            int i = RandomUtil.randomInt(info.getServerList().size());
            serAdd = info.getServerList().get(i);
        }
        return serAdd;
    }
    public static Integer  GetTotalCount(String tableName) throws Exception {
        TableClientInfo info = getTableInfo(tableName);
        String serAdd = getServiceAddress(info);
        return doGetTotalCount(serAdd);
    }

    private static ClientInfo doinit() throws Exception {
        logger.debug("init");
        String action = "http://tempuri.org/IClientControllerService/GetClientInfo";
        String cmd = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<s:Body>" +
                "<GetClientInfo xmlns=\"http://tempuri.org/\"/>" +
                "</s:Body>" +
                "</s:Envelope>";
        String socketTimeout = readConfig("socketTimeout");
        HttpResponse execute = HttpUtil.createPost(controllerAddress).timeout(Integer.parseInt(socketTimeout))
                .header("SOAPAction", action)
                .header("Content-Type", "text/xml; charset=utf-8")
                .body(cmd).execute();
        if (execute.isOk()) {
            logger.debug("init ok");

            String result = execute.body();
            logger.debug("result:{}", result);
            try {
                ClientInfo clientInfo = parseClientInfo(result);
                return clientInfo;
            } catch (Exception ex) {
                throw new Exception("解析服务器端信息失败", ex);
            }
        } else {
            throw new Exception("连接控制器失败");
        }
    }

    private static String readConfig(String key) {
        ClassPathResource resource = new ClassPathResource("config.properties");
        Properties properties = new Properties();
        try {
            properties.load(resource.getStream());
            return properties.getProperty(key);
        } catch (IOException e) {
            logger.error(e,"读取config.properties出错！"); // e.printStackTrace();
        }

        return null;
    }

    private static SearchResultSingle doSingleSearch(String address, SearchParameterSingle param) throws Exception {
        if (param == null) {
            param = new SearchParameterSingle();
        }
        String action = "http://tempuri.org/IServiceSingle/Search";
        String surl = address;
        String cmd = param.GenSeachCmd();

        logger.debug("search cmd:{}", cmd);
        HttpResponse execute = HttpUtil.createPost(surl)
                .header("SOAPAction", action)
                .header("Content-Type", "text/xml; charset=utf-8")
                .body(cmd).execute();

        if (execute.isOk()) {
            logger.debug("search ok");

            String result = execute.body();
            logger.debug("result:{}", result);
            try {
                SearchResultSingle single = parseResultSingle(result);
                return single;
            } catch (Exception ex) {
                throw new Exception("解析信息失败", ex);
            }
        } else {
            throw new Exception("查询失败");
        }
    }

    private static Integer doGetTotalCount(String address) throws Exception {
        String action = "http://tempuri.org/IServiceSingle/GetTotalCount";
        String surl = address;
        String cmd = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body><GetTotalCount xmlns=\"http://tempuri.org/\"/></s:Body></s:Envelope>";

        HttpResponse execute = HttpUtil.createPost(surl)
                .header("SOAPAction", action)
                .header("Content-Type", "text/xml; charset=utf-8")
                .body(cmd).execute();

        if (execute.isOk()) {
            String result = execute.body();
            logger.debug("result:{}", result);
            try {
                JSONObject obj = JSONUtil.xmlToJson(result);
                //s:Envelope/s:Body
                JSONObject objRoot = obj.getJSONObject("s:Envelope").getJSONObject("s:Body");
                Integer anInt = objRoot.getJSONObject("GetTotalCountResponse").getInt("GetTotalCountResult");
                return anInt;
            } catch (Exception ex) {
                throw new Exception("解析信息失败", ex);
            }
        } else {
            throw new Exception("查询失败");
        }
    }
    public static ClientInfo parseClientInfo(String xml) {
        JSONObject obj = JSONUtil.xmlToJson(xml);
        //s:Envelope/s:Body
        JSONObject objRoot = obj.getJSONObject("s:Envelope").getJSONObject("s:Body");
        JSONObject objDataRoot = objRoot.getJSONObject("GetClientInfoResponse").getJSONObject("GetClientInfoResult");
        ClientInfo info = new ClientInfo();
        //获取指定数据
        //表
        //a:tableNames/b:string
        JSONArray arrayTablesNames = objDataRoot.getJSONObject("a:tableNames").getJSONArray("b:string");
        for (Object o : arrayTablesNames) {
            info.getTableNames().add(o.toString());
        }

        //检索服务
        //a:tablesServerList/b:ArrayOfstring/b:string
        JSONArray arrayServerList = objDataRoot.getJSONObject("a:tablesServerList").getJSONArray("b:ArrayOfstring");
        for (Object o : arrayServerList) {
            List<String> list = new ArrayList<>();
            if (o != null && StrUtil.isNotBlank(o.toString())) {
                list = parseJsonObj2List(((JSONObject) o), "b:string");
            }
            info.getTablesServerList().add(list);
        }

        //字段列
        //a:tablesColumns/b:ArrayOfstring/b:string
        JSONArray arrayCols = objDataRoot.getJSONObject("a:tablesColumns").getJSONArray("b:ArrayOfstring");
        for (Object o : arrayCols) {
            List<String> list = new ArrayList<>();
            if (o != null && StrUtil.isNotBlank(o.toString())) {
                list = parseJsonObj2List(((JSONObject) o), "b:string");
            }
            info.getTablesColumns().add(list);
        }

        //字段类型
        //a:tablesColumnsType/b:ArrayOfint/b:int
        JSONArray arrayColTypes = objDataRoot.getJSONObject("a:tablesColumnsType").getJSONArray("b:ArrayOfint");
        for (Object o : arrayColTypes) {
            List<Integer> list = new ArrayList<>();
            if (o != null && StrUtil.isNotBlank(o.toString())) {
                JSONArray array = ((JSONObject) o).getJSONArray("b:int");
                for (Object o1 : array) {
                    list.add(Convert.toInt(o1.toString(), 0));
                }
            }
            info.getTablesColumnsType().add(list);
        }

        //返回字段
        //a:tablesReturnColumns/b:ArrayOfstring/b:string
        JSONArray arrayRtnCols = objDataRoot.getJSONObject("a:tablesReturnColumns").getJSONArray("b:ArrayOfstring");
        for (Object o : arrayRtnCols) {
            List<String> list = new ArrayList<>();
            if (o != null && StrUtil.isNotBlank(o.toString())) {
                list = parseJsonObj2List(((JSONObject) o), "b:string");
            }
            info.getTablesReturnColumns().add(list);
        }

        //天津检索服务专用 弱覆盖、公告
        //a:weakconverServerList/b:string
        JSONObject weakServerlst = objDataRoot.getJSONObject("a:weakconverServerList");
        List<String> list1 = parseJsonObj2List(weakServerlst, "b:string");
        info.setWeakconverServerList(list1);
        //a:weakconverReturnColumns/b:string
        JSONObject weakServerCol = objDataRoot.getJSONObject("a:weakconverReturnColumns");
        List<String> list2 = parseJsonObj2List(weakServerCol, "b:string");
        info.setWeakconverReturnColumns(list2);

        JSONObject noticeServerlst = objDataRoot.getJSONObject("a:noticeServerList");
        List<String> list3 = parseJsonObj2List(noticeServerlst, "b:string");
        info.setNoticeServerList(list3);
        //a:noticeReturnColumns/b:string
        JSONObject noticeServerCol = objDataRoot.getJSONObject("a:noticeReturnColumns");
        List<String> list4 = parseJsonObj2List(noticeServerCol, "b:string");
        info.setNoticeReturnColumns(list4);
        return info;
    }

    public static SearchResultSingle parseResultSingle(String xml) {
        JSONObject obj = JSONUtil.xmlToJson(xml);
        //s:Envelope/s:Body
        JSONObject objRoot = obj.getJSONObject("s:Envelope").getJSONObject("s:Body");
        JSONObject objDataRoot = objRoot.getJSONObject("SearchResponse").getJSONObject("SearchResult");
        SearchResultSingle result = new SearchResultSingle();

        Integer anInt = objDataRoot.getInt("a:count");
        if (anInt != null) {
            result.setCount(anInt);
        }
        //a:pageCount
        Integer pageCount = objDataRoot.getInt("a:pageCount");
        if (pageCount != null) {
            result.setPageCount(pageCount);
        }
        //a:pageIndex
        Integer pageIndex = objDataRoot.getInt("a:pageIndex");
        if (pageIndex != null) {
            result.setPageIndex(pageIndex);
        }
        //a:distanceList
        JSONObject disObj = objDataRoot.getJSONObject("a:distanceList");
        if (disObj != null && !disObj.isEmpty()) {
            List<Double> disList = new ArrayList<>();
            Object o1 = disObj.get("b:double");
            if (o1 == null) {

            } else if (o1 instanceof JSONObject) {
                disList.add(Convert.toDouble(o1.toString(), 0.0));
            } else if (o1 instanceof JSONArray) {
                for (Object o2 : (JSONArray) o1) {
                    disList.add(Convert.toDouble(o2.toString(), 0.0));
                }
            } else {
                disList.add(Convert.toDouble(o1.toString(), 0.0));
            }
            result.setDistanceList(disList);
        }
        //a:rows
        JSONObject rowObj = objDataRoot.getJSONObject("a:rows");
        if (rowObj != null && !rowObj.isEmpty()) {
            Object array = rowObj.get("b:ArrayOfstring");
            if (array instanceof JSONArray) {
                //多条数据
                for (Object ro : (JSONArray) array) {
                    List<String> rowlst = new ArrayList<>();
                    JSONArray array1 = ((JSONObject) ro).getJSONArray("b:string");
                    for (Object o : array1) {
                        rowlst.add(o.toString());
                    }
                    result.getRows().add(ArrayUtil.toArray(rowlst, String.class));
                }
            } else if (array instanceof JSONObject) {
                //单条数据
                List<String> rowlst = new ArrayList<>();
                JSONArray array1 = ((JSONObject) array).getJSONArray("b:string");
                for (Object o : array1) {
                    rowlst.add(o.toString());
                }
                result.getRows().add(ArrayUtil.toArray(rowlst, String.class));
            }
        }


        return result;
    }

    private static List<String> parseJsonObj2List(JSONObject obj, String key) {
        List<String> list = new ArrayList<>();
        if (obj != null) {
            Object o2 = obj.get(key);
            if (o2 instanceof String) {
                list.add(o2.toString());
            } else if (o2 instanceof JSONObject) {
                list.add(o2.toString());
            } else if (o2 instanceof JSONArray) {
                for (Object o1 : (JSONArray) o2) {
                    list.add(o1.toString());
                }
            }
        }
        return list;
    }
}
