package com.cherlshall.butterfly.elasticsearch.entity;

import lombok.Data;
import org.elasticsearch.cluster.health.ClusterIndexHealth;

@Data
public class IndexHealth extends AbstractHealth {
    private String indexName;
    private int replicaNum;

    public IndexHealth fromResponse(ClusterIndexHealth index) {
        this.indexName = index.getIndex();
        this.replicaNum = index.getNumberOfReplicas();
        this.setStatus(index.getStatus().value());
        int activeShards = index.getActiveShards();
        int unassignedShards = index.getUnassignedShards();
        this.setActiveShardNum(activeShards);
        this.setUnassignedShardNum(unassignedShards);
        if (unassignedShards == 0) {
            this.setActiveShardPercent(100);
        } else {
            this.setActiveShardPercent(activeShards * 100 / (activeShards + unassignedShards));
        }
        return this;
    }

}
