package com.cherlshall.butterfly.common.validate.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface RuleParser {

    /**
     * 解析注解
     * @param object 原对象
     * @param field 当前解析的字段
     * @param value 当前解析字段的值
     * @param valueStr 当前解析字段值的字符串格式
     * @param annotation  当前匹配的注解
     * @return true: 通过 false: 为通过
     */
    boolean parse(Object object, Field field, Object value, String valueStr, Annotation annotation);
}
