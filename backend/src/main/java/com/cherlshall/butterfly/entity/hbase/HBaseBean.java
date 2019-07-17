package com.cherlshall.butterfly.entity.hbase;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(of = "rowName")
public class HBaseBean {
    private String rowName;
    private String family;
    private String qualifier;
    private String value;

    public HBaseBean(String rowName, String family, String qualifier, String value) {
        this.rowName = rowName;
        this.family = family;
        this.qualifier = qualifier;
        this.value = value;
    }
}
