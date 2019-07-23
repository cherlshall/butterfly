package com.cherlshall.butterfly.module.elasticsearch.service;

import java.util.Map;

public interface EsIndexService {

    void create(String indexName, Map<String, String> settings, Map<String, String> properties);

    void delete(String indexName);

    String[] list();
}
