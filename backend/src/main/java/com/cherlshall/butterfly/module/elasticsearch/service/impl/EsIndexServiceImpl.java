package com.cherlshall.butterfly.module.elasticsearch.service.impl;

import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.module.elasticsearch.dao.EsIndexDao;
import com.cherlshall.butterfly.module.elasticsearch.service.EsIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EsIndexServiceImpl implements EsIndexService {

    @Autowired
    private EsIndexDao dao;

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
}
