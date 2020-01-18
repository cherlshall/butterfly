package com.cherlshall.butterfly.elasticsearch.controller;

import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.elasticsearch.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/es/cluster")
public class ClusterController {

    @Autowired
    private ClusterService service;

    @GetMapping("/health")
    public R health() {
        return R.ok(service.health());
    }
}
