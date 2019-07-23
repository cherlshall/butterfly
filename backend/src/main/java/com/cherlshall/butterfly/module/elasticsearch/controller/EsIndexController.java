package com.cherlshall.butterfly.module.elasticsearch.controller;

import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.module.elasticsearch.service.EsIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/es/index")
public class EsIndexController {
    @Autowired
    private EsIndexService service;

    @GetMapping
    private R list() {
        return R.ok(service.list());
    }
}
