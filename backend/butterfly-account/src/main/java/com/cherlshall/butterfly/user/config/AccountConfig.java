package com.cherlshall.butterfly.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by htf on 2019/8/6.
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
