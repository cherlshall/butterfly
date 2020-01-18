package com.cherlshall.butterfly.elasticsearch.service.impl;

import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.elasticsearch.dao.ClusterDao;
import com.cherlshall.butterfly.elasticsearch.entity.ClusterHealth;
import com.cherlshall.butterfly.elasticsearch.service.ClusterService;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClusterServiceImpl implements ClusterService {

    @Autowired
    private ClusterDao dao;

    @Override
    public ClusterHealth health() {
        ClusterHealthResponse response = dao.health();
        if (response == null) {
            throw new ButterflyException();
        }
        return new ClusterHealth().fromResponse(response);
    }
}
