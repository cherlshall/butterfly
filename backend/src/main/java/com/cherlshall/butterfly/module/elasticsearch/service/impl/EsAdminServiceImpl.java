package com.cherlshall.butterfly.module.elasticsearch.service.impl;

import com.cherlshall.butterfly.module.elasticsearch.dao.EsAdminDao;
import com.cherlshall.butterfly.module.elasticsearch.service.EsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EsAdminServiceImpl implements EsAdminService {
    @Autowired
    private EsAdminDao dao;
}
