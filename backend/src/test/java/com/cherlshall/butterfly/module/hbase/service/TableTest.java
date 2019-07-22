package com.cherlshall.butterfly.module.hbase.service;

import com.cherlshall.butterfly.module.hbase.entity.HBaseTable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TableTest {

    @Autowired
    TableService service;

    @Test
    public void findAll() {
        HBaseTable test = service.findByPage("test", "", 10, false);
        System.out.println(test);
    }
}
