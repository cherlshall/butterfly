package com.cherlshall.butterfly.m2.controller;

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
}
