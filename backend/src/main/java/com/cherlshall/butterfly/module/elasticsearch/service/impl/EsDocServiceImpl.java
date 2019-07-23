package com.cherlshall.butterfly.module.elasticsearch.service.impl;

import com.cherlshall.butterfly.module.elasticsearch.dao.EsDocDao;
import com.cherlshall.butterfly.module.elasticsearch.service.EsDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EsDocServiceImpl implements EsDocService {

    @Autowired
    private EsDocDao dao;

    @Override
    public int insert(String indexName, Map<String, Object> data) {
        return dao.insert(indexName, data);
    }

    @Override
    public int insert(String indexName, List<Map<String, Object>> data) {
        return dao.insert(indexName, data);
    }

    @Override
    public int delete(String indexName, String id) {
        return dao.delete(indexName, id);
    }

    @Override
    public int update(String indexName, String id, Map<String, Object> data) {
        return dao.update(indexName, id, data);
    }
}
