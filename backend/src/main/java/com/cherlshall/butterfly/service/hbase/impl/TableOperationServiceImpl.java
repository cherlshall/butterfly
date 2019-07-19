package com.cherlshall.butterfly.service.hbase.impl;

import com.cherlshall.butterfly.dao.hbase.AdminOperationDao;
import com.cherlshall.butterfly.dao.hbase.TableOperationDao;
import com.cherlshall.butterfly.entity.hbase.HBaseBean;
import com.cherlshall.butterfly.entity.hbase.HBaseTable;
import com.cherlshall.butterfly.service.hbase.TableOperationService;
import com.cherlshall.butterfly.util.vo.ResponseVO;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableOperationServiceImpl implements TableOperationService {

    @Autowired
    TableOperationDao dao;
    @Autowired
    AdminOperationDao adminDao;

    @Override
    public ResponseVO<HBaseTable> findByPage(String tableName, String rowKey, int pageSize) {
        Boolean disable = adminDao.isDisable(tableName);
        if (disable == null || disable) {
            return ResponseVO.ofFailure("table is disabled");
        }
        List<Result> results = dao.findByPage(tableName, rowKey, pageSize);
        HBaseTable table = new HBaseTable(results);
        return ResponseVO.ofSuccess(table);
    }

    @Override
    public ResponseVO<Integer> insertRow(String tableName, String rowKey, List<HBaseBean> beans) {
        Boolean exist = adminDao.exist(tableName);
        if (exist == null) {
            return ResponseVO.ofFailure("server error");
        }
        if (!exist) {
            return ResponseVO.ofFailure("table does not exist");
        }
        Boolean disable = adminDao.isDisable(tableName);
        if (disable == null) {
            return ResponseVO.ofFailure("server error");
        }
        if (disable) {
            return ResponseVO.ofFailure("table is disabled");
        }
        int insertSuccess = 0;
        for (HBaseBean bean : beans) {
            try {
                insertSuccess += dao.insert(tableName, rowKey,
                        bean.getFamily(), bean.getQualifier(), bean.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseVO.ofSuccess(insertSuccess);
    }

    @Override
    public ResponseVO<Void> deleteRow(String tableName, String rowKey) {
        Boolean exist = adminDao.exist(tableName);
        if (exist == null) {
            return ResponseVO.ofFailure("server error");
        }
        if (!exist) {
            return ResponseVO.ofFailure("table does not exist");
        }
        int delete = dao.delete(tableName, rowKey);
        if (delete > 0) {
            return ResponseVO.ofSuccess(null);
        } else {
            return ResponseVO.ofFailure("server error");
        }
    }
}
