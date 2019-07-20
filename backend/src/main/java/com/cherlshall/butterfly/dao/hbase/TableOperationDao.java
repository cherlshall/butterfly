package com.cherlshall.butterfly.dao.hbase;

import org.apache.hadoop.hbase.client.Result;

import java.util.List;

public interface TableOperationDao {

    List<Result> findByRowKeyAndPage(String tableName, String rowKey, int pageSize);

    List<Result> findByPage(String tableName, int pageSize);

    Result findByRowKey(String tableName, String rowKey);

    int insert(String tableName, String rowName, String familyName, String qualifier, String value);

    int delete(String tableName, String rowName);

    int delete(String tableName, String rowName, String familyName, String qualifier);

    int update(String tableName, String rowName, String familyName, String qualifier, String value);
}
