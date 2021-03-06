package com.cherlshall.butterfly.elasticsearch.service;

import com.cherlshall.butterfly.common.vo.PageData;
import com.cherlshall.butterfly.common.vo.PageParam;

import java.util.List;
import java.util.Map;

public interface DocService {

    int insert(String indexName, Map<String, Object> data);

    int insert(String indexName, List<Map<String, Object>> data);

    int delete(String indexName, String id);

    int update(String indexName, String id, Map<String, Object> data);

    PageData<Map<String, Object>> selectByPage(String indexName, PageParam params);
}
