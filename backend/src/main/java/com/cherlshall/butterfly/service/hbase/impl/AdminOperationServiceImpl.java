package com.cherlshall.butterfly.service.hbase.impl;

import com.cherlshall.butterfly.dao.hbase.AdminOperationDao;
import com.cherlshall.butterfly.service.hbase.AdminOperationService;
import com.cherlshall.butterfly.util.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminOperationServiceImpl implements AdminOperationService {

    @Autowired
    AdminOperationDao dao;

    @Override
    public ResponseVO<String> create(String tableName, String... families) {
        boolean create = dao.create(tableName, families);
        if (create) {
            return ResponseVO.ofSuccess(null);
        } else {
            return ResponseVO.ofFailure(null);
        }
    }

    @Override
    public ResponseVO<String> delete(String tableName) {
        boolean delete = dao.delete(tableName);
        if (delete) {
            return ResponseVO.ofSuccess(null);
        } else {
            return ResponseVO.ofFailure(null);
        }
    }

    @Override
    public ResponseVO<Boolean> exist(String tableName) {
        boolean exist = dao.delete(tableName);
        return ResponseVO.ofSuccess(exist);
    }

    @Override
    public ResponseVO<String[]> list() {
        return ResponseVO.ofSuccess(dao.list());
    }
}
