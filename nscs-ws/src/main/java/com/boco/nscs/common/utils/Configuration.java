package com.boco.nscs.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Properties;

public class Configuration {
	private  static Logger log = LoggerFactory.getLogger(Configuration.class);
	private Properties properties;
	private static  String configFile = "/config.properties";
	private  static Configuration cf = new Configuration();

	private Configuration() {
		properties = new Properties();
		InputStream input = null;
		try {
		    log.debug("use configFile: {}", configFile);
			input = this.getClass().getResourceAsStream(configFile);
			properties.load(new InputStreamReader(input, "utf-8"));
		} catch (IOException e) {
		    log.warn("load   configFile fail",e);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				log.warn("Can't   close   the   InputStream",e);
			}
		}
	}


	public static Configuration getInstance() {
		return cf;
	}


	public Properties getProperties() {
		return this.properties;
	}


	public String getValue(String key) {
		String str = "";
		try {
			str = properties.getProperty(key);
		} catch (Exception e) {
		    log.warn("getValue   error",e);
		}
		if (str == null) {
			str = "";
		}
		return str;
	}


	public Iterator getAllKey(){
        return properties.entrySet().iterator();
	}
}
