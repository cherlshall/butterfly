package com.cherlshall.butterfly.user.controller;

import com.cherlshall.butterfly.common.CookieUtil;
import com.cherlshall.butterfly.common.vo.Code;
import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/notices")
    public R userNotices(HttpServletRequest request) {
        Integer uid = CookieUtil.getUid(request.getCookies());
        return R.ok(service.getNotices(uid));
    }

    @GetMapping("/currentUser")
    public R currentUser(HttpServletRequest request) {
        Integer uid = CookieUtil.getUid(request.getCookies());
        return R.ok(service.getCurrentUser(uid));
    }
}
