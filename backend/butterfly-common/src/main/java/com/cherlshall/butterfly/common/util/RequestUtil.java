package com.cherlshall.butterfly.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by htf on 2020/9/24.
 */
public class RequestUtil {

    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteHost();
        }
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            ip = ip.split(",")[0];
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
