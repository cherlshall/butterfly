package com.cherlshall.butterfly.controller.user;

import com.cherlshall.butterfly.service.user.NoticeService;
import com.cherlshall.butterfly.util.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    @RequestMapping("/notices")
    public ResponseVO<List> getUserNotices(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object id = session.getAttribute("id");
        return noticeService.getNotices((int) id);
    }
}
