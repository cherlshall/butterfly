package com.cherlshall.butterfly.common.validate.annotation;

import com.cherlshall.butterfly.common.validate.parser.RangeValueParser;
import com.cherlshall.butterfly.common.validate.parser.RuleParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数值范围
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RangeValue {

    /**
     * 最大值
     */
    long maxValue() default Long.MAX_VALUE;

    /**
     * 最小值
     */
    long minValue() default Long.MIN_VALUE;

    /**
     * 字段校验失败时的msg
     */
    String message() default "{name}大小应该在{minValue}到{maxValue}之间";

    /**
     * 仅匹配到时进行验证
     */
    String[] where() default {};

    /**
     * 指定规则解析器
     */
    Class<? extends RuleParser>[] parser() default RangeValueParser.class;
}
