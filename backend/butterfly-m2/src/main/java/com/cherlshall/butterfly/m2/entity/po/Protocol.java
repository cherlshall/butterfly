package com.cherlshall.butterfly.m2.entity.po;

import com.cherlshall.butterfly.common.validate.annotation.*;
import com.cherlshall.butterfly.sql.annotation.Invisible;
import lombok.Data;

/**
 * @author hu.tengfei
 * @date 2020/1/7
 */
@Data
public class Protocol {

    @SetNull
    private Integer id;

    /**
     * 协议号
     */
    @Required(where = "category=1")
    @Name("TYPE")
    @SetNull(where = {"category=2", "category=3"})
    private Long type;

    /**
     * 所属协议
     */
    @Required(where = {"category=2", "category=3"})
    @Name("所属协议")
    @SetNull(where = "category=1")
    private Integer protocolId;

    /**
     * 类型 1:协议 2:TLV 3:结构体
     */
    @Required
    @Among(value = {"1", "2", "3"}, message = "{name}可选择值为协议、TLV或结构体")
    @Name("协议类型")
    private Integer category;

    @Trim
    @RangeLength
    @Name("中文名")
    private String cnName;

    @Trim
    @Required
    @RangeLength
    @Name("英文名")
    private String enName;

    @Trim
    @RangeLength
    @Name("协议描述")
    private String description;

    /**
     * 是否正在使用 1: 正在使用 2: 废弃
     */
    @Among({"1", "2"})
    @Name("正在使用")
    private Integer active;

    /**
     * 所属协议的英文名
     */
    @Invisible
    private String protocolEnName;
}
