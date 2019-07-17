package com.cherlshall.butterfly.hbase.dao;

import com.cherlshall.butterfly.dao.hbase.TableOperationDao;
import org.apache.hadoop.hbase.client.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TableTest {

    @Autowired
    TableOperationDao dao;

    @Test
    public void insert() {
        dao.insert("test", "3", "f1", "name", "name1");
        dao.insert("test", "4", "f1", "name", "name2");
        dao.insert("test", "5", "f1", "name", "name3");
        dao.insert("test", "6", "f1", "name", "name4");
        dao.insert("test", "7", "f1", "name", "name5");
        dao.insert("test", "8", "f1", "name", "name6");
        dao.insert("test", "9", "f1", "name", "name7");
        dao.insert("test", "10", "f1", "name", "name8");
        dao.insert("test", "11", "f1", "name", "name9");
        dao.insert("test", "12", "f1", "name", "name10");
        dao.insert("test", "13", "f1", "name", "name11");
        dao.insert("test", "14", "f1", "name", "name12");
        dao.insert("test", "15", "f1", "name", "name13");
        dao.insert("test", "16", "f1", "name", "name14");
    }

    @Test
    public void find() {
        List<Result> test = dao.findByPage("test", "2", 10);
        System.out.println(test.size());
    }
}
