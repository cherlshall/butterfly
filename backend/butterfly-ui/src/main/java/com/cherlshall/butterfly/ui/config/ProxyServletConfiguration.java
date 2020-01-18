package com.cherlshall.butterfly.ui.config;

import com.google.common.collect.ImmutableMap;
import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;
import java.util.Map;

/**
 * @author hu.tengfei
 * @date 2019/11/29
 */
@Configuration
public class ProxyServletConfiguration {

    // 路由设置
    @Value("${proxy.servlet_url}")
    private String servlet_url;

    // 代理目标地址
    @Value("${proxy.target_url}")
    private String target_url;

    @Bean
    public ServletRegistrationBean<Servlet> proxyServletRegistration() {
        ServletRegistrationBean<Servlet> registrationBean =
                new ServletRegistrationBean<>(new ProxyServletWithCookie(), servlet_url);
        // 设置代理参数
        Map<String, String> params = ImmutableMap.of(
                "targetUri", target_url,
                "log", "true");
        registrationBean.setInitParameters(params);
        return registrationBean;
    }

    /**
     * 携带完整cookie的实现
     */
    private static class ProxyServletWithCookie extends ProxyServlet {
        @Override
        protected String getCookieNamePrefix() {
            return "";
        }
    }
}
