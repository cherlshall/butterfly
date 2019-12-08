package com.cherlshall.butterfly.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author hu.tengfei
 * @date 2019/8/4
 */
@SpringBootApplication(scanBasePackages = "com.cherlshall.butterfly")
@EnableEurekaClient
public class ElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class, args);
    }
}
