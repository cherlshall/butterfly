package com.cherlshall.butterfly.module.hdfs.service.impl;

import com.cherlshall.butterfly.module.hdfs.dao.HDFSAdminDao;
import com.cherlshall.butterfly.module.hdfs.service.HDFSAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HDFSAdminServiceImpl implements HDFSAdminService {
    @Autowired
    private HDFSAdminDao dao;
}
