package com.cherlshall.butterfly.module.basic.controller;

import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.module.basic.entity.UserLogin;
import com.cherlshall.butterfly.module.basic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login/account")
    public Object login(@RequestBody UserLogin user, HttpServletRequest request) {
        Map<String, Object> result = userService.login(user.getUserName(), user.getPassword());
        if ("ok".equals(result.get("status"))) {
            HttpSession session = request.getSession();
            session.setAttribute("id", result.get("id"));
            session.setAttribute("authority", result.get("currentAuthority"));
            result.remove("id");
        }
        return result;
    }

    @RequestMapping("currentAuthority")
    public Map<String, Object> getCurrentAuthority(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> data = new HashMap<>();
        data.put("authority", session.getAttribute("authority"));
        return data;
    }

    @RequestMapping("/logout/account")
    public R logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("id");
        session.removeAttribute("authority");
        return R.ok("logout success");
    }

    @RequestMapping("/currentUser")
    public R currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        int id = (int) session.getAttribute("id");
        return R.ok(userService.getCurrentUser(id));
    }

}
