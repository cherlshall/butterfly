package com.cherlshall.butterfly.module.elasticsearch.controller;

import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.module.elasticsearch.service.EsClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/es/cluster")
public class EsClusterController {

    @Autowired
    private EsClusterService service;

    @GetMapping("/health")
    public R health() {
        return R.ok(service.health());
    }
}
