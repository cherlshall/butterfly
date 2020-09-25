package com.cherlshall.butterfly.common.validate.annotation;

import com.cherlshall.butterfly.common.validate.parser.RuleParser;
import com.cherlshall.butterfly.common.validate.parser.TrimParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 会对String类型的字段进行trim
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Trim {

    /**
     * 仅匹配到时进行验证
     */
    String[] where() default {};

    /**
     * 指定规则解析器
     */
    Class<? extends RuleParser>[] parser() default TrimParser.class;
}
