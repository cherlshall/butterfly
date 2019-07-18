package com.cherlshall.butterfly.service.hbase;

import com.cherlshall.butterfly.entity.hbase.HTableDetail;
import com.cherlshall.butterfly.util.vo.ResponseVO;

import java.util.List;

public interface AdminOperationService {

    ResponseVO<Void> create(String tableName, String... families);

    ResponseVO<Void> delete(String tableName);

    ResponseVO<String[]> list();

    ResponseVO<List<HTableDetail>> detail();

    ResponseVO<Void> disable(String tableName);

    ResponseVO<Void> enable(String tableName);

    ResponseVO<Void> addFamily(String tableName, String family);

    ResponseVO<Void> deleteFamily(String tableName, String family);
}
