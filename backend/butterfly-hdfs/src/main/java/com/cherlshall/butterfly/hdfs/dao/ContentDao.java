package com.cherlshall.butterfly.hdfs.dao;

import com.cherlshall.butterfly.common.vo.PageData;

public interface ContentDao {

    boolean write(String path, String content);

    String read(String path);

    PageData<String> readLine(String path, int startLine, int size);
}
