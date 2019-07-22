package com.cherlshall.butterfly.module.hbase.service.impl;

import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.module.hbase.dao.TableDao;
import com.cherlshall.butterfly.module.hbase.entity.HBaseBean;
import com.cherlshall.butterfly.module.hbase.dao.AdminDao;
import com.cherlshall.butterfly.module.hbase.entity.HBaseTable;
import com.cherlshall.butterfly.module.hbase.service.TableService;
import com.cherlshall.butterfly.module.hbase.util.Check;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableServiceImpl implements TableService {

    @Autowired
    TableDao dao;
    @Autowired
    AdminDao adminDao;
    @Autowired
    Check check;

    @Override
    public HBaseTable findByPage(String tableName, String rowKey, int pageSize, boolean removeFirst) {
        check.checkUsable(tableName);
        if (rowKey == null || rowKey.isEmpty()) {
            return new HBaseTable(dao.findByPage(tableName, pageSize));
        }
        if (removeFirst) {
            pageSize++;
        }
        List<Result> results = dao.findByRowKeyAndPage(tableName, rowKey, pageSize);
        if (removeFirst && results.size() > 0) {
            results.remove(0);
        }
        return new HBaseTable(results);
    }

    @Override
    public HBaseTable findByRowKey(String tableName, String rowKey) {
        check.checkUsable(tableName);
        return new HBaseTable(dao.findByRowKey(tableName, rowKey));
    }

    @Override
    public int insertRow(String tableName, String rowKey, List<HBaseBean> beans) {
        check.checkUsable(tableName);
        int insertSuccess = 0;
        for (HBaseBean bean : beans) {
            try {
                insertSuccess += dao.insert(tableName, rowKey,
                        bean.getFamily(), bean.getQualifier(), bean.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return insertSuccess;
    }

    @Override
    public void deleteRow(String tableName, String rowKey) {
        check.checkExist(tableName);
        int delete = dao.delete(tableName, rowKey);
        if (delete <= 0) {
            throw new ButterflyException();
        }
    }

    @Override
    public void deleteCol(String tableName, String rowName, String familyName, String qualifier) {
        check.checkExist(tableName);
        int delete = dao.delete(tableName, rowName, familyName, qualifier);
        if (delete <= 0) {
            throw new ButterflyException("delete failure, maybe resource does not exist");
        }
    }


}
