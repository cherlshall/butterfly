package com.cherlshall.butterfly.module.hbase.entity;

import lombok.Data;

import java.util.List;

@Data
public class TableNameBean {
    private List<String> tableNames;
}
