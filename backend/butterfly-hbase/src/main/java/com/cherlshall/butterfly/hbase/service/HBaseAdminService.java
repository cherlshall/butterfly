package com.cherlshall.butterfly.hbase.service;

import com.cherlshall.butterfly.hbase.entity.HTableDetail;

import java.util.List;

public interface HBaseAdminService {

    void create(String tableName, String... families);

    void delete(String tableName);

    String[] list();

    List<HTableDetail> detail();

    void disable(String tableName);

    void enable(String tableName);

    List<String> listFamily(String tableName);

    int addFamily(String tableName, String... family);

    int deleteFamily(String tableName, String... family);

    List<String> truncate(List<String> tableName);
}
