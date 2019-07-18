package com.cherlshall.butterfly.service.hbase.impl;

import com.cherlshall.butterfly.dao.hbase.AdminOperationDao;
import com.cherlshall.butterfly.entity.hbase.HTableDetail;
import com.cherlshall.butterfly.service.hbase.AdminOperationService;
import com.cherlshall.butterfly.util.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminOperationServiceImpl implements AdminOperationService {

    @Autowired
    AdminOperationDao dao;

    @Override
    public ResponseVO<Void> create(String tableName, String... families) {
        if (families.length == 0) {
            return ResponseVO.ofFailure("Table should have at least one column family");
        }
        boolean create = dao.create(tableName, families);
        if (create) {
            return ResponseVO.ofSuccess(null);
        } else {
            return ResponseVO.ofFailure(null);
        }
    }

    @Override
    public ResponseVO<Void> delete(String tableName) {
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
        Boolean exist = dao.exist(tableName);
        if (exist == null) {
            return ResponseVO.ofFailure("server error");
        }
        if (!exist) {
            return ResponseVO.ofFailure("table does not exist");
        }
        Boolean isDisable = dao.isDisable(tableName);
        if (isDisable) {
            return ResponseVO.ofFailure("table is already disabled");
        }
        boolean disable = dao.disable(tableName);
        if (!disable) {
            return ResponseVO.ofFailure("server error");
        }
        return ResponseVO.ofSuccess(null);
    }

    @Override
    public ResponseVO<Void> enable(String tableName) {
        Boolean exist = dao.exist(tableName);
        if (exist == null) {
            return ResponseVO.ofFailure("server error");
        }
        if (!exist) {
            return ResponseVO.ofFailure("table does not exist");
        }
        Boolean isDisable = dao.isDisable(tableName);
        if (!isDisable) {
            return ResponseVO.ofFailure("table is already enabled");
        }
        boolean enable = dao.enable(tableName);
        if (!enable) {
            return ResponseVO.ofFailure("server error");
        }
        return ResponseVO.ofSuccess(null);
    }

    @Override
    public ResponseVO<Void> addFamily(String tableName, String family) {
        Boolean exist = dao.exist(tableName);
        if (exist == null) {
            return ResponseVO.ofFailure("server error");
        }
        if (!exist) {
            return ResponseVO.ofFailure("table does not exist");
        }
        Boolean existFamily = dao.existFamily(tableName, family);
        if (existFamily) {
            return ResponseVO.ofFailure("family already exists");
        }
        boolean add = dao.addFamily(tableName, family);
        if (add) {
            return ResponseVO.ofSuccess(null);
        }
        return ResponseVO.ofFailure("server error");
    }

    @Override
    public ResponseVO<Void> deleteFamily(String tableName, String family) {
        Boolean exist = dao.exist(tableName);
        if (exist == null) {
            return ResponseVO.ofFailure("server error");
        }
        if (!exist) {
            return ResponseVO.ofFailure("table does not exist");
        }
        Boolean existFamily = dao.existFamily(tableName, family);
        if (!existFamily) {
            return ResponseVO.ofFailure("family does not exist");
        }
        boolean delete = dao.deleteFamily(tableName, family);
        if (delete) {
            return ResponseVO.ofSuccess(null);
        }
        return ResponseVO.ofFailure("server error");
    }
}
