package com.cherlshall.butterfly.module.hbase.util;

import com.cherlshall.butterfly.module.hbase.dao.HbaseAdminDao;
import com.cherlshall.butterfly.common.exception.ButterflyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HbaseCheck {

    @Autowired
    HbaseAdminDao dao;

    public void checkUsable(String tableName) {
        checkExist(tableName);
        checkEnable(tableName);
    }

    public void checkTableAndFamily(String tableName, String family) {
        checkExist(tableName);
        checkExist(tableName, family);
    }

    public void checkTableButFamily(String tableName, String family) {
        checkExist(tableName);
        checkNotExist(tableName, family);
    }

    public void checkExist(String tableName) {
        Boolean exist = dao.exist(tableName);
        if (exist == null) {
            throw new ButterflyException();
        }
        if (!exist) {
            throw new ButterflyException("table " + tableName + " does not exist");
        }
    }

    public void checkNotExist(String tableName) {
        Boolean exist = dao.exist(tableName);
        if (exist == null) {
            throw new ButterflyException();
        }
        if (exist) {
            throw new ButterflyException("table " + tableName + " is already exist");
        }
    }

    public void checkDisable(String tableName) {
        Boolean disable = dao.isDisable(tableName);
        if (disable == null) {
            throw new ButterflyException();
        }
        if (!disable) {
            throw new ButterflyException("table " + tableName + " is already disabled");
        }
    }

    public void checkEnable(String tableName) {
        Boolean disable = dao.isDisable(tableName);
        if (disable == null) {
            throw new ButterflyException();
        }
        if (disable) {
            throw new ButterflyException("table " + tableName + " is disabled");
        }
    }

    public void checkExist(String tableName, String family) {
        Boolean exist = dao.existFamily(tableName, family);
        if (exist == null) {
            throw new ButterflyException();
        }
        if (!exist) {
            throw new ButterflyException("family " + family + " does not exist in table " + tableName);
        }
    }

    public void checkNotExist(String tableName, String family) {
        Boolean exist = dao.existFamily(tableName, family);
        if (exist == null) {
            throw new ButterflyException();
        }
        if (exist) {
            throw new ButterflyException("family " + family + " is already exist in table " + tableName);
        }
    }
}
