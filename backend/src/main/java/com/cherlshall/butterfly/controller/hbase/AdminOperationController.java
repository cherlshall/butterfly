package com.cherlshall.butterfly.controller.hbase;

import com.cherlshall.butterfly.entity.hbase.HTableDetail;
import com.cherlshall.butterfly.entity.hbase.HTableSimple;
import com.cherlshall.butterfly.service.hbase.AdminOperationService;
import com.cherlshall.butterfly.util.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hbase/admin")
public class AdminOperationController {

    @Autowired
    AdminOperationService service;

    @GetMapping("/table")
    public ResponseVO<String[]> list() {
        return service.list();
    }

    @GetMapping("/detail")
    public ResponseVO<List<HTableDetail>> detail() {
        return service.detail();
    }

    @PostMapping("/table")
    public ResponseVO<Void> create(@RequestBody HTableSimple hTableSimple) {
        return service.create(hTableSimple.getTableName(), hTableSimple.getFamilies().toArray(new String[0]));
    }

    @DeleteMapping("/table/{tableName}")
    public ResponseVO<Void> delete(@PathVariable("tableName") String tableName) {
        return service.delete(tableName);
    }

    @PutMapping("/disable/{tableName}")
    public ResponseVO<Void> disable(@PathVariable("tableName") String tableName) {
        return service.disable(tableName);
    }

    @PutMapping("/enable/{tableName}")
    public ResponseVO<Void> enable(@PathVariable("tableName") String tableName) {
        return service.enable(tableName);
    }

    @PostMapping("/family/{tableName}/{family}")
    public ResponseVO<Void> addFamily(@PathVariable("tableName") String tableName,
                                      @PathVariable("family") String family) {
        return service.addFamily(tableName, family);
    }

    @DeleteMapping("/family/{tableName}/{family}")
    public ResponseVO<Void> deleteFamily(@PathVariable("tableName") String tableName,
                                         @PathVariable("family") String family) {
        return service.deleteFamily(tableName, family);
    }
}
