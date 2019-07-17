package com.cherlshall.butterfly.controller.hbase;

import com.cherlshall.butterfly.service.hbase.AdminOperationService;
import com.cherlshall.butterfly.util.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hbase/admin")
public class AdminOperationController {

    @Autowired
    AdminOperationService service;

    @GetMapping("/list")
    public ResponseVO<String[]> list() {
        return service.list();
    }
}
