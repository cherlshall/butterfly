package com.cherlshall.butterfly.module.hbase.entity;

import lombok.Data;

import java.util.List;

@Data
public class HTableDetail {

    private String tableName;
    private int regionReplication;
    private boolean readOnly;
    private boolean disable;
    private List<String> families;
}
