package com.cherlshall.butterfly.hbase.dao;

import com.cherlshall.butterfly.hbase.entity.HTableDetail;

import java.util.List;

public interface AdminDao {

    boolean create(String tableName, String... families);

    boolean delete(String tableName);

    Boolean exist(String tableName);

    String[] list();

    List<HTableDetail> detail();

    boolean disable(String tableName);

    boolean enable(String tableName);

    Boolean isDisable(String tableName);

    Boolean existFamily(String tableName, String family);

    List<String> listFamily(String tableName);

    int addFamily(String tableName, String... families);

    int deleteFamily(String tableName, String... families);

    boolean truncate(String tableName);

}
