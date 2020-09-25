package com.cherlshall.butterfly.common.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by htf on 2020/9/24.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthorityInterceptor authorityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorityInterceptor).excludePathPatterns("/static/*").excludePathPatterns("/error")
                .addPathPatterns("/**");
    }
}
