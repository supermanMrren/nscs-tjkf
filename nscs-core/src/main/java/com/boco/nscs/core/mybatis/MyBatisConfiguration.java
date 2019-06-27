package com.boco.nscs.core.mybatis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "mybatis", name = "sqllog-interceptor-on",havingValue="true")
public class MyBatisConfiguration {
    @Bean
    SqlLogInterceptor sqlLogInterceptor() {
        return new SqlLogInterceptor();
    }
}
