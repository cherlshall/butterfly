package com.cherlshall.butterfly.elasticsearch.service;

import java.util.Map;

public interface EsIndexService {

    void create(String indexName, Map<String, String> settings, Map<String, String> properties);

    void delete(String indexName);

    String[] list();

    String[] properties(String indexName);
}
