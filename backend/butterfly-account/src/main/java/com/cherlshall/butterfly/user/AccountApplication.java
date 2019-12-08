package com.cherlshall.butterfly.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author hu.tengfei
 * @date 2019/8/4
 */
@SpringBootApplication(scanBasePackages = "com.cherlshall.butterfly")
@EnableEurekaClient
public class AccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }
}
