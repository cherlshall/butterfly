package com.cherlshall.butterfly.hdfs.controller;

import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.hdfs.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hdfs/file")
public class FileController {

    @Autowired
    private FileService service;

    @GetMapping("/names")
    public R listFileName(String parent) {
        return R.ok(service.listFileName(parent));
    }

    @GetMapping("/detail")
    public R listFileDetail(String parent) {
        return R.ok(service.listFileDetail(parent));
    }

    @PostMapping("/file")
    public R create(String path) {
        boolean create = service.create(path);
        if (create) {
            return R.ok();
        } else {
            return R.error("file is already exists");
        }
    }

    @DeleteMapping
    public R delete(String path) {
        boolean delete = service.delete(path);
        if (delete) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @PostMapping("/dir")
    public R mkdirs(String path) {
        boolean mkdirs = service.mkdirs(path);
        if (mkdirs) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}
