package com.cherlshall.butterfly.module.hdfs;

import com.cherlshall.butterfly.module.hdfs.dao.HdfsContentDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContentDaoTest {

    @Autowired
    private HdfsContentDao dao;

    @Test
    public void write() {
        boolean write = dao.write("/test/test.txt", "hello world!");
        System.out.println(write);
    }

    @Test
    public void read() {
        String read = dao.read("/test/test.txt");
        System.out.println(read);
    }
}
