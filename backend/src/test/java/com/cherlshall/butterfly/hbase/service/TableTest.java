package com.cherlshall.butterfly.hbase.service;

import com.cherlshall.butterfly.entity.hbase.HBaseTable;
import com.cherlshall.butterfly.service.hbase.TableOperationService;
import com.cherlshall.butterfly.util.vo.ResponseVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TableTest {

    @Autowired
    TableOperationService service;

    @Test
    public void findAll() {
        ResponseVO<HBaseTable> test = service.findByPage("test", null, 10);
        System.out.println(test);
    }
}
