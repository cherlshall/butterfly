package com.cherlshall.butterfly.elasticsearch.dao.impl;

import com.cherlshall.butterfly.elasticsearch.dao.EsClusterDao;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class EsClusterDaoImpl implements EsClusterDao {

    @Autowired
    RestHighLevelClient client;

    @Override
    public ClusterHealthResponse health() {
        // There are no required parameters.
        // By default, the client will check all indices and will not wait for any events.
        ClusterHealthRequest request = new ClusterHealthRequest();

        // Timeout for the request as a TimeValue. Defaults to 30 seconds
        request.timeout(TimeValue.timeValueSeconds(50));

        // Timeout to connect to the master node as a TimeValue. Defaults to the same as timeout
        request.masterNodeTimeout(TimeValue.timeValueSeconds(20));

        // The level of detail of the returned health information. Default value is cluster.
        request.level(ClusterHealthRequest.Level.INDICES);

        // The status to wait (e.g. green, yellow, or red)
//        request.waitForStatus(ClusterHealthStatus.RED);

        try {
            return client.cluster().health(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
