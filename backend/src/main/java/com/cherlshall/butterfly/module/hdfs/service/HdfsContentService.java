package com.cherlshall.butterfly.module.hdfs.service;

import com.cherlshall.butterfly.common.vo.ParamsVO;
import com.cherlshall.butterfly.common.vo.TableData;

public interface HdfsContentService {

    boolean write(String path, String content);

    String read(String path);

    TableData readToJson(String path, ParamsVO params);
}
