package com.cherlshall.butterfly.module.hbase.dao;

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
    TableDao dao;

    @Test
    public void insert() {
        dao.insert("test", "3", "f1", "name", "jack");
        dao.insert("test", "3", "f2", "chinese", "90");
        dao.insert("test", "3", "f3", "animal", "fish");
        dao.insert("test", "6", "f1", "name", "rose");
        dao.insert("test", "6", "f1", "age", "17");
        dao.insert("test", "6", "f2", "math", "88");
        dao.insert("test", "6", "f3", "animal", "cat");
        dao.insert("test", "6", "f3", "game", "pal4");
        dao.insert("test", "7", "f1", "name", "irelia");
        dao.insert("test", "7", "f1", "age", "18");
        dao.insert("test", "7", "f2", "game", "lol");
        dao.insert("test", "9", "f1", "name", "susan");
        dao.insert("test", "9", "f2", "math", "59");
        dao.insert("test", "9", "f3", "chinese", "hebei");
    }

    @Test
    public void find() {
        List<Result> test = dao.findByPage("test", 10);
        System.out.println(test.size());
    }
}
