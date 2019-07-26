package com.cherlshall.butterfly.module.hdfs.config;

import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class HdfsConfig {

    @Value("${hdfs.path}")
    private String path;

    @Bean
    public org.apache.hadoop.conf.Configuration configuration() {
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set("fs.defaultFS", path);
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        return conf;
    }

    @Bean
    public FileSystem getFileSystem(@Autowired org.apache.hadoop.conf.Configuration conf) {
        try {
            return FileSystem.get(conf);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
