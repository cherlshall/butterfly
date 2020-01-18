package com.cherlshall.butterfly.hdfs.service;

import com.alibaba.fastjson.JSONObject;
import com.cherlshall.butterfly.common.vo.PageData;
import com.cherlshall.butterfly.common.vo.PageParam;
import com.cherlshall.butterfly.common.vo.TableData;

public interface ContentService {

    boolean write(String path, String content);

    String read(String path);

    TableData<JSONObject> readToTable(String path, PageParam params);

    PageData<String> readToJson(String path, PageParam params);
}
