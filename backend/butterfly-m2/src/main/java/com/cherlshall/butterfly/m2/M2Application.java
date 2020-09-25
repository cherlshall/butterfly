package com.cherlshall.butterfly.m2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by htf on 2020/1/7.
 */
@SpringBootApplication(scanBasePackages = "com.cherlshall.butterfly")
@EnableEurekaClient
@EnableTransactionManagement(order = 2)
public class M2Application {
    public static void main(String[] args) {
        SpringApplication.run(M2Application.class, args);
    }
}
