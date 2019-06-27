package com.boco.nscs.common.utils;

import com.boco.nscs.core.exception.NscsExceptionEnum;
import com.boco.nscs.core.exception.ParamErrorException;
import com.boco.nscs.core.util.JsonUtil;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wong on 2017/5/29.
 */
public class FileUtil {
    private  static Logger log = LoggerFactory.getLogger(FileUtil.class);
    public static String readWSRequstXml(String filePath) throws Exception {
        StringBuilder result = new StringBuilder();
        File file=new File(filePath);
        if(file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(s);
            }
            br.close();
        }
        return result.toString();
    }

    public  static String getTxtInfo(String fileUrl) throws Exception{
        String data = "";
        try{
            ClassPathResource cpr = new ClassPathResource(fileUrl);
            byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            data = new String(bdata, StandardCharsets.UTF_8);
            return data;
        }catch (Exception ex){
            log.warn("读取内容异常：{}",ex);
        }
        return null;
    }

    public static Map<String,String> getKeyValue(InputStream inputStream){
        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = null;
        Document document = null;
        try {
            reader = new SAXReader();
            document = reader.read(inputStream);
            Element root = document.getRootElement();
            List<Element> childElements = root.elements();
            for (Element child : childElements) {
                List<Attribute> attributeList = child.attributes();
                map.put(attributeList.get(0).getValue(), attributeList.get(1).getValue());
            }
            return map;
        } catch (DocumentException e) {
            log.warn("读取配置文件失败",e);
            return null;
        }finally {
            if (inputStream!=null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.warn("IO流关闭异常", e);
                }
            }
        }
    }
}
