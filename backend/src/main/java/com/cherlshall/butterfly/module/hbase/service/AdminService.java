package com.cherlshall.butterfly.module.hbase.service;

import com.cherlshall.butterfly.common.vo.ResponseVO;
import com.cherlshall.butterfly.module.hbase.entity.HTableDetail;

import java.util.List;

public interface AdminService {

    ResponseVO<Void> create(String tableName, String... families);

    ResponseVO<Void> delete(String tableName);

    ResponseVO<String[]> list();

    ResponseVO<List<HTableDetail>> detail();

    ResponseVO<Void> disable(String tableName);

    ResponseVO<Void> enable(String tableName);

    ResponseVO<List<String>> listFamily(String tableName);

    int addFamily(String tableName, String... family);

    int deleteFamily(String tableName, String... family);
}
