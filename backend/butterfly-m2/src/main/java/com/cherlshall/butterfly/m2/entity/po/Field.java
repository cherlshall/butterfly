package com.cherlshall.butterfly.m2.entity.po;

import com.cherlshall.butterfly.common.validate.annotation.*;
import com.cherlshall.butterfly.sql.annotation.Invisible;
import lombok.Data;

/**
 * @author hu.tengfei
 * @date 2020/1/7
 */
@Data
public class Field {

    @SetNull
    private Integer id;

    @Required
    @Name("所属协议")
    private Integer protocolId; // 通过protocolId来查询父级category

    @Name("TYPE")
    @Required(where = {"1", "2"}) // 只有父级category为协议或tlv才有type
    @SetNull(where = "3")
    private Long type;

    @Trim
    @RangeLength
    @Name("中文名")
    private String cnName;

    @Trim
    @Required
    @RangeLength
    @Name("英文名")
    private String enName;

    @SetNull
    private Integer fieldCount;

    /**
     * 字段类型
     * string、int、long、float、double、binary、tlv、struct
     */
    @Among(value = {"string", "int", "long", "float", "double", "binary", "tlv", "struct"})
    @Name("字段类型")
    private String valueType;

    @Required
    @Name("字段大小")
    private Integer size;

    @Name("详见")
    @Required(where = {"valueType=tlv", "valueType=struct"})
    @SetNull(where = {"valueType=string", "valueType=int", "valueType=long", "valueType=float",
            "valueType=double", "valueType=binary"})
    private Integer link;

    @Name("wireshark名称")
    @RangeLength
    private String wiresharkName;

    @Name("wireshark过滤语法")
    @RangeLength(maxLength = 1024)
    private String wiresharkFilterSyntax;

    @Name("示例")
    @RangeLength(maxLength = 1024)
    private String example;

    @Name("备注")
    @RangeLength(maxLength = 1024)
    private String remark;

    /**
     * 是否正在使用 1: 正在使用 2: 废弃
     */
    @Name("正在使用")
    @Among({"1", "2"})
    private Integer active;

    @Invisible
    private String linkEnName;
}
