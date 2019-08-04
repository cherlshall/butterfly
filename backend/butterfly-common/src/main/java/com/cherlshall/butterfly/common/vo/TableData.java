package com.cherlshall.butterfly.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class TableData<T> extends PageData<T> {
    private List<String> columns;
}
