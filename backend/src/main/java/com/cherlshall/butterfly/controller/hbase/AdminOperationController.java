package com.cherlshall.butterfly.controller.hbase;

import com.cherlshall.butterfly.entity.hbase.FamilyChange;
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

    @GetMapping("/family/{tableName}")
    public ResponseVO<List<String>> listFamily(@PathVariable("tableName") String tableName) {
        return service.listFamily(tableName);
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

    @PostMapping("/family/{tableName}")
    public ResponseVO<int[]> changeFamily(@PathVariable("tableName") String tableName,
                                         @RequestBody FamilyChange change) {
        int count = 0;
        int[] counts = new int[2];
        for (int i = 0; i < change.getAddition().size(); i++) {
            ResponseVO<Void> res = service.addFamily(tableName, change.getAddition().get(i));
            if (res.getSuccess())
                count++;
        }
        counts[0] = count;
        count = 0;
        for (int i = 0; i < change.getRemove().size(); i++) {
            ResponseVO<Void> res = service.deleteFamily(tableName, change.getRemove().get(i));
            if (res.getSuccess())
                count++;
        }
        counts[1] = count;
        return ResponseVO.ofSuccess(counts);
    }
}
