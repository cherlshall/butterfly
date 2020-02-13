package com.cherlshall.butterfly.m2.controller;

import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.m2.entity.po.Type;
import com.cherlshall.butterfly.m2.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("m2/type")
public class TypeController {

    @Autowired
    private TypeService service;

    @PostMapping()
    public R create(@RequestBody Type type) {
        type.setId(null);
        type.setActive(1);
        service.insert(type);
        return R.ok();
    }

    @PutMapping("/active/{id}/{active}")
    public R changeActive(@PathVariable("id") Integer id, @PathVariable("active") Integer active) {
        service.changeActive(id, active);
        return R.ok();
    }

    @PutMapping("/order/{id}/{order}")
    public R changeOrder(@PathVariable("id") Integer id, @PathVariable("order") Integer order) {
        service.changeOrder(id, order);
        return R.ok();
    }

    @PutMapping()
    public R update(@RequestBody Type type) {
        type.setActive(null);
        service.update(type);
        return R.ok();
    }

    @GetMapping()
    public R list() {
        return R.ok(service.list());
    }

    @GetMapping("/active")
    public R listActive() {
        return R.ok(service.listActive());
    }

}
