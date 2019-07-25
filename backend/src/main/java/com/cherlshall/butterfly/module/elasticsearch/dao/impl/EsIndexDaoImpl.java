package com.cherlshall.butterfly.module.elasticsearch.dao.impl;

import com.cherlshall.butterfly.module.elasticsearch.dao.EsIndexDao;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Map;

@Repository
public class EsIndexDaoImpl implements EsIndexDao {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public boolean createIndex(String indexName, Map<String, String> settings, Map<String, String> properties) {
        try {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            if (settings != null && settings.size() > 0) {
                Settings.Builder builder = Settings.builder().putProperties(settings, key -> key);
                request.settings(builder);
            }
            if (properties != null && properties.size() > 0) {
                StringBuilder mapping = new StringBuilder("{\"properties\":{");
                boolean notFirstProperty = false;
                for(Map.Entry<String, String> property : properties.entrySet()) {
                    if (notFirstProperty) {
                        mapping.append(",");
                    }
                    notFirstProperty = true;
                    mapping.append("\"").append(property.getKey()).append("\":{\"type\":\"")
                            .append(property.getValue()).append("\"}");
                }
                mapping.append("}}");
                request.mapping(mapping.toString(), XContentType.JSON);
            }
            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteIndex(String indexName) {
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String[] list() {
        try {
            return client.indices().get(new GetIndexRequest("*"), RequestOptions.DEFAULT).getIndices();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, Object> mapping(String indexName) {
        GetMappingsRequest request = new GetMappingsRequest();
        request.indices(indexName);
        request.setMasterTimeout(TimeValue.timeValueMinutes(1));
//        request.indicesOptions(IndicesOptions.lenientExpandOpen());
        try {
            GetMappingsResponse response = client.indices().getMapping(request, RequestOptions.DEFAULT);
            Map<String, MappingMetaData> mappings = response.mappings();
            MappingMetaData metaData = mappings.get(indexName);
            return metaData.sourceAsMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
