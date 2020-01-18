package com.cherlshall.butterfly.m2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author hu.tengfei
 * @date 2020/1/7
 */
@SpringBootApplication(scanBasePackages = "com.cherlshall.butterfly")
@EnableEurekaClient
public class M2Application {
    public static void main(String[] args) {
        SpringApplication.run(M2Application.class, args);
    }
}
