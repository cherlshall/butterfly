package com.cherlshall.butterfly.sql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记update时作为set参数的字段
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateSet {

    /**
     * 当字段为null时的策略
     * @return true: 更新为null false: 略过
     */
    boolean nullEnable() default false;
}
