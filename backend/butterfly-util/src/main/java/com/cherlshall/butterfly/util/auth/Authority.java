package com.cherlshall.butterfly.util.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 定义所需权限
 * 标注于类时，作用于类中所有方法
 * 标注于方法时，作用于本方法
 * Created by htf on 2020/9/24.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Authority {

    Identity[] value();
}
