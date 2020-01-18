package com.cherlshall.butterfly.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageData<T> {
    private long total;
    private List<T> list;

    public PageData() {

    }

    public PageData(long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public PageData(List<T> list, long total) {
        this.total = total;
        this.list = list;
    }
}
