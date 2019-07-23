package com.cherlshall.butterfly.module.elasticsearch.config;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Objects;

@Configuration
public class EsConfig {

    @Value("${elasticsearch.host}")
    private String[] hosts;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        HttpHost[] httpHosts = Arrays.stream(hosts)
                .map(this::makeHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        return new RestHighLevelClient(RestClient.builder(httpHosts));
    }

    private HttpHost makeHttpHost(String host) {
        assert StringUtils.isNotEmpty(host);
        String[] address = host.split(":");
        if (address.length > 0) {
            String ip = address[0];
            int port = address.length > 1 ? Integer.parseInt(address[1]) : 9200;
            return new HttpHost(ip, port);
        }
        return null;
    }
}
