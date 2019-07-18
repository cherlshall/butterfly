package com.cherlshall.butterfly.dao.hbase;

import com.cherlshall.butterfly.entity.hbase.HTableDetail;

import java.util.List;

public interface AdminOperationDao {

    boolean create(String tableName, String... families);

    boolean delete(String tableName);

    Boolean exist(String tableName);

    String[] list();

    List<HTableDetail> detail();

    boolean disable(String tableName);

    boolean enable(String tableName);

    Boolean isDisable(String tableName);

    Boolean existFamily(String tableName, String family);

    boolean addFamily(String tableName, String family);

    boolean deleteFamily(String tableName, String family);

}
