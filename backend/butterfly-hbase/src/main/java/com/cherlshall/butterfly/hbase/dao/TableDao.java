package com.cherlshall.butterfly.hbase.dao;

import org.apache.hadoop.hbase.client.Result;

import java.util.List;

public interface TableDao {

    List<Result> findByRowKeyAndPage(String tableName, String rowKey, int pageSize);

    List<Result> findByPage(String tableName, int pageSize);

    List<Result> findByTimestampAndRowKey(String tableName, String rowKey, int pageSize, long start, long end);

    List<Result> findByTimestamp(String tableName, int pageSize, long start, long end);

    Result findByRowKey(String tableName, String rowKey);

    int insert(String tableName, String rowName, String familyName, String qualifier, String value);

    int delete(String tableName, String rowName);

    int delete(String tableName, String rowName, String familyName, String qualifier);

    int update(String tableName, String rowName, String familyName, String qualifier, String value);
}
