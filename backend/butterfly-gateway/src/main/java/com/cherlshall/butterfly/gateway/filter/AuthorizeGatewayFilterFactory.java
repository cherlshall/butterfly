package com.cherlshall.butterfly.gateway.filter;

import com.cherlshall.butterfly.gateway.feign.AccountFeignClient;
import com.cherlshall.butterfly.util.auth.Identity;
import com.cherlshall.butterfly.util.entity.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by htf on 2019/8/5.
 */
@Component
public class AuthorizeGatewayFilterFactory extends
        AbstractGatewayFilterFactory<AuthorizeGatewayFilterFactory.Config> {
    private static final Log logger = LogFactory.getLog(AuthorizeGatewayFilterFactory.class);

    @Autowired
    private AccountFeignClient accountFeignClient;

    public AuthorizeGatewayFilterFactory() {
        super(Config.class);
        logger.info("Loaded GatewayFilterFactory [Authorize]");
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("enabled");
    }

    @Override
    public GatewayFilter apply(AuthorizeGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            if (!config.isEnabled()) {
                return chain.filter(exchange);
            }

            ServerHttpRequest request = exchange.getRequest();
            MultiValueMap<String, HttpCookie> cookies = request.getCookies();
            HttpCookie tokenCookie = cookies.getFirst("token");
            String tokenStr = tokenCookie == null ? null : tokenCookie.getValue();
            ServerHttpResponse response = exchange.getResponse();
            if (StringUtils.isEmpty(tokenStr)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            UserInfo userInfo = accountFeignClient.userInfo(tokenStr);
            if (userInfo == null || userInfo.getUserName() == null || userInfo.getIdentities() == null) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            HttpCookie jsessionid = cookies.getFirst("JSESSIONID");
            String jsessionidStr = jsessionid == null ? "" : "JSESSIONID=" + tokenStr + "; ";
            String cookie = jsessionidStr + "userName=" + userInfo.getUserName() +
                    "; identity=" + String.join(Identity.SEP, userInfo.getIdentities());
            request.mutate().headers(httpHeaders -> httpHeaders.add("cookie", cookie));
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // 控制是否开启认证
        private boolean enabled;

        public Config() {
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

}
