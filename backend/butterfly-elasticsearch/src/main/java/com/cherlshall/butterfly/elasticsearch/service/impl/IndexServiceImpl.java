package com.cherlshall.butterfly.elasticsearch.service.impl;

import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.elasticsearch.dao.IndexDao;
import com.cherlshall.butterfly.elasticsearch.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private IndexDao dao;

    @Override
    public void create(String indexName, Map<String, String> settings, Map<String, String> properties) {
        boolean createSuccess = dao.createIndex(indexName, settings, properties);
        if (!createSuccess) {
            throw new ButterflyException("create failure");
        }
    }

    @Override
    public void delete(String indexName) {
        boolean deleteSuccess = dao.deleteIndex(indexName);
        if (!deleteSuccess) {
            throw new ButterflyException("delete failure");
        }
    }

    @Override
    public String[] list() {
        String[] indices = dao.list();
        if (indices == null) {
            throw new ButterflyException("list failure");
        }
        return indices;
    }

    @Override
    public String[] properties(String indexName) {
        Map<String, Object> mapping = dao.mapping(indexName);
        if (mapping == null) {
            return new String[0];
        }
        Object properties = mapping.get("properties");
        if (properties == null) {
            return new String[0];
        }
        @SuppressWarnings("unchecked") Map<String, Object> propsMap = (Map<String, Object>) properties;
        String[] propNames = new String[propsMap.size()];
        int index = 0;
        for (Map.Entry<String, Object> entry : propsMap.entrySet()) {
            propNames[index++] = entry.getKey();
        }
        return propNames;
    }
}
