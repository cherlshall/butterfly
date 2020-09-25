package com.cherlshall.butterfly.ui.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 适配前端 browser 路由
 * Created by htf on 2020/9/25.
 */
@Component
public class BrowserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!request.getRequestURI().contains(".")) {
            request.getRequestDispatcher("/").forward(request, response);
        }
        return true;
    }
}
