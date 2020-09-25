package com.cherlshall.butterfly.user.controller;

import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.user.entity.UserLogin;
import com.cherlshall.butterfly.user.service.AccountService;
import com.cherlshall.butterfly.util.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService service;

    @PostMapping("/login")
    public R login(@RequestBody UserLogin user) {
        return R.ok(service.login(user.getUserName(), user.getPassword()));
    }

    @PostMapping("/logout")
    public R logout(@CookieValue String token) {
        boolean logout = service.logout(token);
        if (logout) {
            return R.ok("logout success");
        }
        return R.error("logout failure");
    }

    @GetMapping
    public UserInfo userInfo(String token) {
        return service.getUserInfoByToken(token);
    }

    @PostMapping("upload")
    public R upload(@RequestParam MultipartFile file) {
        return R.ok();
    }

}
