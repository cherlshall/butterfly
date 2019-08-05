package com.cherlshall.butterfly.user.controller;

import com.cherlshall.butterfly.user.cache.FakeConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fake")
public class FakeController {

    @RequestMapping("/chartData")
    public String chartData() {
        return FakeConfig.CHART_DATA;
    }

    @RequestMapping("/tags")
    public String tags() {
        return FakeConfig.TAGS;
    }

    @RequestMapping("/list")
    public String list() {
        return FakeConfig.LIST;
    }

    @RequestMapping("/project/notice")
    public String notice() {
        return FakeConfig.NOTICE;
    }
}
