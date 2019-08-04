package com.cherlshall.butterfly.hbase.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

import java.io.IOException;

@Configuration
public class HBaseConfig {

    @Value("${hbase.zookeeper.quorum}")
    private String zookeeperQuorum;

    @Value("${hbase.zookeeper.property.clientPort}")
    private String clientPort;

    private org.apache.hadoop.conf.Configuration conf;

    @Bean
    public HbaseTemplate hbaseTemplate() {
        return new HbaseTemplate(getConf());
    }

    @Bean
    public Admin admin() throws IOException {
        Connection connection = ConnectionFactory.createConnection(getConf());
        return connection.getAdmin();
    }

    private org.apache.hadoop.conf.Configuration getConf() {
        if (conf == null) {
            conf = HBaseConfiguration.create();
            conf.set("hbase.zookeeper.quorum", zookeeperQuorum);
            conf.set("hbase.zookeeper.property.clientPort", clientPort);
        }
        return conf;
    }

}
