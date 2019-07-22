package com.cherlshall.butterfly.module.hbase.entity;

import lombok.Data;

import java.util.List;

@Data
public class FamilyChange {
    private List<String> remove;
    private List<String> addition;
}
