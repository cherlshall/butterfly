package com.cherlshall.butterfly.common.validate.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class TrimParser implements RuleParser {
    @Override
    public boolean parse(Object object, Field field, Object value, String valueStr, Annotation annotation) {
        try {
            field.set(object, valueStr.trim());
        } catch (IllegalAccessException ignored) {
        }
        return true;
    }
}
