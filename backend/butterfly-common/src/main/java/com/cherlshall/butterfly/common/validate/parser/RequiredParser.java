package com.cherlshall.butterfly.common.validate.parser;

import com.cherlshall.butterfly.common.validate.annotation.Required;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class RequiredParser implements RuleParser {
    @Override
    public boolean parse(Object object, Field field, Object value, String valueStr, Annotation annotation) {
        Required required = (Required) annotation;
        if (value == null) {
            return false;
        }
        return required.allowBlank() || !valueStr.trim().isEmpty();
    }
}
