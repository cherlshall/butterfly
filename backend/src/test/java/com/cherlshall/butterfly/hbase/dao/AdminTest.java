package com.cherlshall.butterfly.hbase.dao;

import com.cherlshall.butterfly.dao.hbase.AdminOperationDao;
import com.cherlshall.butterfly.entity.hbase.HTableDetail;
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
        dao.create("test1", "f1", "f2", "f3");
        dao.create("test2", "f1", "f2", "f3");
        dao.create("test3", "f1", "f2", "f3");
        dao.create("test4", "f1", "f2", "f3");
        dao.create("test5", "f1", "f2", "f3");
        dao.create("test6", "f1", "f2", "f3");
        dao.create("test7", "f1", "f2", "f3");
        dao.create("test8", "f1", "f2", "f3");
        dao.create("test9", "f1", "f2", "f3");
        dao.create("test10", "f1", "f2", "f3");
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

    @Test
    public void detail() {
        List<HTableDetail> detail = dao.detail();
        System.out.println(detail);
    }

    @Test
    public void isDisable() {
        System.out.println(dao.isDisable("test1"));
    }

    @Test
    public void disable() {
        dao.disable("test1");
        System.out.println(dao.isDisable("test1"));
    }

    @Test
    public void enable() {
        dao.enable("test1");
        System.out.println(dao.isDisable("test1"));
    }

    @Test
    public void addFamily() {
        boolean b = dao.addFamily("test1", "f4");
        System.out.println(b);
    }

    @Test
    public void deleteFamily() {
        boolean b = dao.deleteFamily("test2", "f1");
        System.out.println(b);
    }
}
