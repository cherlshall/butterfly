package com.cherlshall.butterfly.m2.entity.po;

import com.cherlshall.butterfly.sql.annotation.UpdateSet;
import com.cherlshall.butterfly.sql.annotation.UpdateWhere;
import lombok.Data;

@Data
@UpdateSet
public class Type {

    @UpdateWhere
    private Integer id;

    private String cnName;

    private String enName;

    private Integer displayOrder;

    private Integer active;
}
