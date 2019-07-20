package com.cherlshall.butterfly.util.hbase;

import com.cherlshall.butterfly.dao.hbase.AdminOperationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Check {

    @Autowired
    AdminOperationDao dao;

    public String checkUsable(String tableName) {
        String checkExist = checkExist(tableName);
        if (checkExist != null) {
            return checkExist;
        }
        Boolean disable = dao.isDisable(tableName);
        if (disable == null) {
            return "server error";
        }
        if (disable) {
            return "table " + tableName + " is disabled";
        }
        return null;
    }

    public String checkExist(String tableName) {
        Boolean exist = dao.exist(tableName);
        if (exist == null) {
            return "server error";
        }
        if (!exist) {
            return "table " + tableName + " does not exist";
        }
        return null;
    }
}
