package com.cherlshall.butterfly.m2.entity.po;

import com.cherlshall.butterfly.sql.annotation.Invisible;
import lombok.Data;

/**
 * @author hu.tengfei
 * @date 2020/1/7
 */
@Data
public class Field {
    private Integer id;
    private Integer protocolId;
    private Long type;
    private String cnName;
    private String enName;
    private Integer fieldCount;
    private String valueType;
    private Integer size;
    private Integer link;
    private String wiresharkName;
    private String wiresharkFilterSyntax;
    private String example;
    private String remark;

    @Invisible
    private String linkEnName;
}
