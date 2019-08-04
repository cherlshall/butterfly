package com.cherlshall.butterfly.elasticsearch.dao.impl;

import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.common.vo.PageData;
import com.cherlshall.butterfly.elasticsearch.dao.EsDocDao;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class EsDocDaoImpl implements EsDocDao {

    @Autowired
    private RestHighLevelClient client;


    @Override
    public int insert(String indexName, Map<String, Object> data) {
        IndexRequest request = new IndexRequest(indexName);
        request.timeout(TimeValue.timeValueSeconds(1));
//        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
//        request.version(2);
//        request.versionType(VersionType.EXTERNAL);
        String id = (String) data.remove("_id");
        if (id != null && !id.isEmpty()) {
//            request.opType(DocWriteRequest.OpType.CREATE);
            request.id(id);
        }
        request.source(data);
//        request.setPipeline("pipeline");
        try {
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
//            if (response.getResult() == DocWriteResponse.Result.CREATED) {
//                return 1;
//            }
            return shardInfo.getSuccessful();
        } catch (ElasticsearchException e) {
            e.printStackTrace();
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

    @Override
    public PageData<Map<String, Object>> selectByPage(String indexName, int from, int size,
                                 String orderName, Boolean orderAsc) {
        SearchRequest request = new SearchRequest(indexName);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.from(from);
        builder.size(size);
        if (orderName != null) {
            if (orderAsc != null) {
                builder.sort(orderName, orderAsc ? SortOrder.ASC : SortOrder.DESC);
            } else {
                builder.sort(orderName);
            }
        }
        request.source(builder);
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            PageData<Map<String, Object>> pageData = new PageData<>();
            List<Map<String, Object>> sourceList = new ArrayList<>();
            pageData.setDataSource(sourceList);
            for (SearchHit hit : hits) {
                Map<String, Object> source = hit.getSourceAsMap();
                String id = hit.getId();
                source.put("_id", id);
                sourceList.add(source);
            }
            TotalHits totalHits = hits.getTotalHits();
            pageData.setTotal(totalHits.value);
            return pageData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
