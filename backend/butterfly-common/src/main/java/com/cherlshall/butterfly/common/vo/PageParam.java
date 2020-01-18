package com.cherlshall.butterfly.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.CaseFormat;
import lombok.Data;

@Data
public class PageParam {
    @JsonIgnore // 返回response时忽略
    private Integer currentPage;
    @JsonIgnore
    private Integer pageSize;
    @JsonIgnore
    private String orderName;
    @JsonIgnore
    private String orderDirection;

    @JsonIgnore
    public Integer getStartIndex() {
        if (this.currentPage == null || this.pageSize == null)
            return null;
        return (this.currentPage - 1) * this.pageSize;
    }

    @JsonIgnore
    public int getStartIndexWithDefault() {
        if (currentPage == null) {
            return 0;
        }
        if (pageSize == null) {
            return (this.currentPage - 1) * 10;
        }
        return (this.currentPage - 1) * this.pageSize;
    }

    @JsonIgnore
    public int getPageSizeWithDefault() {
        return pageSize == null ? 10 : pageSize;
    }

    @JsonIgnore
    public String getOrderName() {
        if (this.orderName == null)
            return null;
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.orderName);
    }

    @JsonIgnore
    public Boolean isOrderAsc() {
        if (orderDirection == null) {
            return null;
        }
        return orderDirection.equalsIgnoreCase("asc") ||
                orderDirection.equalsIgnoreCase("ascend");
    }

    @JsonIgnore
    public Boolean isOrderDesc() {
        if (orderDirection == null) {
            return null;
        }
        return orderDirection.equalsIgnoreCase("desc") ||
                orderDirection.equalsIgnoreCase("descend");
    }
}
