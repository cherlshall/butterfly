package com.cherlshall.butterfly.module.hbase.dao.impl;

import com.cherlshall.butterfly.module.hbase.dao.HBaseAdminDao;
import com.cherlshall.butterfly.module.hbase.entity.HTableDetail;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class HBaseAdminDaoImpl implements HBaseAdminDao {

    @Autowired
    HbaseTemplate template;
    @Autowired
    Admin admin;

    @Override
    public boolean create(String tableName, String... families) {
        HTableDescriptor hd = new HTableDescriptor(TableName.valueOf(tableName));
        for (String family : families) {
            hd.addFamily(new HColumnDescriptor(family.getBytes()));
        }
        try (Connection connection = ConnectionFactory.createConnection(template.getConfiguration());
             Admin admin = connection.getAdmin()) {
            admin.createTable(hd);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(String tableName) {
        try {
            TableName tn = TableName.valueOf(tableName);
            if (!admin.isTableDisabled(tn)) {
                admin.disableTable(tn);
            }
            admin.deleteTable(tn);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean exist(String tableName) {
        try {
            return admin.tableExists(TableName.valueOf(tableName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String[] list() {
        try {
            TableName[] tableNames = admin.listTableNames();
            String[] tableStrNames = new String[tableNames.length];
            for (int i = 0; i < tableNames.length; i++) {
                tableStrNames[i] = tableNames[i].getNameAsString();
            }
            return tableStrNames;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<HTableDetail> detail() {
        try {
            HTableDescriptor[] hTables = admin.listTables();
            List<HTableDetail> tables = new ArrayList<>();
            for (HTableDescriptor hTable : hTables) {
                HTableDetail detail = new HTableDetail();
                detail.setTableName(hTable.getNameAsString());
                detail.setReadOnly(hTable.isReadOnly());
                detail.setRegionReplication(hTable.getRegionReplication());
                detail.setDisable(admin.isTableDisabled(hTable.getTableName()));
                Set<byte[]> familiesKeys = hTable.getFamiliesKeys();
                List<String> families = new ArrayList<>();
                detail.setFamilies(families);
                for (byte[] key : familiesKeys) {
                    families.add(new String(key));
                }
                tables.add(detail);
            }
            return tables;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean disable(String tableName) {
        try {
            admin.disableTable(TableName.valueOf(tableName));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean enable(String tableName) {
        try {
            admin.enableTable(TableName.valueOf(tableName));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean isDisable(String tableName) {
        try {
            return admin.isTableDisabled(TableName.valueOf(tableName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean existFamily(String tableName, String family) {
        try {
            return admin.getTableDescriptor(TableName.valueOf(tableName)).hasFamily(family.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> listFamily(String tableName) {
        try {
            Set<byte[]> familiesKeys = admin.getTableDescriptor(TableName.valueOf(tableName))
                    .getFamiliesKeys();
            List<String> list = new ArrayList<>();
            for (byte[] key : familiesKeys) {
                list.add(new String(key));
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int addFamily(String tableName, String... families) {
        int addNum = 0;
        for (String f : families) {
            try {
                admin.addColumn(TableName.valueOf(tableName), new HColumnDescriptor(f));
                addNum++;
            } catch (Exception ignore) {
            }
        }
        return addNum;
    }

    @Override
    public int deleteFamily(String tableName, String... families) {
        int deleteNum = 0;
        for (String f : families) {
            try {
                admin.deleteColumn(TableName.valueOf(tableName), f.getBytes());
                deleteNum++;
            } catch (Exception ignore) {
            }
        }
        return deleteNum;
    }
}
