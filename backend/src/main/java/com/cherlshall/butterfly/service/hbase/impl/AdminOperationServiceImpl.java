package com.cherlshall.butterfly.service.hbase.impl;

import com.cherlshall.butterfly.dao.hbase.AdminOperationDao;
import com.cherlshall.butterfly.entity.hbase.HTableDetail;
import com.cherlshall.butterfly.service.hbase.AdminOperationService;
import com.cherlshall.butterfly.util.hbase.Check;
import com.cherlshall.butterfly.util.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminOperationServiceImpl implements AdminOperationService {

    @Autowired
    AdminOperationDao dao;
    @Autowired
    Check check;

    @Override
    public ResponseVO<Void> create(String tableName, String... families) {
        if (families.length == 0) {
            return ResponseVO.ofFailure("Table should have at least one column family");
        }
        String checkNotExist = check.checkNotExist(tableName);
        if (checkNotExist != null) {
            return ResponseVO.ofFailure(checkNotExist);
        }
        boolean create = dao.create(tableName, families);
        if (create) {
            return ResponseVO.ofSuccess(null);
        } else {
            return ResponseVO.ofFailure("server error");
        }
    }

    @Override
    public ResponseVO<Void> delete(String tableName) {
        String checkExist = check.checkExist(tableName);
        if (checkExist != null) {
            return ResponseVO.ofFailure(checkExist);
        }
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
        String checkExist = check.checkExist(tableName);
        if (checkExist != null) {
            return ResponseVO.ofFailure(checkExist);
        }
        String checkEnable = check.checkEnable(tableName);
        if (checkEnable != null) {
            return ResponseVO.ofFailure(checkEnable);
        }
        boolean disable = dao.disable(tableName);
        if (!disable) {
            return ResponseVO.ofFailure("server error");
        }
        return ResponseVO.ofSuccess(null);
    }

    @Override
    public ResponseVO<Void> enable(String tableName) {
        String checkExist = check.checkExist(tableName);
        if (checkExist != null) {
            return ResponseVO.ofFailure(checkExist);
        }
        String checkDisable = check.checkDisable(tableName);
        if (checkDisable != null) {
            return ResponseVO.ofFailure(checkDisable);
        }
        boolean enable = dao.enable(tableName);
        if (!enable) {
            return ResponseVO.ofFailure("server error");
        }
        return ResponseVO.ofSuccess(null);
    }

    @Override
    public ResponseVO<List<String>> listFamily(String tableName) {
        String checkExist = check.checkExist(tableName);
        if (checkExist != null) {
            return ResponseVO.ofFailure(checkExist);
        }
        return ResponseVO.ofSuccess(dao.listFamily(tableName));
    }

    @Override
    public ResponseVO<Void> addFamily(String tableName, String family) {
        String checkResult = check.checkTableAndFamily(tableName, family);
        if (checkResult != null) {
            return ResponseVO.ofFailure(checkResult);
        }
        boolean add = dao.addFamily(tableName, family);
        if (add) {
            return ResponseVO.ofSuccess(null);
        }
        return ResponseVO.ofFailure("server error");
    }

    @Override
    public ResponseVO<Void> deleteFamily(String tableName, String family) {
        String checkExist = check.checkExist(tableName);
        if (checkExist != null) {
            return ResponseVO.ofFailure(checkExist);
        }
        List<String> families = dao.listFamily(tableName);
        if (families == null) {
            return ResponseVO.ofFailure("server error");
        }
        boolean existFamily = families.contains(family);
        if (!existFamily) {
            return ResponseVO.ofFailure("family does not exist");
        }
        if (families.size() == 1) {
            return ResponseVO.ofFailure("table has one family at least");
        }
        boolean delete = dao.deleteFamily(tableName, family);
        if (delete) {
            return ResponseVO.ofSuccess(null);
        }
        return ResponseVO.ofFailure("server error");
    }
}
