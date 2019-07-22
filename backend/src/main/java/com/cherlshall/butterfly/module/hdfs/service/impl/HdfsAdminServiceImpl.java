package com.cherlshall.butterfly.module.hdfs.service.impl;

import com.cherlshall.butterfly.module.hdfs.dao.HdfsAdminDao;
import com.cherlshall.butterfly.module.hdfs.service.HdfsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HdfsAdminServiceImpl implements HdfsAdminService {
    @Autowired
    private HdfsAdminDao dao;
}
