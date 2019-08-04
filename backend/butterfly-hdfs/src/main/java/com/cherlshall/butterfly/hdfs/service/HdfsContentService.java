package com.cherlshall.butterfly.hdfs.service;

import com.alibaba.fastjson.JSONObject;
import com.cherlshall.butterfly.common.vo.PageData;
import com.cherlshall.butterfly.common.vo.ParamsVO;
import com.cherlshall.butterfly.common.vo.TableData;

public interface HdfsContentService {

    boolean write(String path, String content);

    String read(String path);

    TableData<JSONObject> readToTable(String path, ParamsVO params);

    PageData<String> readToJson(String path, ParamsVO params);
}
