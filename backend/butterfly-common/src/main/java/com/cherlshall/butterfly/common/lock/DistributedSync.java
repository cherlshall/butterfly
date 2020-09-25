package com.cherlshall.butterfly.common.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于 spring aop 在分布式环境下同步方法
 * Created by htf on 2020/9/24.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedSync {

    String[] value();

    /**
     * 过期时间
     * 防止程序因宕机等原因未能执行 unlock() 导致死锁
     */
    int expire() default 300_000;

    /**
     * 获取锁时的超时时间
     * @return 设置为负数取消超时，不建议，防止死锁
     */
    int timeout() default 10_000;

    /**
     * 自动续约
     */
    boolean renewal() default false;
}
