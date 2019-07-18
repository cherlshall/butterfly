package com.cherlshall.butterfly.entity.hbase;

import lombok.Data;

import java.util.List;

@Data
public class HTableSimple {
    private String tableName;
    private List<String> families;
}
