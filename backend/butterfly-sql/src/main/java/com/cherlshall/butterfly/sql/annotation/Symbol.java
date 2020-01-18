package com.cherlshall.butterfly.sql.annotation;

import com.cherlshall.butterfly.sql.enums.SymbolEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 匹配模式
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Symbol {
    SymbolEnum value();
}
