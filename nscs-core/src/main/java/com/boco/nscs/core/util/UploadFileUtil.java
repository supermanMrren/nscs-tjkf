package com.boco.nscs.core.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;
import java.util.UUID;

public class UploadFileUtil {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(UploadFileUtil.class);

    public static String getBasePath() throws UnsupportedEncodingException {
        File path = null;
        try {
            path = new File(java.net.URLDecoder.decode(ResourceUtils.getURL("classpath:").getPath(),"utf-8"));
        } catch (FileNotFoundException e) {
            logger.warn("getBasePath fail",e);
        }
        if(!path.exists())
            path = new File("");

        return path.getAbsolutePath();
    }

    public static String getUploadFilePath(String type,String fileName){
        return StrUtil.format("/upload/{}/{}",type,fileName);
    }
    public static String uploadFile(MultipartFile file,String type)throws IOException {
        String name = file.getOriginalFilename();
        String suffixName = name.substring(name.lastIndexOf('.'));
        String newFileName = String.format("%s%s", UUID.randomUUID().toString(), suffixName);
        logger.debug("upload file:{}",newFileName);
        return uploadFile(file,type,newFileName);
    }
    public static String uploadFile(MultipartFile file,String type,String saveFileName)throws IOException {
       if (saveFileName.indexOf(".")<=0){
           String name = file.getOriginalFilename();
           String suffixName = name.substring(name.lastIndexOf('.'));
           saveFileName = String.format("%s.%s", saveFileName, suffixName);
       }

        String fileDir = getBasePath();
        fileDir = fileDir + "/static/upload/" + type + "/";
        if (!FileUtil.exist(fileDir)) {
            FileUtil.mkdir(fileDir);
        }

        logger.debug("upload file:{}",saveFileName);
        File tempFile = new File(fileDir, saveFileName);
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdir();
        }
        if (tempFile.exists()) {
            tempFile.delete();
        }
        tempFile.createNewFile();
        file.transferTo(tempFile);
        return tempFile.getName();
    }

    /*
    *base64图片文件保存
     */
    public static String uploadBase64Img(String imgBase64, String type,String saveFileName) throws IOException {
        if (StrUtil.isBlank(imgBase64))
            return "";
        OutputStream out = null;
        try {
            String imgStr = imgBase64.split(",")[1];
            String suffix = imgBase64.split(",")[0].split("/")[1].split(";")[0];
            Base64.Decoder decoder = Base64.getDecoder();
            //Base64解码
            byte[] bytes = decoder.decode(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {//调整异常数据
                    bytes[i] += 256;
                }
            }

            String fileDir = getBasePath();
            fileDir = fileDir + "/static/upload/" + type + "/";
            if (!FileUtil.exist(fileDir)) {
                FileUtil.mkdir(fileDir);
            }

            String newFileName;
            if (StrUtil.isNotBlank(saveFileName)){
                if(saveFileName.indexOf(".")<=0){
                    newFileName = String.format("%s.%s", saveFileName, suffix);
                }
                else{
                    newFileName = saveFileName;
                }
            }
            else {
                newFileName = String.format("%s%s", UUID.randomUUID().toString(), suffix);
            }

            //新生成的图片
            String imgFilePath = fileDir + newFileName;
            logger.debug("upload file:{}",imgFilePath);
            out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return newFileName;
        } catch (IOException ex) {

        } finally {
            if (out != null)
                out.close();
        }
        return "";
    }
}
