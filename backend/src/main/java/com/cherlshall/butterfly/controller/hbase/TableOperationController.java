package com.cherlshall.butterfly.controller.hbase;

import com.cherlshall.butterfly.entity.hbase.HBaseTable;
import com.cherlshall.butterfly.service.hbase.TableOperationService;
import com.cherlshall.butterfly.util.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
