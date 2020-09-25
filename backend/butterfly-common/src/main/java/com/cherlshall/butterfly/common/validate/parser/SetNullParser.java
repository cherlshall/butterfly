package com.cherlshall.butterfly.common.validate.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class SetNullParser implements RuleParser {
    @Override
    public boolean parse(Object object, Field field, Object value, String valueStr, Annotation annotation) {
        if (value == null) {
            return true;
        }
        try {
            field.set(object, null);
        } catch (IllegalAccessException ignored) {
        }
        return true;
    }
}
