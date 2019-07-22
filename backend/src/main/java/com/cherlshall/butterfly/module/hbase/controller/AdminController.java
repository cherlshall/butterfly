package com.cherlshall.butterfly.module.hbase.controller;

import com.cherlshall.butterfly.common.vo.ResponseVO;
import com.cherlshall.butterfly.module.hbase.entity.FamilyChange;
import com.cherlshall.butterfly.module.hbase.entity.HTableDetail;
import com.cherlshall.butterfly.module.hbase.entity.HTableSimple;
import com.cherlshall.butterfly.module.hbase.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hbase/admin")
public class AdminController {

    @Autowired
    AdminService service;

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

    @GetMapping("/family/{tableName}")
    public ResponseVO<List<String>> listFamily(@PathVariable("tableName") String tableName) {
        return service.listFamily(tableName);
    }

    @PostMapping("/family/{tableName}")
    public ResponseVO<int[]> changeFamily(@PathVariable("tableName") String tableName,
                                         @RequestBody FamilyChange change) {
        int[] counts = new int[2];
        counts[0] = service.addFamily(tableName, change.getAddition().toArray(new String[0]));
        counts[1] = service.deleteFamily(tableName, change.getRemove().toArray(new String[0]));
        return ResponseVO.ofSuccess(counts);
    }
}
