package com.cherlshall.butterfly.elasticsearch.service;

import com.cherlshall.butterfly.elasticsearch.entity.ClusterHealth;

public interface ClusterService {
    ClusterHealth health();
}
