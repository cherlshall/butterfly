package com.cherlshall.butterfly.elasticsearch.dao;

import java.util.Map;

public interface IndexDao {

    boolean createIndex(String indexName, Map<String, String> settings, Map<String, String> properties);

    boolean deleteIndex(String indexName);

    String[] list();

    Map<String, Object> mapping(String indexName);

}
