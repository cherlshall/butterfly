package com.cherlshall.butterfly.module.es;

import com.cherlshall.butterfly.common.vo.PageData;
import com.cherlshall.butterfly.module.elasticsearch.dao.EsDocDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DocTest {

    @Autowired
    private EsDocDao dao;

    @Test
    public void insert() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("p1", "p1v");
        dao.insert("t1", map);
    }

    @Test
    public void list() {
        PageData<Map<String, Object>> result = dao.selectByPage("t1", 0, 10, null, null);
        System.out.println(result.getTotal());
        System.out.println(result.getDataSource());
    }
}
