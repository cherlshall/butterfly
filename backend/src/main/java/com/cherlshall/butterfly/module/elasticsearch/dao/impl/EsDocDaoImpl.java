package com.cherlshall.butterfly.module.elasticsearch.dao.impl;

import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.module.elasticsearch.dao.EsDocDao;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Repository
public class EsDocDaoImpl implements EsDocDao {

    @Autowired
    private RestHighLevelClient client;


    @Override
    public int insert(String indexName, Map<String, Object> data) {
        IndexRequest request = new IndexRequest(indexName).source(data);
        request.timeout(TimeValue.timeValueSeconds(1));
//        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
//        request.version(2);
//        request.versionType(VersionType.EXTERNAL);
        request.opType(DocWriteRequest.OpType.CREATE);
//        request.setPipeline("pipeline");
        try {
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
//            if (response.getResult() == DocWriteResponse.Result.CREATED) {
//                return 1;
//            }
            return shardInfo.getSuccessful();
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {
                throw new ButterflyException("document with same index and id already existed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int insert(String indexName, List<Map<String, Object>> data) {
        return 0;
    }

    @Override
    public int delete(String indexName, String id) {
        DeleteRequest request = new DeleteRequest(indexName, id);
        request.timeout(TimeValue.timeValueMinutes(2));
//        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        try {
            DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
            ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
            return shardInfo.getSuccessful();
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                throw new ButterflyException("document with same index and id already existed");
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(String indexName, String id, Map<String, Object> data) {
        UpdateRequest request = new UpdateRequest(indexName, id).doc(data);
        request.timeout(TimeValue.timeValueSeconds(1));
        try {
            UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
            ReplicationResponse.ShardInfo shardInfo = updateResponse.getShardInfo();
            return shardInfo.getSuccessful();
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                throw new ButterflyException("document with same index and id already existed");
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
