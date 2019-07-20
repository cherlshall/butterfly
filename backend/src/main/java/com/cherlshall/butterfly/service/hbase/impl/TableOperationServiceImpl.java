package com.cherlshall.butterfly.service.hbase.impl;

import com.cherlshall.butterfly.dao.hbase.AdminOperationDao;
import com.cherlshall.butterfly.dao.hbase.TableOperationDao;
import com.cherlshall.butterfly.entity.hbase.HBaseBean;
import com.cherlshall.butterfly.entity.hbase.HBaseTable;
import com.cherlshall.butterfly.service.hbase.TableOperationService;
import com.cherlshall.butterfly.util.hbase.Check;
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
    @Autowired
    Check check;

    @Override
    public ResponseVO<HBaseTable> findByPage(String tableName, String rowKey, int pageSize, boolean removeFirst) {
        String checkUsable = check.checkUsable(tableName);
        if (checkUsable != null) {
            return ResponseVO.ofFailure(checkUsable);
        }
        if (rowKey == null || rowKey.isEmpty()) {
            return ResponseVO.ofSuccess(new HBaseTable(dao.findByPage(tableName, pageSize)));
        }
        if (removeFirst) {
            pageSize++;
        }
        List<Result> results = dao.findByRowKeyAndPage(tableName, rowKey, pageSize);
        HBaseTable table = new HBaseTable(results);
        return ResponseVO.ofSuccess(table);
    }

    @Override
    public ResponseVO<HBaseTable> findByRowKey(String tableName, String rowKey) {
        String checkUsable = check.checkUsable(tableName);
        if (checkUsable != null) {
            return ResponseVO.ofFailure(checkUsable);
        }
        return ResponseVO.ofSuccess(new HBaseTable(dao.findByRowKey(tableName, rowKey)));
    }

    @Override
    public ResponseVO<Integer> insertRow(String tableName, String rowKey, List<HBaseBean> beans) {
        String checkUsable = check.checkUsable(tableName);
        if (checkUsable != null) {
            return ResponseVO.ofFailure(checkUsable);
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
        String checkExist = check.checkExist(tableName);
        if (checkExist != null) {
            return ResponseVO.ofFailure(checkExist);
        }
        int delete = dao.delete(tableName, rowKey);
        if (delete > 0) {
            return ResponseVO.ofSuccess(null);
        } else {
            return ResponseVO.ofFailure("server error");
        }
    }


}
