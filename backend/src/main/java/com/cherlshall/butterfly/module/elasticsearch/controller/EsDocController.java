package com.cherlshall.butterfly.module.elasticsearch.controller;

import com.cherlshall.butterfly.module.elasticsearch.service.EsDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/es/doc")
public class EsDocController {

    @Autowired
    private EsDocService service;
}
