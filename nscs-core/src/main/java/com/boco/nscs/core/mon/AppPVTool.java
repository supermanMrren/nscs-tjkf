package com.boco.nscs.core.mon;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppPVTool {
    private static ConcurrentHashMap<String,Integer> dicPv =new ConcurrentHashMap<>();//pv统计
    private static String curDay;
    private static String curLogFile;
    private static Integer pvTotal;

    public static void addPv(String ip,String url,String method,Boolean isAjax){
        if (isAjax) return;
        if (!checkUrl(url)) return;
        if (!DateUtil.today().equals(curDay)){
            //初始化
            init();
        }
        incr(url);
        //记录
        String msg = StrUtil.format("{} {} {}",ip,method,url);
        writeRecord(msg);
    }

    public static Map<String,Integer> getPv(){
        return dicPv;
    }

    public static Integer getPv(String url){
        return dicPv.getOrDefault(url,0);
    }
    public static Integer getPvTotal(){
        return pvTotal;
    }

    private static void init(){
        curDay = DateUtil.today();
        String filePath = StrUtil.format("{}{}pv",  System.getProperty("user.dir"), File.separator);
        if (!FileUtil.exist(filePath)){
            FileUtil.mkdir(filePath);
        }
        curLogFile = StrUtil.format("{}{}{}.log",  filePath, File.separator, curDay);
        pvTotal=0;
        dicPv.clear();

        //数据加载
        File file = new File(curLogFile);
        if (file.exists()){
            List<String> strings = FileUtil.readUtf8Lines(file);
            for (String line : strings) {
                String[] cols= line.split(" ");
                if (cols.length==5){
                    incr(cols[4]);
                }
            }
        }
        //历史文件清除
        File[] files = FileUtil.ls(filePath);
        Date checkDate = DateUtil.offsetDay(new Date(),-10);
        for (File f : files) {
            if(f.lastModified() < checkDate.getTime()){
                f.delete();
            }
        }
    }
    private static Boolean checkUrl(String url){
        if (StrUtil.isBlank(url)) return false;
        //排除路径
        if (url.equals("/") || url.equalsIgnoreCase("/Mon/info")
                || url.equalsIgnoreCase("/Mon/stat")
                || url.equalsIgnoreCase("/Mon/cpuload")
                || url.equalsIgnoreCase("/patchcaimg")
                )
            return false;
        //排除目录
        if (url.startsWith("/css/") || url.startsWith("/images/") || url.startsWith("/js/")
                || url.startsWith("/assets/") || url.startsWith("/img/") || url.startsWith("/javascripts/")
                )
            return false;
//        if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".ico")
//                || url.endsWith(".png") || url.endsWith(".gif") || url.endsWith(".jpg")
//                || url.endsWith(".jpeg")
//                )
//            return false;
        return true;
    }
    private static void incr(String url){
        Integer cnt = dicPv.getOrDefault(url,0);
        cnt++;
        dicPv.put(url,cnt);
        pvTotal++;
    }
    private static void writeRecord(String msg){
        if (StrUtil.isBlank(curLogFile)) return;
        if (StrUtil.isBlank(msg)) return;
        msg+= System.getProperty("line.separator");
        FileUtil.appendUtf8String(msg,curLogFile);
    }
}
