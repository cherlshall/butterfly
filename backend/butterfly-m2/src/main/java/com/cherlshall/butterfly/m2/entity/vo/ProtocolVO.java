package com.cherlshall.butterfly.m2.entity.vo;

import com.cherlshall.butterfly.sql.annotation.Symbol;
import com.cherlshall.butterfly.sql.annotation.UpdateSet;
import com.cherlshall.butterfly.sql.annotation.UpdateWhere;
import com.cherlshall.butterfly.sql.entity.ParamsVO;
import com.cherlshall.butterfly.sql.enums.SymbolEnum;
import lombok.Data;

@Data
@UpdateSet(nullEnable = true)
public class ProtocolVO extends ParamsVO {

    @UpdateWhere
    private Integer id;

    private Long type;
    private Integer protocolId;

    /**
     * 类型 1:协议 2:TLV 3:结构体
     */
    private Integer category;

    @Symbol(SymbolEnum.LIKE)
    private String cnName;

    @Symbol(SymbolEnum.LIKE)
    private String enName;

    @Symbol(SymbolEnum.LIKE)
    private String description;

    private Integer active;
}
