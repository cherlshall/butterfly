package com.cherlshall.butterfly.module.hbase.service;

import com.cherlshall.butterfly.common.vo.ResponseVO;
import com.cherlshall.butterfly.module.hbase.entity.HBaseBean;
import com.cherlshall.butterfly.module.hbase.entity.HBaseTable;

import java.util.List;

public interface TableService {

    ResponseVO<HBaseTable> findByPage(String tableName, String rowKey, int pageSize, boolean removeFirst);

    ResponseVO<HBaseTable> findByRowKey(String tableName, String rowKey);

    ResponseVO<Integer> insertRow(String tableName, String rowKey, List<HBaseBean> beans);

    ResponseVO<Void> deleteRow(String tableName, String rowKey);

    ResponseVO<Void> deleteCol(String tableName, String rowName, String familyName, String qualifier);
}
