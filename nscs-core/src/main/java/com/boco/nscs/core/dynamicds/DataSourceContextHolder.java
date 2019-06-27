package com.boco.nscs.core.dynamicds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * datasource的上下文
 *
 */
public class DataSourceContextHolder {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(DataSourceContextHolder.class);
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    /**
     * 默认数据源
     */
    public static final String DEFAULT_DS = "nscs";

    /**
     * 设置数据源类型
     *
     * @param dataSourceType 数据库类型
     */
    public static void setDataSourceType(String dataSourceType) {
//        logger.debug("切换到{}数据源",dataSourceType);
        contextHolder.set(dataSourceType);
    }

    /**
     * 获取数据源类型
     */
    public static String getDataSourceType() {
        return contextHolder.get();
    }

    /**
     * 清除数据源类型
     */
    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}
