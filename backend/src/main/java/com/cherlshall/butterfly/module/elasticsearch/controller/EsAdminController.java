package com.cherlshall.butterfly.module.elasticsearch.controller;

import com.cherlshall.butterfly.module.elasticsearch.service.EsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/es/admin")
public class EsAdminController {
    @Autowired
    private EsAdminService service;
}
