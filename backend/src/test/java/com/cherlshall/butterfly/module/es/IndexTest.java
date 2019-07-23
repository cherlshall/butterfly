package com.cherlshall.butterfly.module.es;

import com.cherlshall.butterfly.module.elasticsearch.service.EsIndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexTest {

    @Autowired
    private EsIndexService service;

    @Test
    public void list() {
        String[] list = service.list();
        System.out.println(Arrays.toString(list));
    }

    @Test
    public void create() {
        service.create("butterfly", null, null);
    }

    @Test
    public void delete() {
        service.delete("butterfly");
    }

}
