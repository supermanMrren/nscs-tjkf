package com.boco.nscs;

import com.boco.nscs.core.interceptor.AccessLogInterceptor;
import com.boco.nscs.core.interceptor.RestAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by CC on 2017/6/13.
 * web配置
 */
@Configuration
public class NscsWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //日志记录拦截器
        registry.addInterceptor(new AccessLogInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/images/**")
                .excludePathPatterns("/assets/**")
        ;
//        registry.addInterceptor(getAuthInterceptor()).addPathPatterns("/api/**");
    }
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry){
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")//设置允许跨域的路径
                .allowedOrigins("*")//设置允许跨域请求的域名
                .allowCredentials(true)//是否允许证书 不再默认开启
                .allowedMethods("GET", "POST", "PUT", "DELETE")//设置允许的方法
                .maxAge(3600);//跨域允许时间
    }
}
