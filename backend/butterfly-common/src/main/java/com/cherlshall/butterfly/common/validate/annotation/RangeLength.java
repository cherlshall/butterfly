package com.cherlshall.butterfly.common.validate.annotation;

import com.cherlshall.butterfly.common.validate.parser.RangeLengthParser;
import com.cherlshall.butterfly.common.validate.parser.RuleParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 长度范围
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RangeLength {

    /**
     * 最小长度
     */
    int minLength() default 0;

    /**
     * 最大长度
     */
    int maxLength() default 255;

    /**
     * 字段校验失败时的msg
     */
    String message() default "{name}长度应该在{minLength}到{maxLength}之间";

    /**
     * 仅匹配到时进行验证
     */
    String[] where() default {};

    /**
     * 指定规则解析器
     */
    Class<? extends RuleParser> parser() default RangeLengthParser.class;
}
