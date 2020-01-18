package com.cherlshall.butterfly.hdfs.service.impl;

import com.cherlshall.butterfly.hdfs.dao.FileDao;
import com.cherlshall.butterfly.hdfs.entity.HdfsBean;
import com.cherlshall.butterfly.hdfs.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileDao dao;

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
