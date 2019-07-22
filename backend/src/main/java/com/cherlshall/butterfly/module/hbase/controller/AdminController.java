package com.cherlshall.butterfly.module.hbase.controller;

import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.module.hbase.entity.FamilyChange;
import com.cherlshall.butterfly.module.hbase.entity.HTableSimple;
import com.cherlshall.butterfly.module.hbase.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hbase/admin")
public class AdminController {

    @Autowired
    AdminService service;

    @GetMapping("/table")
    public R list() {
        return R.ok(service.list());
    }

    @GetMapping("/detail")
    public R detail() {
        return R.ok(service.detail());
    }

    @PostMapping("/table")
    public R create(@RequestBody HTableSimple hTableSimple) {
        service.create(hTableSimple.getTableName(), hTableSimple.getFamilies().toArray(new String[0]));
        return R.ok();
    }

    @DeleteMapping("/table/{tableName}")
    public R delete(@PathVariable("tableName") String tableName) {
        service.delete(tableName);
        return R.ok();
    }

    @PutMapping("/disable/{tableName}")
    public R disable(@PathVariable("tableName") String tableName) {
        service.disable(tableName);
        return R.ok();
    }

    @PutMapping("/enable/{tableName}")
    public R enable(@PathVariable("tableName") String tableName) {
        service.enable(tableName);
        return R.ok();
    }

    @GetMapping("/family/{tableName}")
    public R listFamily(@PathVariable("tableName") String tableName) {
        return R.ok(service.listFamily(tableName));
    }

    @PostMapping("/family/{tableName}")
    public R changeFamily(@PathVariable("tableName") String tableName,
                                         @RequestBody FamilyChange change) {
        int[] counts = new int[2];
        counts[0] = service.addFamily(tableName, change.getAddition().toArray(new String[0]));
        counts[1] = service.deleteFamily(tableName, change.getRemove().toArray(new String[0]));
        return R.ok(counts);
    }
}
