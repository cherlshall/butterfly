package com.cherlshall.butterfly.module.hdfs;

import com.alibaba.fastjson.JSONObject;
import com.cherlshall.butterfly.common.vo.ParamsVO;
import com.cherlshall.butterfly.common.vo.TableData;
import com.cherlshall.butterfly.module.hdfs.service.HdfsContentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContentServiceTest {

    @Autowired
    private HdfsContentService service;

    @Test
    public void write() {
        Random random = new Random();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("filed1", "hello");
        jsonObject.put("filed2", "world");
        jsonObject.put("filed3", "hello world hello world hello world hello world");
        jsonObject.put("filed4", "bye");
        jsonObject.put("filed5", "world");
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");
        jsonObject.put("arr1", list);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            int r = random.nextInt(10);
            jsonObject.put("field" + r, r);
            jsonObject.put("id", i);
            builder.append(jsonObject.toJSONString());
            builder.append("\n");
        }
        boolean write = service.write("/test/json1.txt", builder.toString());
        System.out.println(write);
    }

    @Test
    public void readToJson() {
        TableData tableData = service.readToTable("/test/json1.txt", new ParamsVO());
        System.out.println(tableData);
    }
}
