package com.cherlshall.butterfly.common.aop;

import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.common.lock.CallerKey;
import com.cherlshall.butterfly.common.lock.CallerKeyFactory;
import com.cherlshall.butterfly.common.lock.DistributedLock;
import com.cherlshall.butterfly.common.lock.DistributedSync;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 分布式环境下同步方法
 * Created by htf on 2020/9/25.
 */
@Component
@Aspect
@Order(3)
public class DistributedSynchronized {

    @Autowired
    private DistributedLock lock;

    @Pointcut("@annotation(com.cherlshall.butterfly.common.lock.DistributedSync)")
    public void syncPointCut() {

    }

    @Around("syncPointCut()")
    public Object syncMethod(ProceedingJoinPoint pjp) throws Throwable {
        CallerKey callerKey = CallerKeyFactory.produce();
        String[] lockedKey = null;
        Object proceed = null;
        Throwable throwable = null;
        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            DistributedSync distributedSync = method.getAnnotation(DistributedSync.class);
            String[] value = distributedSync.value();
            lockedKey = new String[value.length];
            int timeout = distributedSync.timeout();
            for (int i = 0; i < value.length; i++) {
                if (timeout > 0) {
                    lock.lock(value[i], callerKey, distributedSync.expire(), timeout);
                } else {
                    lock.lock(value[i], callerKey, distributedSync.expire());
                }
                lockedKey[i] = value[i];
            }
            proceed = pjp.proceed();
        } catch (Throwable t) {
            throwable = t;
        }
        boolean unlockFail = false;
        if (lockedKey != null) {
            for (String key : lockedKey) {
                if (key != null) {
                    unlockFail |= !lock.unlock(key, callerKey);
                }
            }
        }
        if (throwable != null) {
            throw throwable;
        }
        if (unlockFail) {
            throw new ButterflyException("系统繁忙，请稍后再试");
        }
        return proceed;
    }
}
