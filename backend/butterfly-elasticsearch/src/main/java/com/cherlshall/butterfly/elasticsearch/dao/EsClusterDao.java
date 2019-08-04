package com.cherlshall.butterfly.elasticsearch.dao;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;

public interface EsClusterDao {

    ClusterHealthResponse health();
}
