package com.cherlshall.butterfly.gateway.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author hu.tengfei
 * @date 2019/8/5
 */
@Component
public class AuthorizeGatewayFilterFactory extends
        AbstractGatewayFilterFactory<AuthorizeGatewayFilterFactory.Config> {
    private static final Log logger = LogFactory.getLog(AuthorizeGatewayFilterFactory.class);

    private static final String AUTHORIZE_TOKEN = "token";
    private static final String AUTHORIZE_UID = "uid";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public AuthorizeGatewayFilterFactory() {
        super(Config.class);
        logger.info("Loaded GatewayFilterFactory [Authorize]");
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("enabled");
    }

    @Override
    public GatewayFilter apply(AuthorizeGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            if (!config.isEnabled()) {
                return chain.filter(exchange);
            }

            ServerHttpRequest request = exchange.getRequest();
            MultiValueMap<String, HttpCookie> cookies = request.getCookies();
            HttpCookie tokenCookie = cookies.getFirst(AUTHORIZE_TOKEN);
            String token = tokenCookie == null ? null : tokenCookie.getValue();
            HttpCookie uidCookie = cookies.getFirst(AUTHORIZE_UID);
            String uid = uidCookie == null ? null : uidCookie.getValue();

            if (token == null) {
                token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
            }
            if (uid == null) {
                uid = request.getQueryParams().getFirst(AUTHORIZE_UID);
            }

            ServerHttpResponse response = exchange.getResponse();
            if (StringUtils.isEmpty(token) || StringUtils.isEmpty(uid)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            String authToken = stringRedisTemplate.opsForValue().get(uid);
            if (authToken == null || !authToken.equals(token)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // 控制是否开启认证
        private boolean enabled;

        public Config() {}

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

}
