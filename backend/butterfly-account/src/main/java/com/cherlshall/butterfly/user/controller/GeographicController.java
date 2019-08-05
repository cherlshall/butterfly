package com.cherlshall.butterfly.user.controller;

import com.cherlshall.butterfly.user.cache.GeographicConfig;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/geographic")
public class GeographicController {

    @RequestMapping("/province")
    public String getProvince() {
        return GeographicConfig.PROVINCE.toJSONString();
    }

    @RequestMapping("/city/{provinceId}")
    public String getCity(@PathVariable(name = "provinceId") String provinceId) {
        return GeographicConfig.CITY.getJSONArray(provinceId).toJSONString();
    }
}
