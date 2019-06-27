package com.boco.nscs.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by CC on 2017/6/10.
 * 主数据库配置
 */
@Configuration
@MapperScan(value = "com.boco.nscs.mapper.*",sqlSessionTemplateRef = "sysSqlSessionTemplate")
public class SysDataSourceConfig {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(SysDataSourceConfig.class);
    @Bean(name = "sysDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sys")
    @Primary
    public DataSource myDataSource() {
        logger.debug("init sysDataSource");
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "sysSqlSessionFactory")
    @Primary
    public SqlSessionFactory mySqlSessionFactory(@Qualifier("sysDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        //解决 用Maven打包情况下 MyBatis无法扫描Spring Boot别名的问题
        bean.setVfs(SpringBootVFS.class);
        bean.setTypeAliasesPackage("com.boco.nscs.entity");
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/sqlmaps/*/*.xml"));

        //分页插件 针对多数据源 需要取消配置文件中对应配置 在每个数据源中配置
        PageInterceptor pageHelper = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "oracle");
        properties.setProperty("countColumn", "*");
        properties.setProperty("params", "pageNum=pageIndex;pageSize=pageSize;");
        properties.setProperty("supportMethodsArguments", "true");
        pageHelper.setProperties(properties);
        bean.setPlugins(new Interceptor[]{pageHelper});

        //多数据源下需要自己配置 拦截器
//        logger.debug("register SqlLogInterceptor");
//        SqlLogInterceptor sqlLogInterceptor = new SqlLogInterceptor();
//        bean.setPlugins(new Interceptor[]{sqlLogInterceptor});
        return bean.getObject();
    }

    @Bean(name = "sysTransactionManager")
    @Primary
    public DataSourceTransactionManager myTransactionManager(@Qualifier("sysDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sysSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate mySqlSessionTemplate(@Qualifier("sysSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
