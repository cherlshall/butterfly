package com.cherlshall.butterfly.hdfs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cherlshall.butterfly.common.vo.PageData;
import com.cherlshall.butterfly.common.vo.PageParam;
import com.cherlshall.butterfly.common.vo.TableData;
import com.cherlshall.butterfly.hdfs.dao.ContentDao;
import com.cherlshall.butterfly.hdfs.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentDao dao;

    @Override
    public boolean write(String path, String content) {
        return dao.write(path, content);
    }

    @Override
    public String read(String path) {
        return dao.read(path);
    }

    @Override
    public TableData<JSONObject> readToTable(String path, PageParam params) {
        PageData<String> pageData = dao.readLine(path,
                params.getStartIndexWithDefault(),
                params.getPageSizeWithDefault());
        TableData<JSONObject> tableData = new TableData<>();
        List<JSONObject> dataSource = new ArrayList<>();
        Set<String> fieldNames = new HashSet<>();
        for (String line : pageData.getList()) {
            JSONObject jsonObject = JSONObject.parseObject(line);
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                fieldNames.add(entry.getKey());
                entry.setValue(entry.getValue().toString());
            }
            dataSource.add(jsonObject);
        }
        tableData.setColumns(new ArrayList<>(fieldNames));
        tableData.setTotal(pageData.getTotal());
        tableData.setList(dataSource);
        return tableData;
    }

    @Override
    public PageData<String> readToJson(String path, PageParam params) {
        return dao.readLine(path,
                params.getStartIndexWithDefault(),
                params.getPageSizeWithDefault());
    }

}
