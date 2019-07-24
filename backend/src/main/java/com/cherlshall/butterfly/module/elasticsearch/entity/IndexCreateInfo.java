package com.cherlshall.butterfly.module.elasticsearch.entity;

import lombok.Data;

import java.util.Map;

@Data
public class IndexCreateInfo {
    private String indexName;
    private Map<String, String> settings;
    private Map<String, String> properties;
}
