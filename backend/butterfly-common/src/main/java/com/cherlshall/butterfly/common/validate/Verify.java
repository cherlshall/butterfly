package com.cherlshall.butterfly.common.validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解于 controller 中需要验证的参数，aop 会自动验证参数
 * Created by htf on 2020/9/24.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Verify {

    /**
     * 用域配合验证注解的 where 字段
     * 详细解析规则见 {@link Validate}
     */
    String value() default "";
}
