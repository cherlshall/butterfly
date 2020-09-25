package com.cherlshall.butterfly.common.validate.annotation;

import com.cherlshall.butterfly.common.validate.parser.RuleParser;
import com.cherlshall.butterfly.common.validate.parser.SetNullParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 会被强制赋空值
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SetNull {

    /**
     * 仅匹配到时进行验证
     */
    String[] where() default {};

    /**
     * 指定规则解析器
     */
    Class<? extends RuleParser>[] parser() default SetNullParser.class;
}
