package com.cherlshall.butterfly.service.hbase;

import com.cherlshall.butterfly.entity.hbase.HBaseBean;
import com.cherlshall.butterfly.entity.hbase.HBaseTable;
import com.cherlshall.butterfly.util.vo.ResponseVO;

import java.util.List;

public interface TableOperationService {

    ResponseVO<HBaseTable> findByPage(String tableName, String rowKey, int pageSize);

    ResponseVO<Integer> insertRow(String tableName, String rowKey, List<HBaseBean> beans);

    ResponseVO<Void> deleteRow(String tableName, String rowKey);
}
