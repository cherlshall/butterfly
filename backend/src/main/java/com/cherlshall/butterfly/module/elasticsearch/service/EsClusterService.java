package com.cherlshall.butterfly.module.elasticsearch.service;

import com.cherlshall.butterfly.module.elasticsearch.entity.ClusterHealth;

public interface EsClusterService {
    ClusterHealth health();
}
