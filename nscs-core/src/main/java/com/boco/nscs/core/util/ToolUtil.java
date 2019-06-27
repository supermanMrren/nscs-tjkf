package com.boco.nscs.core.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.boco.nscs.core.entity.BizPager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 高频方法集合类
 */
public class ToolUtil {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(ToolUtil.class);

    private ToolUtil() {
    }

    /**
     * 获取异常的具体信息
     */
    public static String getExceptionMsg(Exception e) {
        StringWriter sw = new StringWriter();
        try{
            e.printStackTrace(new PrintWriter(sw));
        }finally {
            try {
                sw.close();
            } catch (IOException e1) {
                logger.warn("error",e1);
            }
        }
        return sw.getBuffer().toString().replaceAll("\\$","T");
    }

    /**
     * 强转->string,并去掉多余空格
     *
     * @param str
     * @return
     */
    public static String toStr(Object str) {
        return toStr(str, "");
    }

    /**
     * 强转->string,并去掉多余空格
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static String toStr(Object str, String defaultValue) {
        if (null == str) {
            return defaultValue;
        }
        return str.toString().trim();
    }


    /**
     * 获取map中第一个数据值
     *
     * @param <K> Key的类型
     * @param <V> Value的类型
     * @param map 数据源
     * @return 返回的值
     */
    public static <K, V> V getFirstOrNull(Map<K, V> map) {
        V obj = null;
        for (Entry<K, V> entry : map.entrySet()) {
            obj = entry.getValue();
            if (obj != null) {
                break;
            }
        }
        return obj;
    }


    /**
     * 获取临时目录
     *
     * @author stylefeng
     * @Date 2017/5/24 22:35
     */
    public static String getTempPath(){
        return System.getProperty("java.io.tmpdir");
    }

    public static <T> MultiValueMap<String, Object> bean2MutiMap(T bean){
        Map<String, Object> smap = BeanUtil.beanToMap(bean);
        MultiValueMap<String, Object> map= new LinkedMultiValueMap<>();
        for (Entry<String, Object> entry : smap.entrySet()) {
            if (entry.getValue()!=null) {
                map.add(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    public static String[] list2Array(List<String> list) {
        return list.toArray(new String[list.size()]);
    }

    public static Boolean IsPicFile(String file){
        if (StrUtil.isBlank(file))
            return false;
        String fileType= FileUtil.extName(file).toLowerCase();
        if (fileType.endsWith("jpg")||fileType.endsWith("png")|| fileType.endsWith("bmp")){
            return true;
        }
        else
            return false;
    }
    /**
     * 判断是否是简单值类型.包括：基础数据类型、CharSequence、Number、Date、Class;
     *
     * @param clazz
     * @return
     */
    public static boolean isSimpleValueType(Class<?> clazz) {
        return (ClassUtils.isPrimitiveOrWrapper(clazz)
                || clazz.isEnum()
                || CharSequence.class.isAssignableFrom(clazz)
                || Number.class.isAssignableFrom(clazz)
                || Date.class.isAssignableFrom(clazz)
                || BizPager.class.isAssignableFrom(clazz)
                || Class.class == clazz);
    }

    public static String getClientIP(HttpServletRequest request ){
        String ip = request.getHeader("X-Forwarded-For");
        if (StrUtil.isBlank(ip) || ip.equalsIgnoreCase("unknown")){
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}