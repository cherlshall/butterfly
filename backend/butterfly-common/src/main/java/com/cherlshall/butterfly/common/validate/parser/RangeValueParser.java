package com.cherlshall.butterfly.common.validate.parser;

import com.alibaba.fastjson.util.TypeUtils;
import com.cherlshall.butterfly.common.validate.annotation.RangeValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class RangeValueParser implements RuleParser {
    @Override
    public boolean parse(Object object, Field field, Object value, String valueStr, Annotation annotation) {
        if (value == null) {
            return true;
        }
        RangeValue rangeValue = (RangeValue) annotation;
        long max = rangeValue.maxValue();
        long min = rangeValue.minValue();
        Long valueLong = TypeUtils.castToLong(value);
        return valueLong > min && valueLong < max;
    }
}
