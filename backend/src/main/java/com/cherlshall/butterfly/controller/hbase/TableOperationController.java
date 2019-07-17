package com.cherlshall.butterfly.controller.hbase;

import com.cherlshall.butterfly.entity.hbase.HBaseTable;
import com.cherlshall.butterfly.service.hbase.TableOperationService;
import com.cherlshall.butterfly.util.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hbase/table")
public class TableOperationController {

    @Autowired
    TableOperationService service;

    @GetMapping("/findAll")
    public ResponseVO<HBaseTable> findByPage(@RequestParam("tableName") String tableName,
                                             @RequestParam("rowKey") String rowKey,
                                             @RequestParam("pageSize") int pageSize) {
        return service.findByPage(tableName, rowKey, pageSize);
    }
}
