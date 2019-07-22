package com.cherlshall.butterfly.module.hbase.service;

import com.cherlshall.butterfly.module.hbase.entity.HTableDetail;

import java.util.List;

public interface HbaseAdminService {

    void create(String tableName, String... families);

    void delete(String tableName);

    String[] list();

    List<HTableDetail> detail();

    void disable(String tableName);

    void enable(String tableName);

    List<String> listFamily(String tableName);

    int addFamily(String tableName, String... family);

    int deleteFamily(String tableName, String... family);
}
