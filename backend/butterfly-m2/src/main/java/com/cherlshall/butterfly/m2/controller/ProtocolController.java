package com.cherlshall.butterfly.m2.controller;

import com.cherlshall.butterfly.common.validate.Validate;
import com.cherlshall.butterfly.common.vo.R;
import com.cherlshall.butterfly.m2.entity.po.Protocol;
import com.cherlshall.butterfly.m2.entity.vo.ProtocolVO;
import com.cherlshall.butterfly.m2.service.ProtocolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author hu.tengfei
 * @date 2020/1/7
 */
@RestController
@RequestMapping("m2/protocol")
public class ProtocolController {

    @Autowired
    private ProtocolService service;

    @PostMapping()
    public R insert(@RequestBody Protocol protocol) {
        protocol.setActive(1);
        Validate.check(protocol);
        service.insert(protocol);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R delete(@PathVariable(name = "id") Integer id) {
        service.delete(id);
        return R.ok();
    }

    @PutMapping()
    public R update(@RequestBody ProtocolVO protocolVO) {
        service.update(protocolVO);
        return R.ok();
    }

    @GetMapping("/{id}")
    public R findById(@PathVariable("id") Integer id) {
        return R.ok(service.findById(id));
    }

    @GetMapping()
    public R listByPage(ProtocolVO protocolVO) {
        return R.ok(service.listByPage(protocolVO));
    }

    @GetMapping("names/{category}")
    public R listProtocolName(@PathVariable("category") Integer category) {
        return R.ok(service.listProtocolName(category));
    }

    @GetMapping("names/{category}/{protocolId}")
    public R listProtocolName(@PathVariable("category") Integer category, @PathVariable("protocolId") Integer protocolId) {
        return R.ok(service.listProtocolName(category, protocolId));
    }

    @PutMapping("active/{active}/{id}")
    public R changeActive(@PathVariable("active") Integer active, @PathVariable("id") Integer id) {
        service.changeActive(active, id);
        return R.ok();
    }
}
