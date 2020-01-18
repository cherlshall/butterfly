package com.cherlshall.butterfly.sql.entity;

import com.cherlshall.butterfly.sql.annotation.Invisible;
import com.google.common.base.CaseFormat;
import lombok.Data;

@Data
public class ParamsVO {
    @Invisible
    private int currentPage = 1;
    @Invisible
    private int pageSize = 10;
    @Invisible
    private String orderName;
    @Invisible
    private String orderDirection;

    public String getOrderName() {
        if (orderName != null) {
            return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.orderName);
        }
        return null;
    }

    public int getStartIndex() {
        return (this.currentPage - 1) * this.pageSize;
    }
}
