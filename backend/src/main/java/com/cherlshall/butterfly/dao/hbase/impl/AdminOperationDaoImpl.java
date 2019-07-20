package com.cherlshall.butterfly.dao.hbase.impl;

import com.cherlshall.butterfly.dao.hbase.AdminOperationDao;
import com.cherlshall.butterfly.entity.hbase.HTableDetail;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.exceptions.DeserializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public class AdminOperationDaoImpl implements AdminOperationDao {

    @Autowired
    HbaseTemplate template;

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
        try (Connection connection = ConnectionFactory.createConnection(template.getConfiguration());
             Admin admin = connection.getAdmin()) {
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
        try (Connection connection = ConnectionFactory.createConnection(template.getConfiguration());
             Admin admin = connection.getAdmin()) {
            return admin.tableExists(TableName.valueOf(tableName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String[] list() {
        try (Connection connection = ConnectionFactory.createConnection(template.getConfiguration());
             Admin admin = connection.getAdmin()) {
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
        try (Connection connection = ConnectionFactory.createConnection(template.getConfiguration());
             Admin admin = connection.getAdmin()) {
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
        try (Connection connection = ConnectionFactory.createConnection(template.getConfiguration());
             Admin admin = connection.getAdmin()) {
            admin.disableTable(TableName.valueOf(tableName));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean enable(String tableName) {
        try (Connection connection = ConnectionFactory.createConnection(template.getConfiguration());
             Admin admin = connection.getAdmin()) {
            admin.enableTable(TableName.valueOf(tableName));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean isDisable(String tableName) {
        try (Connection connection = ConnectionFactory.createConnection(template.getConfiguration());
             Admin admin = connection.getAdmin()) {
            return admin.isTableDisabled(TableName.valueOf(tableName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean existFamily(String tableName, String family) {
        try (Connection connection = ConnectionFactory.createConnection(template.getConfiguration());
             Admin admin = connection.getAdmin()) {
            return admin.getTableDescriptor(TableName.valueOf(tableName)).hasFamily(family.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> listFamily(String tableName) {
        try (Connection connection = ConnectionFactory.createConnection(template.getConfiguration());
             Admin admin = connection.getAdmin()) {
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
    public boolean addFamily(String tableName, String family) {
        try (Connection connection = ConnectionFactory.createConnection(template.getConfiguration());
             Admin admin = connection.getAdmin()) {
            admin.addColumn(TableName.valueOf(tableName), new HColumnDescriptor(family));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteFamily(String tableName, String family) {
        try (Connection connection = ConnectionFactory.createConnection(template.getConfiguration());
             Admin admin = connection.getAdmin()) {
            admin.deleteColumn(TableName.valueOf(tableName), family.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
