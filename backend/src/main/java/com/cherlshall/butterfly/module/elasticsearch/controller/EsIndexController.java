package com.cherlshall.butterfly.module.elasticsearch.controller;

import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.module.elasticsearch.entity.IndexCreateInfo;
import com.cherlshall.butterfly.module.elasticsearch.service.EsIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/es/index")
public class EsIndexController {
    @Autowired
    private EsIndexService service;

    @GetMapping
    public R list() {
        return R.ok(service.list());
    }

    @PostMapping()
    public R create(@RequestBody IndexCreateInfo indexCreateInfo) {
        service.create(indexCreateInfo.getIndexName(),
                indexCreateInfo.getSettings(),
                indexCreateInfo.getProperties());
        return R.ok();
    }

    @DeleteMapping("/{indexName}")
    public R delete(@PathVariable("indexName") String indexName) {
        service.delete(indexName);
        return R.ok();
    }
}
