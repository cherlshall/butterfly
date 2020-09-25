package com.cherlshall.butterfly.common.conf;

import com.cherlshall.butterfly.common.CookieUtil;
import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.common.vo.Code;
import com.cherlshall.butterfly.util.auth.Authority;
import com.cherlshall.butterfly.util.auth.Identity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by htf on 2020/9/24.
 */
@Component
public class AuthorityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Authority authAnnotation = handlerMethod.getMethodAnnotation(Authority.class);
            if (authAnnotation == null) {
                authAnnotation = handlerMethod.getBeanType().getAnnotation(Authority.class);
            }
            if (authAnnotation == null || authAnnotation.value().length == 0) {
                return true;
            }
            String[] currentAuths = CookieUtil.getAuth(request.getCookies());
            if (!permitPass(currentAuths, authAnnotation.value())) {
//                response.sendError(HttpStatus.FORBIDDEN.value());
                throw new ButterflyException(Code.UNAUTH);
            }
        }
        return false;
    }

    private boolean permitPass(String[] currentAuths, Identity... authConf) {
        if (currentAuths == null) {
            return authConf == null || authConf.length == 0;
        }
        for (Identity identity : authConf) {
            for (String currentAuth : currentAuths) {
                if (identity.permit(currentAuth)) {
                    return true;
                }
            }
        }
        return false;
    }
}
