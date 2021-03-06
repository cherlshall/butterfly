package com.cherlshall.butterfly.elasticsearch.dao;

import com.cherlshall.butterfly.common.vo.PageData;

import java.util.List;
import java.util.Map;

public interface DocDao {

    int insert(String indexName, Map<String, Object> data);

    int insert(String indexName, List<Map<String, Object>> data);

    int delete(String indexName, String id);

    int update(String indexName, String id, Map<String, Object> data);

    PageData<Map<String, Object>> selectByPage(String indexName, int from, int size,
                                               String orderName, Boolean orderAsc);

}
