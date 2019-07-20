package com.cherlshall.butterfly.controller.hbase;

import com.cherlshall.butterfly.entity.hbase.HBaseBean;
import com.cherlshall.butterfly.entity.hbase.HBaseTable;
import com.cherlshall.butterfly.service.hbase.TableOperationService;
import com.cherlshall.butterfly.util.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hbase/table")
public class TableOperationController {

    @Autowired
    TableOperationService service;

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
