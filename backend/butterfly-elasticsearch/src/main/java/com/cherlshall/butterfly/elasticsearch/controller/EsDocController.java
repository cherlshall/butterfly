package com.cherlshall.butterfly.elasticsearch.controller;

import com.cherlshall.butterfly.common.vo.ParamsVO;
import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.elasticsearch.service.EsDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/es/doc")
public class EsDocController {

    @Autowired
    private EsDocService service;

    @PostMapping("/{indexName}")
    public R insert(@PathVariable("indexName") String indexName, @RequestBody Map<String, Object> data) {
        int insert = service.insert(indexName, data);
        if (insert > 0) {
            return R.ok();
        }
        return R.error("insert failure");
    }

    @DeleteMapping("/{indexName}/{id}")
    public R delete(@PathVariable("indexName") String indexName, @PathVariable("id") String id) {
        int insert = service.delete(indexName, id);
        if (insert > 0) {
            return R.ok();
        }
        return R.error("delete failure");
    }

    @GetMapping("/{indexName}")
    public R select(@PathVariable("indexName") String indexName, ParamsVO params) {
        return R.ok(service.selectByPage(indexName, params));
    }
}
