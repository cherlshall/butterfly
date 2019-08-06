package com.cherlshall.butterfly.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author hu.tengfei
 * @date 2019/8/6
 */
@Configuration
public class AccountConfig {

    /**
     * 过期时间（小时）
     */
    @Value("${token.timeout}")
    private int tokenTimeout;

    public int getTokenTimeout() {
        return tokenTimeout;
    }
}
