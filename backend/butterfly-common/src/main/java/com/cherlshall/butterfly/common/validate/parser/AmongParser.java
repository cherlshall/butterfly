package com.cherlshall.butterfly.common.validate.parser;

import com.cherlshall.butterfly.common.validate.annotation.Among;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AmongParser implements RuleParser {
    @Override
    public boolean parse(Object object, Field field, Object value, String valueStr, Annotation annotation) {
        if (value == null) {
            return true;
        }
        Among among = (Among) annotation;
        String[] amongValues = among.value();
        for (String amongValue : amongValues) {
            if (amongValue.equals(valueStr)) {
                return true;
            }
        }
        return false;
    }
}
