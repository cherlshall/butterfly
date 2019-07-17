package com.cherlshall.butterfly.service.hbase;

import com.cherlshall.butterfly.util.vo.ResponseVO;

public interface AdminOperationService {

    ResponseVO<String> create(String tableName, String... families);

    ResponseVO<String> delete(String tableName);

    ResponseVO<Boolean> exist(String tableName);

    ResponseVO<String[]> list();
}
