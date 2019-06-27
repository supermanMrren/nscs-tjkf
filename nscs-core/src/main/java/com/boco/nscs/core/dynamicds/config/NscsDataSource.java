package com.boco.nscs.core.dynamicds.config;

import com.boco.nscs.core.dynamicds.DataSourceContextHolder;
import com.boco.nscs.core.dynamicds.DynamicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CC on 2017/9/13.
 * 模版
 */
//@Configuration
public class NscsDataSource {

    /**
     * 动态数据源: 通过AOP在不同数据源之间动态切换
     * @return
     */
    @Bean(name = "dynamicDS")
    public DataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(dataSource1());

        // 配置多数据源
        Map<Object, Object> dsMap = new HashMap(5);
        //数据源1
        dsMap.put( DataSourceContextHolder.DEFAULT_DS, dataSource1());
        //数据源2
//        dsMap.put("ds2", dataSource2());

        dynamicDataSource.setTargetDataSources(dsMap);

        return dynamicDataSource;
    }

    @Bean(name = "defaultDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sys") // application.properteis中对应属性的前缀
    public DataSource dataSource1() {
        return DataSourceBuilder.create().build();
    }
}
