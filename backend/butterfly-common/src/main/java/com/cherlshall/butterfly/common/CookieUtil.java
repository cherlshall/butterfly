package com.cherlshall.butterfly.common;

import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.common.vo.Code;

import javax.servlet.http.Cookie;
import java.util.*;

public class CookieUtil {

    public static Map<String, List<String>> toMap(Cookie[] cookies) {
        Map<String, List<String>> cookieMap = new HashMap<>();
        if (cookies == null) {
            return cookieMap;
        }
        for (Cookie cookie : cookies) {
            cookieMap.computeIfAbsent(cookie.getName(), k -> new ArrayList<>()).add(cookie.getValue());
        }
        return cookieMap;
    }

    public static String getFirstValue(Cookie[] cookies, String name) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static Integer getUid(Cookie[] cookies) {
        String uidStr = getFirstValue(cookies, "uid");
        if (uidStr == null) {
            throw new ButterflyException(Code.BAD_REQUEST);
        }
        int uid;
        try {
            uid = Integer.parseInt(uidStr);
        } catch (NumberFormatException e) {
            throw new ButterflyException(Code.BAD_REQUEST);
        }
        return uid;
    }
}
