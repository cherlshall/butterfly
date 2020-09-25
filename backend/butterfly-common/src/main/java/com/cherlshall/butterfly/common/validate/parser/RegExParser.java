package com.cherlshall.butterfly.common.validate.parser;

import com.cherlshall.butterfly.common.validate.annotation.RegEx;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExParser implements RuleParser {
    @Override
    public boolean parse(Object object, Field field, Object value, String valueStr, Annotation annotation) {
        if (value == null) {
            return true;
        }
        RegEx regEx = (RegEx) annotation;
        String reg = regEx.value();
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(valueStr);
        return matcher.matches();
    }
}
