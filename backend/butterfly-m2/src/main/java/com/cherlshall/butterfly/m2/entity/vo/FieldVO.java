package com.cherlshall.butterfly.m2.entity.vo;

import com.cherlshall.butterfly.sql.annotation.Symbol;
import com.cherlshall.butterfly.sql.annotation.UpdateSet;
import com.cherlshall.butterfly.sql.annotation.UpdateWhere;
import com.cherlshall.butterfly.sql.entity.ParamsVO;
import com.cherlshall.butterfly.sql.enums.SymbolEnum;
import lombok.Data;

@Data
@UpdateSet(nullEnable = true)
public class FieldVO extends ParamsVO {

    @UpdateWhere
    private Integer id;
    private Integer protocolId;
    private Long type;

    @Symbol(SymbolEnum.LIKE)
    private String cnName;

    @Symbol(SymbolEnum.LIKE)
    private String enName;

    private Integer fieldCount;
    private String valueType;
    private Integer size;
    private Integer link;

    @Symbol(SymbolEnum.LIKE)
    private String wiresharkName;

    @Symbol(SymbolEnum.LIKE)
    private String wiresharkFilterSyntax;

    @Symbol(SymbolEnum.LIKE)
    private String example;

    @Symbol(SymbolEnum.LIKE)
    private String remark;

    private Integer active;
}
