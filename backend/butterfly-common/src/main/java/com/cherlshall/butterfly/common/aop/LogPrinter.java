package com.cherlshall.butterfly.common.aop;

import com.cherlshall.butterfly.common.CookieUtil;
import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.common.util.RequestUtil;
import com.cherlshall.butterfly.common.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by htf on 2020/9/25.
 */
@Slf4j
@Component
@Aspect
@Order(1)
public class LogPrinter {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void mappingPointCut() {

    }

    @Around("mappingPointCut()")
    public Object log4mapping(ProceedingJoinPoint pjp) {
        Object result;
        StringBuilder builder = new StringBuilder();
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            builder.append("Receive request : ").append(request.getMethod()).append(" ")
                    .append(request.getRequestURI()).append("\n")
                    .append("--------------------------------------------------------\n")
                    .append("IP     : ").append(RequestUtil.getRemoteHost(request)).append("\n")
                    .append("User   : ").append(CookieUtil.getUserName(request.getCookies())).append("\n")
                    .append("Class  : ").append(pjp.getTarget().getClass().getName()).append("\n")
                    .append("Method : ").append(((MethodSignature) pjp.getSignature()).getMethod().getName()).append("\n");
            Object[] args = pjp.getArgs();
            for (int i = 0; i < args.length; i++) {
                if (i == 0) {
                    builder.append("Params : ");
                } else {
                    builder.append("         ");
                }
                builder.append(args[i]).append("\n");
            }
            result = pjp.proceed();
            builder.append("Return : ").append(request).append("\n");
        } catch (ButterflyException e) {
            builder.append("Return : ").append(R.error(e.code(), e.msg())).append("\n");
            throw e;
        } catch (Throwable t) {
            builder.append("Exception : ").append(t.getMessage()).append("\n");
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            throw new RuntimeException(t);
        } finally {
            builder.append("--------------------------------------------------------");
            log.info(builder.toString());
        }
        return result;
    }
}
