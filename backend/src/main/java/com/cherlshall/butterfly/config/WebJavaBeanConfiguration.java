package com.cherlshall.butterfly.config;

import com.cherlshall.butterfly.config.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebJavaBeanConfiguration {

    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 实例化WebMvcConfigurer接口
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * 添加拦截器
             */
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(loginInterceptor)
                        .addPathPatterns("/user/currentUser")
                        .addPathPatterns("/notice/notices");
//                        .addPathPatterns("/hbase/**");
            }
        };
    }
}
