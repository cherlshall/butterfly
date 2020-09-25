package com.cherlshall.butterfly.hdfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by htf on 2019/8/4.
 */
@SpringBootApplication(scanBasePackages = "com.cherlshall.butterfly")
@EnableEurekaClient
public class HdfsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HdfsApplication.class, args);
    }
}
