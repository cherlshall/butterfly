package com.cherlshall.butterfly.module.hbase.entity;

import lombok.Data;

import java.util.List;

@Data
public class HTableSimple {
    private String tableName;
    private List<String> families;
}
