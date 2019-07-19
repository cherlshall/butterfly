package com.cherlshall.butterfly.entity.hbase;

import lombok.Data;

@Data
public class HBaseBean {
    private String family;
    private String qualifier;
    private String value;

    public HBaseBean(String family, String qualifier, String value) {
        this.family = family;
        this.qualifier = qualifier;
        this.value = value;
    }
}
