package com.cherlshall.butterfly.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageData<T> {
    private long total;
    private List<T> dataSource;
}
