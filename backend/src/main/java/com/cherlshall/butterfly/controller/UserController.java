package com.cherlshall.butterfly.controller;

import com.cherlshall.butterfly.entity.UserDetail;
import com.cherlshall.butterfly.entity.UserLogin;
import com.cherlshall.butterfly.service.UserService;
import com.cherlshall.butterfly.util.vo.ResponseVO;
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
        ResponseVO<Map> vo = userService.login(user.getUserName(), user.getPassword());
        Map result = vo.getData();
        if (vo.getSuccess()) {
            HttpSession session = request.getSession();
            session.setAttribute("id", result.get("id"));
            session.setAttribute("authority", result.get("currentAuthority"));
            result.remove("id");
        }
        return result;
    }

    @RequestMapping("currentAuthority")
    public Map getCurrentAuthority(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> data = new HashMap<>();
        data.put("authority", session.getAttribute("authority"));
        return data;
    }

    @RequestMapping("/logout/account")
    public ResponseVO logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("id");
        session.removeAttribute("authority");
        return ResponseVO.ofSuccess("退出成功");
    }

    @RequestMapping("/currentUser")
    public ResponseVO<UserDetail> currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        int id = (int) session.getAttribute("id");
        return userService.getCurrentUser(id);
    }

}
