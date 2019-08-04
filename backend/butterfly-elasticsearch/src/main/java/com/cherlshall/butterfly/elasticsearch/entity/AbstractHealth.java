package com.cherlshall.butterfly.elasticsearch.entity;

import lombok.Data;

@Data
public abstract class AbstractHealth {
    // 0: green 1: yellow 2: red
    private int status;
    private int activeShardNum;
    private int unassignedShardNum;
    private int activeShardPercent;
}
