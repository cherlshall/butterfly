package com.cherlshall.butterfly.module.elasticsearch.dao;

import java.util.Map;

public interface EsIndexDao {

    boolean createIndex(String indexName, Map<String, String> settings, Map<String, String> properties);

    boolean deleteIndex(String indexName);

    String[] list();
}
