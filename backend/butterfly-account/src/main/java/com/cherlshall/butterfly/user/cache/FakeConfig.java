package com.cherlshall.butterfly.user.cache;

import com.cherlshall.butterfly.common.io.ResourceUtil;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FakeConfig {
    public static String CHART_DATA;
    public static String TAGS;
    public static String LIST;
    public static String NOTICE;

    @PostConstruct
    public void init() {
        try {
            CHART_DATA = ResourceUtil.readStringFromResource("cache/fake/chartData.json");
        } catch (IOException e) {
            System.out.println("初始化fake_chart_data失败");
        }
        try {
            TAGS = ResourceUtil.readStringFromResource("cache/fake/tags.json");
        } catch (IOException e) {
            System.out.println("初始化fake_tags失败");
        }
        try {
            LIST = ResourceUtil.readStringFromResource("cache/fake/list.json");
        } catch (IOException e) {
            System.out.println("初始化fake_list失败");
        }
        try {
            NOTICE = ResourceUtil.readStringFromResource("cache/fake/notice.json");
        } catch (IOException e) {
            System.out.println("初始化fake_notice失败");
        }
    }
}
