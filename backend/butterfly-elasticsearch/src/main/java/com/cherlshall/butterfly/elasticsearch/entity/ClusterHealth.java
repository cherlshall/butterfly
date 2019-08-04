package com.cherlshall.butterfly.elasticsearch.entity;

import lombok.Data;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.cluster.health.ClusterIndexHealth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ClusterHealth extends AbstractHealth {

    private String clusterName;
    private int nodeNum;
    private int dataNodeNum;
    private List<IndexHealth> indexHealth;

    public ClusterHealth fromResponse(ClusterHealthResponse response) {

        this.clusterName = response.getClusterName();
        this.nodeNum = response.getNumberOfNodes();
        this.setStatus(response.getStatus().value());
        this.setActiveShardNum(response.getActiveShards());
        this.setUnassignedShardNum(response.getUnassignedShards());
        this.setActiveShardPercent((int) response.getActiveShardsPercent());
        this.setDataNodeNum(response.getNumberOfDataNodes());

        Map<String, ClusterIndexHealth> indices = response.getIndices();
        this.indexHealth = new ArrayList<>(indices.size());
        for (Map.Entry<String, ClusterIndexHealth> index : indices.entrySet()) {
            this.indexHealth.add(new IndexHealth().fromResponse(index.getValue()));
        }
        return this;
    }

}
