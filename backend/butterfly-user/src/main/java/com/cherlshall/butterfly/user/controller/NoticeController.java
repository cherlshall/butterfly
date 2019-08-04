package com.cherlshall.butterfly.user.controller;

import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.user.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @RequestMapping("/notices")
    public R getUserNotices(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object id = session.getAttribute("id");
        return R.ok(noticeService.getNotices((int) id));
    }
}
