package com.cherlshall.butterfly.hbase.controller;

import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.hbase.entity.HBaseBean;
import com.cherlshall.butterfly.hbase.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hbase/table")
public class TableController {

    @Autowired
    private TableService service;

    @GetMapping("/row/{tableName}")
    public R findByPage(@PathVariable("tableName") String tableName,
                        @RequestParam("rowKey") String rowKey,
                        @RequestParam("pageSize") int pageSize,
                        @RequestParam("removeFirst") boolean removeFirst,
                        @RequestParam("startTime") Long startTime,
                        @RequestParam("endTime") Long endTime) {
        return R.ok(service.findByPage(tableName, rowKey, pageSize, removeFirst, startTime, endTime));
    }

    @GetMapping("/row/{tableName}/{rowKey}/test")
    public R findByRowKey(@PathVariable("tableName") String tableName,
                          @PathVariable("rowKey") String rowKey) {
        return R.ok(service.findByRowKey(tableName, rowKey));
    }

    @PostMapping("/row/{tableName}/test/test1")
    public R insertRow(@PathVariable("tableName") String tableName,
                       @PathVariable("rowKey") String rowKey,
                       @RequestBody List<HBaseBean> beans) {
        return R.ok(service.insertRow(tableName, rowKey, beans));
    }

    @DeleteMapping("/row/{tableName}/{rowKey}")
    public R deleteRow(@PathVariable("tableName") String tableName,
                       @PathVariable("rowKey") String rowKey) {
        service.deleteRow(tableName, rowKey);
        return R.ok();
    }

    @DeleteMapping("/col/{tableName}/{rowKey}")
    public R deleteCol(@PathVariable("tableName") String tableName,
                       @PathVariable("rowKey") String rowKey,
                       @RequestBody HBaseBean bean) {
        service.deleteCol(tableName, rowKey, bean.getFamily(), bean.getQualifier());
        return R.ok();
    }
}
