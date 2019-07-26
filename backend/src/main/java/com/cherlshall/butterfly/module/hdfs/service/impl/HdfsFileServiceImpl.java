package com.cherlshall.butterfly.module.hdfs.service.impl;

import com.cherlshall.butterfly.module.hdfs.dao.HdfsFileDao;
import com.cherlshall.butterfly.module.hdfs.entity.HdfsBean;
import com.cherlshall.butterfly.module.hdfs.service.HdfsFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HdfsFileServiceImpl implements HdfsFileService {
    @Autowired
    private HdfsFileDao dao;

    @Override
    public String[] listFileName(String parent) {
        return dao.listFileName(parent);
    }

    @Override
    public List<HdfsBean> listFileDetail(String parent) {
        return dao.listFileDetail(parent);
    }

    @Override
    public boolean create(String path) {
        return dao.create(path);
    }

    @Override
    public boolean delete(String path) {
        return dao.delete(path);
    }

    @Override
    public boolean exists(String path) {
        return dao.exists(path);
    }

    @Override
    public boolean mkdirs(String path) {
        return dao.mkdirs(path);
    }
}
