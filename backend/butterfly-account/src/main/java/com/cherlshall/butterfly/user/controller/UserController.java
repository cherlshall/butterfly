package com.cherlshall.butterfly.user.controller;

import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/notices")
    public R userNotices(@RequestParam("uid") int uid) {
        return R.ok(service.getNotices(uid));
    }

    @GetMapping("/currentUser")
    public R currentUser(@RequestParam("uid") int uid) {
        return R.ok(service.getCurrentUser(uid));
    }
}
