package com.cherlshall.butterfly.ui.config;

import com.google.common.collect.ImmutableMap;
import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by htf on 2019/11/29.
 */
@Configuration
public class ProxyServletConfiguration {

    // 路由设置
    @Value("${proxy.servlet-url}")
    private String servletUrl;

    // 代理目标地址
    @Value("${proxy.target-url}")
    private String targetUrl;

    @Bean
    public ServletRegistrationBean<Servlet> proxyServletRegistration() {
        ServletRegistrationBean<Servlet> registrationBean =
                new ServletRegistrationBean<>(new ProxyServletWithCookie(), servletUrl);
        // 设置代理参数
        Map<String, String> params = ImmutableMap.of(
                "targetUri", targetUrl,
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

        @Override
        protected String rewriteUrlFromRequest(HttpServletRequest servletRequest) {
            StringBuilder uri = new StringBuilder(500);
            uri.append(this.getTargetUri(servletRequest));
            if (servletRequest.getRequestURI() != null) {
                String servletPath = servletRequest.getServletPath();
                if (servletPath != null && !servletPath.isEmpty()) {
                    uri.append(servletPath);
                } else {
                    uri.append(servletRequest.getRequestURI());
                }
            }

            String queryString = servletRequest.getQueryString();
            String fragment = null;
            if (queryString != null) {
                int fragIdx = queryString.indexOf(35);
                if (fragIdx >= 0) {
                    fragment = queryString.substring(fragIdx + 1);
                    queryString = queryString.substring(0, fragIdx);
                }
            }

            queryString = this.rewriteQueryStringFromRequest(servletRequest, queryString);
            if (queryString != null && !queryString.isEmpty()) {
                uri.append('?').append(queryString);
            }

            if (this.doSendUrlFragment && fragment != null) {
                uri.append("#").append(fragment);
            }

            return uri.toString();
        }
    }
}
