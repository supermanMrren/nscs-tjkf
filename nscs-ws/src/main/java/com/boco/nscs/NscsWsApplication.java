package com.boco.nscs;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@MapperScan("com.boco.nscs.mapper")
public class NscsWsApplication extends SpringBootServletInitializer {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(NscsWsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(NscsWsApplication.class, args);
        logger.info("ws website start success!");
    }

    //extend SpringBootServletInitializer 支持war方式部署
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(NscsWsApplication.class);
    }
}
