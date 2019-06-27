package com.boco.nscs.core.util;

import com.fasterxml.jackson.databind.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class JsonUtil {
	 private static  ObjectMapper mapper;
    private  static Logger log = LoggerFactory.getLogger(JsonUtil.class);
	 static {
         mapper = new ObjectMapper();
         //日期格式
         mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
         mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
         mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
         mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
         mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
         mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
         mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
         //序列化
         mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
         mapper.configure(SerializationFeature.FLUSH_AFTER_WRITE_VALUE, true);
     }

	 	/**
		 * 对象转换成json字符串
		 * @param obj
		 * @return
		 */
		public static  String  toJsonStr(Object obj){
			if(obj==null){
				return "{}";
			}
			String resultJson="";
			try {
				resultJson =mapper.writeValueAsString(obj);
				}catch (Exception e) {
				    log.warn("json转换出错",e);
					resultJson="{}";
				}
			return resultJson;
		}

		public static void toJsonStream(OutputStream out, Object obj){
			try {
				mapper.writeValue(out,obj);
			} catch (IOException e) {
				log.warn("json转换出错",e);
			}
		}

	/**
	 * 对象转换成json文件
	 * @param obj 数据
	 * @param path 文件路径
	 * @return
	 */
	public static  void  toJsonFile(Object obj,String path){
		if(obj==null){
			obj= "{}";
		}
		try {
			mapper.writeValue(new File(path),obj);
		}catch (Exception e) {
            log.warn("write to file fail",e);
		}
	}

		 /**
			 * Json转换成Object字符串
			 * @param jsonstr
			 * @return
			 */
		public static <T> T toObject(String jsonstr,Class<T> c) {
			T t;
			try {
				t = c.newInstance();
			} catch (Exception e1) {
				return null;
			}
			if(jsonstr==null){
				return t;
			}
			try {
					return mapper.readValue(jsonstr, c);
				}catch (Exception e) {
					return t;
				}
		}
		
		public static <T> List<T> toList(String jsonstr,Class<T> c) {
			T t;
			List<T> lst = new ArrayList<T>();
			try {
				t = c.newInstance();
			} catch (Exception e1) {
				return lst;
			}
			lst.add(t);
			if(jsonstr==null){
				return lst;
			}
			try {
				JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, c);
				return mapper.readValue(jsonstr, type);
                //return mapper.readValue(jsonstr, new TypeReference<List<T>>(){});
			}catch (Exception e) {
					return lst;
			}
		}

		/*对象转换*/
        public static <T> T mappTo(Object source,Class<T> c){
            String resultJson=toJsonStr(source);
            return toObject(resultJson,c);
        }
}
