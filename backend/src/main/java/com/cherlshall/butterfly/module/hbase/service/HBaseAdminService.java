package com.cherlshall.butterfly.module.hbase.service;

import com.cherlshall.butterfly.module.hbase.entity.HTableDetail;

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

    /**
     * 清空表
     * @param tableName 需要清空的表的表名
     * @return 清空失败的表名
     */
    List<String> truncate(List<String> tableName);
}
