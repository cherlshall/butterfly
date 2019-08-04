package com.cherlshall.butterfly.hbase.service;

import com.cherlshall.butterfly.hbase.entity.HBaseBean;
import com.cherlshall.butterfly.hbase.entity.HBaseTable;

import java.util.List;

public interface HBaseTableService {

    HBaseTable findByPage(String tableName, String rowKey, int pageSize, boolean removeFirst);

    HBaseTable findByRowKey(String tableName, String rowKey);

    int insertRow(String tableName, String rowKey, List<HBaseBean> beans);

    void deleteRow(String tableName, String rowKey);

    void deleteCol(String tableName, String rowName, String familyName, String qualifier);
}
