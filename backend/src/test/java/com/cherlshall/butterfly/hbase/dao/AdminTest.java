package com.cherlshall.butterfly.hbase.dao;

import com.cherlshall.butterfly.dao.hbase.AdminOperationDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminTest {

    @Autowired
    AdminOperationDao dao;

    @Test
    public void create() {
        assert dao.create("test1", "f1", "f2", "f3");
    }

    @Test
    public void exist() {
        assert dao.exist("test");
    }

    @Test
    public void list() {
        String[] tableNames = dao.list();
        List<String> list = new ArrayList<>(Arrays.asList(tableNames));
        System.out.println(list);
        assert list.contains("test");
    }

    @Test
    public void delete() {
        assert dao.delete("test");
    }
}
