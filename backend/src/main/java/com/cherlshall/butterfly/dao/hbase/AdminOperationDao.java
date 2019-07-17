package com.cherlshall.butterfly.dao.hbase;

public interface AdminOperationDao {

    boolean create(String tableName, String... families);

    boolean delete(String tableName);

    boolean exist(String tableName);

    String[] list();
}
