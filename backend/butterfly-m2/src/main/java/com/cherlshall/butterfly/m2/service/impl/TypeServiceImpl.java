package com.cherlshall.butterfly.m2.service.impl;

import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.m2.dao.TypeDao;
import com.cherlshall.butterfly.m2.entity.po.Type;
import com.cherlshall.butterfly.m2.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeDao dao;

    @Override
    public void insert(Type type) {
        if (dao.findByCnName(type.getCnName()) > 0) {
            throw new ButterflyException("cn name is exists");
        }
        if (dao.findByEnName(type.getEnName()) > 0) {
            throw new ButterflyException("en name is exists");
        }
        Integer maxDisplayOrder = dao.findMaxDisplayOrder();
        if (maxDisplayOrder == null) {
            type.setDisplayOrder(0);
        } else {
            type.setDisplayOrder(maxDisplayOrder + 1);
        }
        if (dao.insert(type) < 1) {
            throw new ButterflyException("create failure");
        }
    }

    @Override
    public void changeActive(Integer id, Integer active) {
        if (dao.changeActive(id, active) < 1) {
            throw new ButterflyException("change active failure");
        }
    }

    @Override
    public void changeOrder(Integer id, Integer displayOrder) {
        if (dao.changeOrder(id, displayOrder) < 1) {
            throw new ButterflyException("change order failure");
        }
    }

    @Override
    public void update(Type type) {
        Type before = dao.findById(type.getId());
        if (before.getCnName().equals(type.getCnName())) {
            type.setCnName(null);
        }
        if (before.getEnName().equals(type.getEnName())) {
            type.setEnName(null);
        }
        if (before.getDisplayOrder().equals(type.getDisplayOrder())) {
            type.setDisplayOrder(null);
        }
        if ("TLV".equals(before.getCnName()) || "结构体".equals(before.getCnName())) {
            if (type.getCnName() != null || type.getEnName() != null) {
                throw new ButterflyException("TLV或结构体不可修改名称");
            }
        }
        if (type.getCnName() != null && dao.findByCnName(type.getCnName()) > 0) {
            throw new ButterflyException("cn name is exists");
        }
        if (type.getEnName() != null && dao.findByEnName(type.getEnName()) > 0) {
            throw new ButterflyException("en name is exists");
        }
        if (dao.update(type) < 1) {
            throw new ButterflyException("update failure");
        }
    }

    @Override
    public List<Type> list() {
        return dao.list();
    }

    @Override
    public List<Type> listActive() {
        return dao.listByActive(1);
    }
}
