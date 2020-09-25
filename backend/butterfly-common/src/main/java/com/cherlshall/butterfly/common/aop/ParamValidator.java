package com.cherlshall.butterfly.common.aop;

import com.cherlshall.butterfly.common.validate.Validate;
import com.cherlshall.butterfly.common.validate.Verify;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 自动校验参数
 * Created by htf on 2020/9/24.
 */
@Component
@Aspect
@Order(0)
public class ParamValidator {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void mappingPointCut() {

    }

    @Before("mappingPointCut()")
    public void validateParams(JoinPoint jp) {
        Object[] args = jp.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Object arg = args[i];
            Annotation[] annotations = parameterAnnotations[i];
            for (Annotation annotation : annotations) {
                if (annotation instanceof Verify) {
                    Validate.check(arg, ((Verify) annotation).value());
                    break;
                }
            }
        }
    }
}
