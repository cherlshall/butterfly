package com.cherlshall.butterfly.ui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by htf on 2020/9/25.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private BrowserInterceptor browserInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(browserInterceptor).excludePathPatterns("/", "/index.html");
    }
}
