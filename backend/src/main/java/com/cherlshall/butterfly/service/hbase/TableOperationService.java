package com.cherlshall.butterfly.service.hbase;

import com.cherlshall.butterfly.entity.hbase.HBaseTable;
import com.cherlshall.butterfly.util.vo.ResponseVO;

public interface TableOperationService {

    ResponseVO<HBaseTable> findByPage(String tableName, String rowKey, int pageSize);
}
