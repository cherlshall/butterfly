package com.cherlshall.butterfly.m2.service.impl;

import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.common.vo.PageData;
import com.cherlshall.butterfly.m2.dao.FieldDao;
import com.cherlshall.butterfly.m2.dao.ProtocolDao;
import com.cherlshall.butterfly.m2.entity.po.Field;
import com.cherlshall.butterfly.m2.entity.po.Protocol;
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
    @Autowired
    private ProtocolDao protocolDao;

    @Override
    public void insert(Field field) {
        FieldVO check = new FieldVO();
        Protocol protocol = protocolDao.findById(field.getProtocolId());
        if (protocol.getCategory() != 3) {
            check.setProtocolId(field.getProtocolId());
            check.setType(field.getType());
            if (dao.count(check) != 0) {
                throw new ButterflyException("Type is already exists");
            }
            check.setType(null);
        } else {
            field.setType(null);
        }
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
        Protocol protocol = protocolDao.findById(fieldVO.getProtocolId());
        FieldVO check = new FieldVO();
        check.setProtocolId(fieldVO.getProtocolId());
        if (protocol.getCategory() != 3) {
            check.setType(fieldVO.getType());
            if (!fieldVO.getType().equals(beforeUpdate.getType()) && dao.count(check) != 0) {
                throw new ButterflyException("Type is already exists");
            }
            check.setType(null);
        } else {
            fieldVO.setType(null);
        }
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
        List<Field> fields = dao.listByPage(fieldVO);
        for (Field field : fields) {
            if (field.getValueType().equals("struct")) {
                field.setSize(dao.sumSizeByProtocolId(field.getLink()));
            }
        }
        return new PageData<>(fields, dao.count(fieldVO));
    }

    @Override
    public void changeActive(Integer active, Integer id) {
        if (dao.updateActive(active, id) == 0) {
            throw new ButterflyException("更新状态失败");
        }
    }
}
