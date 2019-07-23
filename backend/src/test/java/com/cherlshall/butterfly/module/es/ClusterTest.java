package com.cherlshall.butterfly.module.es;

import com.cherlshall.butterfly.module.elasticsearch.entity.ClusterHealth;
import com.cherlshall.butterfly.module.elasticsearch.service.EsClusterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClusterTest {

    @Autowired
    private EsClusterService service;

    @Test
    public void health() {
        ClusterHealth health = service.health();
        System.out.println(health);
    }

}
