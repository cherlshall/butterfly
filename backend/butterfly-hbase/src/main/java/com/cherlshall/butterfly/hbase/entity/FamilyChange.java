package com.cherlshall.butterfly.hbase.entity;

import lombok.Data;

import java.util.List;

@Data
public class FamilyChange {
    private List<String> remove;
    private List<String> addition;
}
