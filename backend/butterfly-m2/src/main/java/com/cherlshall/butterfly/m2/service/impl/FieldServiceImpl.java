package com.cherlshall.butterfly.m2.service.impl;

import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.common.vo.PageData;
import com.cherlshall.butterfly.m2.dao.FieldDao;
import com.cherlshall.butterfly.m2.entity.po.Field;
import com.cherlshall.butterfly.m2.entity.vo.FieldVO;
import com.cherlshall.butterfly.m2.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hu.tengfei
 * @date 2020/1/7
 */
@Service
public class FieldServiceImpl implements FieldService {

    @Autowired
    private FieldDao dao;

    @Override
    public void insert(Field field) {
        FieldVO check = new FieldVO();
        check.setProtocolId(field.getProtocolId());
        check.setType(field.getType());
        if (dao.count(check) != 0) {
            throw new ButterflyException("Type is already exists");
        }
        check.setType(null);
        check.setEnName(field.getEnName());
        if (dao.count(check) != 0) {
            throw new ButterflyException("Name(EN) is already exists");
        }
        if (dao.insert(field) == 0) {
            throw new ButterflyException("插入失败");
        }
    }

    @Override
    public void delete(Integer id) {
        if (dao.delete(id) == 0) {
            throw new ButterflyException("删除失败");
        }
    }

    @Override
    public void update(FieldVO fieldVO) {
        Field beforeUpdate = dao.findById(fieldVO.getId());
        FieldVO check = new FieldVO();
        check.setProtocolId(fieldVO.getProtocolId());
        check.setType(fieldVO.getType());
        if (!fieldVO.getType().equals(beforeUpdate.getType()) && dao.count(check) != 0) {
            throw new ButterflyException("Type is already exists");
        }
        check.setType(null);
        check.setEnName(fieldVO.getEnName());
        if (!fieldVO.getEnName().equals(beforeUpdate.getEnName()) && dao.count(check) != 0) {
            throw new ButterflyException("Name(EN) is already exists");
        }
        if (dao.update(fieldVO) == 0) {
            throw new ButterflyException("更新失败");
        }
    }

    @Override
    public PageData<Field> listByPage(FieldVO fieldVO) {
        return new PageData<>(dao.listByPage(fieldVO), dao.count(fieldVO));
    }
}
