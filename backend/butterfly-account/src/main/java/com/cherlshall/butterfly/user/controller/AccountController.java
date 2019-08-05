package com.cherlshall.butterfly.user.controller;

import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.user.entity.UserLogin;
import com.cherlshall.butterfly.user.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService service;

    @PostMapping("/login")
    public R login(@RequestBody UserLogin user) {
        Map<String, Object> result = service.login(user.getUserName(), user.getPassword());
        return R.ok(result);
    }

    @PostMapping("/logout")
    public R logout(@RequestParam("uid") int uid) {
        boolean logout = service.logout(uid);
        if (logout) {
            return R.ok("logout success");
        }
        return R.error("logout failure");
    }
}
