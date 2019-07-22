package com.cherlshall.butterfly.module.hbase.dao.impl;

import com.cherlshall.butterfly.module.hbase.dao.TableDao;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TableDaoImpl implements TableDao {

    @Autowired
    HbaseTemplate template;

    @Override
    public List<Result> findByRowKeyAndPage(String tableName, String rowKey, int pageSize) {
        Scan scan = new Scan();
        scan.setStartRow(rowKey.getBytes());
        scan.setFilter(new PageFilter(pageSize));
        return template.find(tableName, scan, (Result result, int i) -> result);
    }

    @Override
    public List<Result> findByPage(String tableName, int pageSize) {
        Scan scan = new Scan();
        scan.setFilter(new PageFilter(pageSize));
        return template.find(tableName, scan, (Result result, int i) -> result);
    }

    @Override
    public Result findByRowKey(String tableName, String rowKey) {
        return template.get(tableName, rowKey, (Result result, int i) -> result);
    }

    @Override
    public int insert(String tableName, String rowName, String familyName, String qualifier, String value) {
        template.put(tableName, rowName, familyName, qualifier, value.getBytes());
        return 1;
    }

    @Override
    public int delete(String tableName, String rowName) {
        return template.execute(tableName, hTableInterface -> {
            try {
                Delete delete = new Delete(rowName.getBytes());
                hTableInterface.delete(delete);
                return 1;
            } catch (Exception e) {
                return 0;
            }
        });
    }

    @Override
    public int delete(String tableName, String rowName, String familyName, String qualifier) {
        try {
            template.delete(tableName, rowName, familyName, qualifier);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int update(String tableName, String rowName, String familyName, String qualifier, String value) {
        template.put(tableName, rowName, familyName, qualifier, value.getBytes());
        return 1;
    }
}
