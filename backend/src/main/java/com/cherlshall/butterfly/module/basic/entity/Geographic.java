package com.cherlshall.butterfly.module.basic.entity;

import lombok.Data;

@Data
public class Geographic {
    private String key;
    private String label;

    public Geographic(String key, String label) {
        this.key = key;
        this.label = label;
    }
}
