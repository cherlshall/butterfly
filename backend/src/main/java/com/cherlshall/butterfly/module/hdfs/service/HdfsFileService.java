package com.cherlshall.butterfly.module.hdfs.service;

import com.cherlshall.butterfly.module.hdfs.entity.HdfsBean;

import java.util.List;

public interface HdfsFileService {
    String[] listFileName(String parent);

    List<HdfsBean> listFileDetail(String parent);

    boolean create(String path);

    boolean delete(String path);

    boolean exists(String path);

    boolean mkdirs(String path);
}
