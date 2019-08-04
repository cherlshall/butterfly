package com.cherlshall.butterfly.hbase.entity;

import lombok.Data;

@Data
public class HBaseBeanAdv extends HBaseBean {

    private String rowKey;
    private Long timestamp;

    public HBaseBeanAdv(String rowKey, String family, String qualifier, String value, Long timestamp) {
        super(family, qualifier, value);
        this.rowKey = rowKey;
        this.timestamp = timestamp;
    }
}
