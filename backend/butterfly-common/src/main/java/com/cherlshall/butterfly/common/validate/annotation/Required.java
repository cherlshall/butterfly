package com.cherlshall.butterfly.common.validate.annotation;

import com.cherlshall.butterfly.common.validate.parser.RequiredParser;
import com.cherlshall.butterfly.common.validate.parser.RuleParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否必须
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Required {

    /**
     * 是否允许全部为空白
     */
    boolean allowBlank() default false;

    /**
     * 字段校验失败时的msg
     */
    String message() default "{name}不能为空";

    /**
     * 仅匹配到时进行验证
     */
    String[] where() default {};

    /**
     * 指定规则解析器
     */
    Class<? extends RuleParser> parser() default RequiredParser.class;
}
