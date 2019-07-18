package com.cherlshall.butterfly.service.hbase.impl;

import com.cherlshall.butterfly.dao.hbase.AdminOperationDao;
import com.cherlshall.butterfly.dao.hbase.TableOperationDao;
import com.cherlshall.butterfly.entity.hbase.HBaseTable;
import com.cherlshall.butterfly.service.hbase.TableOperationService;
import com.cherlshall.butterfly.util.vo.ResponseVO;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableOperationServiceImpl implements TableOperationService {

    @Autowired
    TableOperationDao dao;
    @Autowired
    AdminOperationDao adminDao;

    @Override
    public ResponseVO<HBaseTable> findByPage(String tableName, String rowKey, int pageSize) {
        Boolean disable = adminDao.isDisable(tableName);
        if (disable == null || disable) {
            return ResponseVO.ofFailure("table is disabled");
        }
        List<Result> results = dao.findByPage(tableName, rowKey, pageSize);
        HBaseTable table = new HBaseTable(results);
        return ResponseVO.ofSuccess(table);
    }
}
