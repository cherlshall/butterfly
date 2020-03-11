package com.cherlshall.butterfly.user.controller;

import com.cherlshall.butterfly.common.CookieUtil;
import com.cherlshall.butterfly.common.vo.Code;
import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.user.entity.UserLogin;
import com.cherlshall.butterfly.user.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    public R logout(HttpServletRequest request) {
        Integer uid = CookieUtil.getUid(request.getCookies());
        boolean logout = service.logout(uid);
        if (logout) {
            return R.ok("logout success");
        }
        return R.error("logout failure");
    }

    @PostMapping("upload")
    public R upload(@RequestParam MultipartFile file) {
        return R.ok();
    }

}
