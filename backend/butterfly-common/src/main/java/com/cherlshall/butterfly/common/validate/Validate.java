package com.cherlshall.butterfly.common.validate;

import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.common.validate.annotation.*;
import com.cherlshall.butterfly.common.validate.parser.RuleParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class Validate {

    @SuppressWarnings("unchecked")
    private static Class<? extends Annotation>[] rules = new Class[]{ SetNull.class, Trim.class, Customize.class,
            Required.class, Among.class, RangeLength.class, RangeValue.class, RegEx.class };

    public static void check(Object checked) {
        check(checked, null);
    }

    public static void check(Object checked, String type) {
        if (checked == null) {
            throw new ButterflyException("参数不能为空");
        }
        if (type != null && type.isEmpty()) {
            type = null;
        }
        Class<?> aClass = checked.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name;
            Name nameAnnotation = field.getAnnotation(Name.class);
            name = nameAnnotation == null ? field.getName() : nameAnnotation.value();
            Object value = null;
            try {
                value = field.get(checked);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            String valueStr = toStr(value);
            for (Class<? extends Annotation> ruleClz : rules) {
                Annotation rule = field.getAnnotation(ruleClz);
                if (shouldParse(checked, aClass, rule, type)) {
                    parse(checked, field, rule, name, value, valueStr);
                }
            }
        }
    }

    private static boolean shouldParse(Object checked, Class<?> aClass, Annotation rule, String type) {
        if (rule == null) {
            return false;
        }
        Class<? extends Annotation> ruleClass = rule.getClass();
        try {
            Method where = ruleClass.getMethod("where");
            String[] conditions = (String[]) where.invoke(rule);
            if (conditions.length == 0) {
                return true;
            }
            // 外层为或 只要符合一个就返回true
            for (String condition : conditions) {
                // 内层为且 全部符合返回true
                String[] cons = condition.split(",");
                boolean shouldParse = true; // 内层是否全部符合
                for (String con : cons) {
                    if (con.contains("=")) {
                        boolean notEq = con.contains("!=");
                        String[] split = condition.split(notEq ? "!=" : "=");
                        try {
                            Field field = aClass.getField(split[0]);
                            field.setAccessible(true);
                            Object value = field.get(checked);
                            // 只要有一个不符合，就设置为false
                            if (Objects.equals(String.valueOf(value), split[1]) == notEq) {
                                shouldParse = false;
                                break;
                            }
                        } catch (NoSuchFieldException e) {
                            shouldParse = false;
                            break;
                        }
                    } else {
                        if (!con.equals(type)) {
                            shouldParse = false;
                            break;
                        }
                    }
                }
                if (shouldParse) {
                    return true;
                }
            }
            // 外层没有一个能符合的
            return false;
        } catch (NoSuchMethodException ignored) {
            // 对于没有where参数的，则应该执行parse
            return true;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void parse(Object object, Field field, Annotation rule, String name, Object value, String valueStr) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends RuleParser>[] parsers = (Class<? extends RuleParser>[]) rule.getClass()
                    .getMethod("parser").invoke(rule);
            for (Class<? extends RuleParser> parser : parsers) {
                if (parser != null && !parser.newInstance().parse(object, field, value, valueStr, rule)) {
                    String message = null;
                    try {
                        message = parseMessage(rule, name);
                    } catch (NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    throw new ButterflyException(message);
                }
            }
        } catch (ReflectiveOperationException ignored) {
        }
    }

    private static String toStr(Object value) {
        return value == null ? "" : value.toString();
    }

    private static String parseMessage(Annotation annotation, String name)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<? extends Annotation> clazz = annotation.getClass();
        String message = (String) clazz.getMethod("message").invoke(annotation);
        message = message.replaceAll("\\{name}", name);
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (message.contains("{" + methodName + "}")) {
                Object value = method.invoke(annotation);
                String valueStr;
                if (value.getClass().isArray()) {
                    Object[] arr = (Object[]) value;
                    valueStr = Arrays.toString(arr);
                } else {
                    valueStr = value.toString();
                }
                message = message.replaceAll("\\{" + methodName + "}", valueStr);
            }
        }
        return message;
    }
}
