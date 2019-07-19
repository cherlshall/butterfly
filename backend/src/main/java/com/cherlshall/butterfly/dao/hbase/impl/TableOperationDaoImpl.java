package com.cherlshall.butterfly.dao.hbase.impl;

import com.cherlshall.butterfly.dao.hbase.TableOperationDao;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TableOperationDaoImpl implements TableOperationDao {

    @Autowired
    HbaseTemplate template;

    @Override
    public List<Result> findByPage(String tableName, String rowKey, int pageSize) {
        Scan scan = new Scan();
        boolean first = rowKey != null && !rowKey.trim().equals("");
        if (first) {
            scan.setStartRow(rowKey.getBytes());
            pageSize++;
        }
        scan.setFilter(new PageFilter(pageSize));
        List<Result> results = template.find(tableName, scan, (Result result, int i) -> result);
        if (first) {
            results.remove(0);
        }
        return results;
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
        template.delete(tableName, rowName, familyName, qualifier);
        return 1;
    }

    @Override
    public int update(String tableName, String rowName, String familyName, String qualifier, String value) {
        template.put(tableName, rowName, familyName, qualifier, value.getBytes());
        return 1;
    }
}
