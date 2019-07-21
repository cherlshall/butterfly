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
        return checkEnable(tableName);
    }

    public String checkTableAndFamily(String tableName, String family) {
        String checkExist = checkExist(tableName);
        if (checkExist != null) {
            return checkExist;
        }
        return checkExist(tableName, family);
    }

    public String checkTableButFamily(String tableName, String family) {
        String checkExist = checkExist(tableName);
        if (checkExist != null) {
            return checkExist;
        }
        return checkNotExist(tableName, family);
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

    public String checkNotExist(String tableName) {
        Boolean exist = dao.exist(tableName);
        if (exist == null) {
            return "server error";
        }
        if (exist) {
            return "table " + tableName + " is already exist";
        }
        return null;
    }

    public String checkDisable(String tableName) {
        Boolean disable = dao.isDisable(tableName);
        if (disable == null) {
            return "server error";
        }
        if (!disable) {
            return "table " + tableName + " is already disabled";
        }
        return null;
    }

    public String checkEnable(String tableName) {
        Boolean disable = dao.isDisable(tableName);
        if (disable == null) {
            return "server error";
        }
        if (disable) {
            return "table " + tableName + " is disabled";
        }
        return null;
    }

    public String checkExist(String tableName, String family) {
        Boolean exist = dao.existFamily(tableName, family);
        if (exist == null) {
            return "server error";
        }
        if (!exist) {
            return "family " + family + " does not exist in table " + tableName;
        }
        return null;
    }

    public String checkNotExist(String tableName, String family) {
        Boolean exist = dao.existFamily(tableName, family);
        if (exist == null) {
            return "server error";
        }
        if (exist) {
            return "family " + family + " is already exist in table " + tableName;
        }
        return null;
    }
}
