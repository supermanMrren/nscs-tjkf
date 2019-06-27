package com.boco.nscs.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by CC on 2017/6/10.
 * 数据库配置-sms
 */
//@Configuration
//@MapperScan(value = "com.boco.nscs.mapper.sms",sqlSessionTemplateRef = "smsSqlSessionTemplate")
public class SmsDataSourceConfig {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(SmsDataSourceConfig.class);

    @Bean(name = "smsDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sms")
    public DataSource myDataSource() {
        logger.debug("init smsDataSource");
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "smsSqlSessionFactory")
    public SqlSessionFactory mySqlSessionFactory(@Qualifier("smsDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        //解决 用Maven打包情况下 MyBatis无法扫描Spring Boot别名的问题
        bean.setVfs(SpringBootVFS.class);
        bean.setTypeAliasesPackage("com.boco.nscs.entity");
        bean.setTypeHandlersPackage("com.boco.nscs.common.typehandler");
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/sqlmaps/sms/*.xml"));
        //设置 jdbctype默认为空 oracle需要
        bean.getObject().getConfiguration().setJdbcTypeForNull(JdbcType.VARCHAR);

        //分页插件 针对多数据源 需要取消配置文件中对应配置 在每个数据源中配置
        PageInterceptor pageHelper = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "informix");
        properties.setProperty("countColumn", "*");
        properties.setProperty("params", "pageNum=pageIndex;pageSize=pageSize;");
        properties.setProperty("supportMethodsArguments", "true");
        pageHelper.setProperties(properties);
        bean.setPlugins(new Interceptor[]{pageHelper});
        return bean.getObject();
    }

    @Bean(name = "smsTransactionManager")
    public DataSourceTransactionManager myTransactionManager(@Qualifier("smsDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "smsSqlSessionTemplate")
    public SqlSessionTemplate mySqlSessionTemplate(@Qualifier("smsSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
