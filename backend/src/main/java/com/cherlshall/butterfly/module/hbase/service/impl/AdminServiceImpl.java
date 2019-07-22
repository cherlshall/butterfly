package com.cherlshall.butterfly.module.hbase.service.impl;

import com.cherlshall.butterfly.module.hbase.service.AdminService;
import com.cherlshall.butterfly.module.hbase.dao.AdminDao;
import com.cherlshall.butterfly.common.vo.ResponseVO;
import com.cherlshall.butterfly.module.hbase.entity.HTableDetail;
import com.cherlshall.butterfly.module.hbase.util.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminDao dao;
    @Autowired
    Check check;

    @Override
    public ResponseVO<Void> create(String tableName, String... families) {
        if (families.length == 0) {
            return ResponseVO.ofFailure("Table should have at least one column family");
        }
        check.checkNotExist(tableName);
        boolean create = dao.create(tableName, families);
        if (create) {
            return ResponseVO.ofSuccess(null);
        } else {
            return ResponseVO.ofFailure("server error");
        }
    }

    @Override
    public ResponseVO<Void> delete(String tableName) {
        check.checkExist(tableName);
        boolean delete = dao.delete(tableName);
        if (delete) {
            return ResponseVO.ofSuccess(null);
        } else {
            return ResponseVO.ofFailure("delete failure");
        }
    }

    @Override
    public ResponseVO<String[]> list() {
        return ResponseVO.ofSuccess(dao.list());
    }

    @Override
    public ResponseVO<List<HTableDetail>> detail() {
        List<HTableDetail> detail = dao.detail();
        if (detail == null) {
            return ResponseVO.ofFailure("server error");
        }
        return ResponseVO.ofSuccess(detail);
    }

    @Override
    public ResponseVO<Void> disable(String tableName) {
        check.checkExist(tableName);
        check.checkEnable(tableName);
        boolean disable = dao.disable(tableName);
        if (!disable) {
            return ResponseVO.ofFailure("server error");
        }
        return ResponseVO.ofSuccess(null);
    }

    @Override
    public ResponseVO<Void> enable(String tableName) {
        check.checkExist(tableName);
        check.checkDisable(tableName);
        boolean enable = dao.enable(tableName);
        if (!enable) {
            return ResponseVO.ofFailure("server error");
        }
        return ResponseVO.ofSuccess(null);
    }

    @Override
    public ResponseVO<List<String>> listFamily(String tableName) {
        check.checkExist(tableName);
        return ResponseVO.ofSuccess(dao.listFamily(tableName));
    }

    @Override
    public int addFamily(String tableName, String... family) {
        check.checkExist(tableName);
        return dao.addFamily(tableName, family);
    }

    @Override
    public int deleteFamily(String tableName, String... family) {
        check.checkExist(tableName);
        return dao.deleteFamily(tableName, family);
    }
}
