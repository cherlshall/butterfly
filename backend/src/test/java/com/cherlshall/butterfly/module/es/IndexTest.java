package com.cherlshall.butterfly.module.es;

import com.cherlshall.butterfly.module.elasticsearch.dao.EsIndexDao;
import com.cherlshall.butterfly.module.elasticsearch.service.EsIndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexTest {

    @Autowired
    private EsIndexService service;
    @Autowired
    private EsIndexDao dao;

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

    @Test
    public void mapping() {
        Map<String, Object> map = dao.mapping("t1");
        System.out.println(map);
    }

    @Test
    public void properties() {
        String[] properties = service.properties("t1");
        System.out.println(Arrays.toString(properties));
    }

}
