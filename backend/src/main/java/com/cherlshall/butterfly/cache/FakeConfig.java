package com.cherlshall.butterfly.cache;

import com.cherlshall.butterfly.util.ioUtil.FileUtil;
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
            CHART_DATA = FileUtil.readJsonStringFromResources("cache/fake/chartData.json");
        } catch (IOException e) {
            System.out.println("初始化fake_chart_data失败");
        }
        try {
            TAGS = FileUtil.readJsonStringFromResources("cache/fake/tags.json");
        } catch (IOException e) {
            System.out.println("初始化fake_tags失败");
        }
        try {
            LIST = FileUtil.readJsonStringFromResources("cache/fake/list.json");
        } catch (IOException e) {
            System.out.println("初始化fake_list失败");
        }
        try {
            NOTICE = FileUtil.readJsonStringFromResources("cache/fake/notice.json");
        } catch (IOException e) {
            System.out.println("初始化fake_notice失败");
        }
    }
}
