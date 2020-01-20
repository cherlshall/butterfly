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

    /**
     * 字段类型
     * string、int、long、byte、tlv、extractor
     */
    private String valueType;

    private Integer size;
    private Integer link;
    private String wiresharkName;
    private String wiresharkFilterSyntax;
    private String example;
    private String remark;

    /**
     * 是否正在使用 1: 正在使用 2: 废弃
     */
    private Integer active;

    @Invisible
    private String linkEnName;
}
