package com.cherlshall.butterfly.hdfs.controller;

import com.cherlshall.butterfly.common.vo.PageParam;
import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.hdfs.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hdfs/content")
public class ContentController {

    @Autowired
    private ContentService service;

    @GetMapping
    public R read(String path) {
        return R.ok(service.read(path));
    }

    @GetMapping("/table")
    public R readToTable(@RequestParam("path") String path,
                         @RequestParam("currentPage") int currentPage,
                         @RequestParam("pageSize") int pageSize) {
        PageParam params = new PageParam();
        params.setCurrentPage(currentPage);
        params.setPageSize(pageSize);
        return R.ok(service.readToTable(path, params));
    }

    @GetMapping("/json")
    public R readToJson(@RequestParam("path") String path,
                        @RequestParam("currentPage") int currentPage,
                        @RequestParam("pageSize") int pageSize) {
        PageParam params = new PageParam();
        params.setCurrentPage(currentPage);
        params.setPageSize(pageSize);
        return R.ok(service.readToJson(path, params));
    }
}
