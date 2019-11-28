package com.cherlshall.butterfly.hbase.dao.impl;

import com.cherlshall.butterfly.hbase.dao.HBaseTableDao;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class HBaseTableDaoImpl implements HBaseTableDao {

    @Autowired
    HbaseTemplate template;

    @Override
    public List<Result> findByRowKeyAndPage(String tableName, String rowKey, int pageSize) {
        Scan scan = new Scan();
        scan.setStartRow(Bytes.fromHex(rowKey));
        scan.setFilter(new PageFilter(pageSize));
        List<Result> results = template.find(tableName, scan, (Result result, int i) -> result);
        if (results.size() > pageSize) {
            return results.subList(0, pageSize);
        }
        return results;
    }

    @Override
    public List<Result> findByPage(String tableName, int pageSize) {
        Scan scan = new Scan();
        scan.setFilter(new PageFilter(pageSize));
        List<Result> results = template.find(tableName, scan, (Result result, int i) -> result);
        if (results.size() > pageSize) {
            return results.subList(0, pageSize);
        }
        return results;
    }

    @Override
    public List<Result> findByTimestampAndRowKey(String tableName, String rowKey, int pageSize, long start, long end) {
        Scan scan = new Scan();
        scan.setStartRow(Bytes.fromHex(rowKey));
        try {
            scan.setTimeRange(start, end);
        } catch (IOException e) {
            e.printStackTrace();
        }
        scan.setFilter(new PageFilter(pageSize));
        List<Result> results = template.find(tableName, scan, (Result result, int i) -> result);
        if (results.size() > pageSize) {
            return results.subList(0, pageSize);
        }
        return results;
    }

    @Override
    public List<Result> findByTimestamp(String tableName, int pageSize, long start, long end) {
        Scan scan = new Scan();
        try {
            scan.setTimeRange(start, end);
        } catch (IOException e) {
            e.printStackTrace();
        }
        scan.setFilter(new PageFilter(pageSize));
        List<Result> results = template.find(tableName, scan, (Result result, int i) -> result);
        if (results.size() > pageSize) {
            return results.subList(0, pageSize);
        }
        return results;
    }

    @Override
    public Result findByRowKey(String tableName, String rowKey) {
        return template.execute(tableName, hTableInterface -> {
            Get get = new Get(Bytes.fromHex(rowKey));
            return hTableInterface.get(get);
        });
    }

    @Override
    public int insert(String tableName, String rowName, String familyName, String qualifier, String value) {
        return template.execute(tableName, hTableInterface -> {
            Put put = new Put(Bytes.fromHex(rowName));
            put.addColumn(familyName.getBytes(), qualifier.getBytes(), value.getBytes());
            hTableInterface.put(put);
            return 1;
        });
    }

    @Override
    public int delete(String tableName, String rowName) {
        return template.execute(tableName, hTableInterface -> {
            try {
                Delete delete = new Delete(Bytes.fromHex(rowName));
                hTableInterface.delete(delete);
                return 1;
            } catch (Exception e) {
                return 0;
            }
        });
    }

    @Override
    public int delete(String tableName, String rowName, String familyName, String qualifier) {
        return template.execute(tableName, hTableInterface -> {
            Delete delete = new Delete(Bytes.fromHex(rowName));
            delete.addColumn(familyName.getBytes(), qualifier.getBytes());
            hTableInterface.delete(delete);
            return 1;
        });
    }

    @Override
    public int update(String tableName, String rowName, String familyName, String qualifier, String value) {
        return template.execute(tableName, hTableInterface -> {
            Put put = new Put(Bytes.fromHex(rowName));
            put.addColumn(familyName.getBytes(), qualifier.getBytes(), value.getBytes());
            hTableInterface.put(put);
            return 1;
        });
    }
}
