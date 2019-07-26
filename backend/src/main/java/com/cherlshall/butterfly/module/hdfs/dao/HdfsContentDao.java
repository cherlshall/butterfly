package com.cherlshall.butterfly.module.hdfs.dao;

import com.cherlshall.butterfly.common.vo.PageData;

public interface HdfsContentDao {

    boolean write(String path, String content);

    String read(String path);

    PageData<String> readLine(String path, int startLine, int size);
}
