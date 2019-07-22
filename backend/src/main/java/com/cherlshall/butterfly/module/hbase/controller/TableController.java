package com.cherlshall.butterfly.module.hbase.controller;

import com.cherlshall.butterfly.common.vo.ResponseVO;
import com.cherlshall.butterfly.module.hbase.entity.HBaseBean;
import com.cherlshall.butterfly.module.hbase.entity.HBaseTable;
import com.cherlshall.butterfly.module.hbase.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hbase/table")
public class TableController {

    @Autowired
    TableService service;

    @GetMapping("/row/{tableName}")
    public ResponseVO<HBaseTable> findByPage(@PathVariable("tableName") String tableName,
                                             @RequestParam("rowKey") String rowKey,
                                             @RequestParam("pageSize") int pageSize,
                                             @RequestParam("removeFirst") boolean removeFirst) {
        return service.findByPage(tableName, rowKey, pageSize, removeFirst);
    }

    @GetMapping("/row/{tableName}/{rowKey}")
    public ResponseVO<HBaseTable> findByRowKey(@PathVariable("tableName") String tableName,
                                             @PathVariable("rowKey") String rowKey) {
        return service.findByRowKey(tableName, rowKey);
    }

    @PostMapping("/row/{tableName}/{rowKey}")
    public ResponseVO<Integer> insertRow(@PathVariable("tableName") String tableName,
                                         @PathVariable("rowKey") String rowKey,
                                         @RequestBody List<HBaseBean> beans) {
        return service.insertRow(tableName, rowKey, beans);
    }

    @DeleteMapping("/row/{tableName}/{rowKey}")
    public ResponseVO<Void> deleteRow(@PathVariable("tableName") String tableName,
                                      @PathVariable("rowKey") String rowKey) {
        return service.deleteRow(tableName, rowKey);
    }

    @DeleteMapping("/col/{tableName}/{rowKey}")
    public ResponseVO<Void> deleteCol(@PathVariable("tableName") String tableName,
                                      @PathVariable("rowKey") String rowKey,
                                      @RequestBody HBaseBean bean) {
        return service.deleteCol(tableName, rowKey, bean.getFamily(), bean.getQualifier());
    }
}
