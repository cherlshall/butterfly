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

    @GetMapping("/findByPage/{tableName}")
    public ResponseVO<HBaseTable> findByPage(@PathVariable("tableName") String tableName,
                                             @RequestParam("rowKey") String rowKey,
                                             @RequestParam("pageSize") int pageSize) {
        return service.findByPage(tableName, rowKey, pageSize);
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
}
