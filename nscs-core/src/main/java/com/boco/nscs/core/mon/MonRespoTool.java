package com.boco.nscs.core.mon;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.json.JSONUtil;
import javax.servlet.http.HttpServletRequest;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MonRespoTool {
    public static  InfoResult getInfo(HttpServletRequest request){
        InfoResult result = getBaseInfo(request);
        //结果
        Map<String, String> map = System.getenv();
//        String userName = map.get("USERNAME");// 获取用户名
        String computerName = map.get("COMPUTERNAME");// 获取计算机名
//        String userDomain = map.get("USERDOMAIN");// 获取计算机域名
        Properties props = System.getProperties();

        Map<String,String> items = new HashMap<>();
        items.put("server.machineName",computerName);
        items.put("server.host",NetUtil.getLocalhostStr());
        items.put("server.cpu","");
        items.put("server.name",request.getServerName());
        items.put("server.os",props.getProperty("os.name") );
        items.put("server.os.arch",props.getProperty("os.arch") );
        items.put("server.os.version",props.getProperty("os.version") );

        items.put("app.ip",request.getLocalAddr());
        items.put("app.port",Integer.toString(request.getLocalPort()));
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
        String pid = mxBean.getName().split("@")[0];
        items.put("app.pid",pid);
        items.put("app.siteName","");
        items.put("app.startTime",DateUtil.formatDateTime( new Date(mxBean.getStartTime())) );
        items.put("app.path",System.getProperty("user.dir"));
        items.put("app.language","java");
        items.put("app.appServer","tomcat");
        items.put("app.javaVer",props.getProperty("java.runtime.version"));
        items.put("app.appServerVer","8.5");
        items.put("app.cpuCount",Integer.toString(Runtime.getRuntime().availableProcessors()));
        result.setItems(items);

        return result;
    }

    public static InfoResult getStat(HttpServletRequest request){
        InfoResult result = getBaseInfo(request);

        Map<String,String> items = new HashMap<>();
        result.setItems(items);
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();

        items.put("app.upTime",Long.toString(mxBean.getUptime()));
        items.put("app.memory ",Long.toString(Runtime.getRuntime().totalMemory()));
        //items.put("app.totalMemory ",Long.toString(Runtime.getRuntime().totalMemory()));
        //items.put("app.freeMemory ",Long.toString(Runtime.getRuntime().freeMemory()));
        //items.put("app.maxMemory  ",Long.toString(Runtime.getRuntime().maxMemory ()));

        // 获得线程总数
        ThreadGroup parentThread;
        for (parentThread = Thread.currentThread().getThreadGroup();
             parentThread.getParent() != null;
             parentThread = parentThread.getParent());
        int totalThread = parentThread.activeCount();
        items.put("app.totalThread",Integer.toString(totalThread));

        //pv
        items.put("app.pvTotal", Integer.toString(AppPVTool.getPvTotal()));
        items.put("app.pv", JSONUtil.toJsonStr(AppPVTool.getPv()));
        return result;
    }

    private static InfoResult getBaseInfo(HttpServletRequest request){
        InfoResult result = new InfoResult();
        result.setCode(1);
        result.setHost(NetUtil.getLocalhostStr());
        result.setType("web");
        result.setGetTIme(DateUtil.now());
        return result;
    }
    public static InfoResult getCpuLoad(HttpServletRequest request){
        InfoResult result = getBaseInfo(request);
        Map<String,String> items = new HashMap<>();
        result.setItems(items);
        items.put("app.cpuload", "0");
        return result;
    }
}
