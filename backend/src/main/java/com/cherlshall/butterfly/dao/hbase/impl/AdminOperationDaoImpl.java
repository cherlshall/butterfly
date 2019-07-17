package com.cherlshall.butterfly.dao.hbase.impl;

import com.cherlshall.butterfly.dao.hbase.AdminOperationDao;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
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
            admin.disableTable(tn);
            admin.deleteTable(tn);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean exist(String tableName) {
        try (Connection connection = ConnectionFactory.createConnection(template.getConfiguration());
             Admin admin = connection.getAdmin()) {
            admin.tableExists(TableName.valueOf(tableName));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
}
