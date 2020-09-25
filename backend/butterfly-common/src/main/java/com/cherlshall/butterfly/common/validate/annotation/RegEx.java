package com.cherlshall.butterfly.common.validate.annotation;

import com.cherlshall.butterfly.common.validate.parser.RegExParser;
import com.cherlshall.butterfly.common.validate.parser.RuleParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用正则匹配
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegEx {

    /**
     * 正则表达式
     */
    String value();

    /**
     * 字段校验失败时的msg
     */
    String message() default "{name}格式错误";

    /**
     * 仅匹配到时进行验证
     */
    String[] where() default {};

    /**
     * 指定规则解析器
     */
    Class<? extends RuleParser>[] parser() default RegExParser.class;
}
