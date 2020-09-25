package com.cherlshall.butterfly.common.validate.parser;

import com.cherlshall.butterfly.common.validate.annotation.RangeLength;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class RangeLengthParser implements RuleParser {
    @Override
    public boolean parse(Object object, Field field, Object value, String valueStr, Annotation annotation) {
        if (value == null) {
            return true;
        }
        RangeLength rangeLength = (RangeLength) annotation;
        int max = rangeLength.maxLength();
        int min = rangeLength.minLength();
        return valueStr.length() <= max && valueStr.length() >= min;
    }
}
