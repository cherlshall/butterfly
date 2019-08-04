package com.cherlshall.butterfly.hdfs.service;

import com.cherlshall.butterfly.hdfs.entity.HdfsBean;

import java.util.List;

public interface HdfsFileService {
    String[] listFileName(String parent);

    List<HdfsBean> listFileDetail(String parent);

    boolean create(String path);

    boolean delete(String path);

    boolean exists(String path);

    boolean mkdirs(String path);
}
