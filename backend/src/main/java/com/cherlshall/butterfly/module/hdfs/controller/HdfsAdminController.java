package com.cherlshall.butterfly.module.hdfs.controller;

import com.cherlshall.butterfly.module.hdfs.service.HdfsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hdfs/admin")
public class HdfsAdminController {
    @Autowired
    private HdfsAdminService service;
}
