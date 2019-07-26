package com.cherlshall.butterfly.module.hdfs;

import com.cherlshall.butterfly.module.hdfs.dao.HdfsContentDao;
import com.cherlshall.butterfly.module.hdfs.dao.HdfsFileDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileDaoTest {

    @Autowired
    private HdfsFileDao dao;

    @Test
    public void mkdirs() {
        boolean mkdirs = dao.mkdirs("/test");
        System.out.println(mkdirs);
    }

    @Test
    public void delete() {
        boolean delete = dao.delete("/test");
        System.out.println(delete);
    }

    @Test
    public void create() {
        boolean create = dao.create("/test/json1.txt");
        System.out.println(create);
    }


}
