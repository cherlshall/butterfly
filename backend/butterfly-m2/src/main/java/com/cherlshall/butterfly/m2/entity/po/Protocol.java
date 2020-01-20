package com.cherlshall.butterfly.m2.entity.po;

import com.cherlshall.butterfly.sql.annotation.Invisible;
import lombok.Data;

/**
 * @author hu.tengfei
 * @date 2020/1/7
 */
@Data
public class Protocol {
    private Integer id;

    /**
     * 协议号
     */
    private Long type;

    /**
     * 所属协议
     */
    private Integer protocolId;

    /**
     * 类型 1:协议 2:TLV 3:结构体
     */
    private Integer category;

    private String cnName;
    private String enName;
    private String description;

    /**
     * 是否正在使用 1: 正在使用 2: 废弃
     */
    private Integer active;

    /**
     * 所属协议的英文名
     */
    @Invisible
    private String protocolEnName;
}
