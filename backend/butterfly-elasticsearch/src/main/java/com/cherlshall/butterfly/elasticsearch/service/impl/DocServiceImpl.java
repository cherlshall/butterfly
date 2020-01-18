package com.cherlshall.butterfly.elasticsearch.service.impl;

import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.common.vo.PageData;
import com.cherlshall.butterfly.common.vo.PageParam;
import com.cherlshall.butterfly.elasticsearch.dao.DocDao;
import com.cherlshall.butterfly.elasticsearch.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DocServiceImpl implements DocService {

    @Autowired
    private DocDao dao;

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

    @Override
    public PageData<Map<String, Object>> selectByPage(String indexName, PageParam params) {
        String orderDirection = params.getOrderDirection();
        Boolean asc;
        if (orderDirection == null) {
            asc = null;
        } else {
            asc = params.isOrderAsc();
        }
        PageData<Map<String, Object>> pageData = dao.selectByPage(indexName, params.getStartIndexWithDefault(),
                params.getPageSizeWithDefault(), params.getOrderName(), asc);
        if (pageData == null) {
            throw new ButterflyException();
        }
        return pageData;
    }
}
