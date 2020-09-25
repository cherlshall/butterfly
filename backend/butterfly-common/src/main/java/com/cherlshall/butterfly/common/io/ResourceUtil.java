package com.cherlshall.butterfly.common.io;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cherlshall.butterfly.util.FileUtil;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by htf on 2020/9/24.
 */
public class ResourceUtil {

    public static InputStream readStreamFromResource(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        if (resource.exists()) {
            return resource.getInputStream();
        }
        throw new IOException();
    }

    public static String readStringFromResource(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        if (resource.exists()) {
            return FileUtil.convertStreamToString(resource.getInputStream(), true);
        }
        throw new IOException();
    }

    public static JSONObject readJSONObjectFromResource(String path) throws IOException {
        return JSON.parseObject(readStringFromResource(path));
    }

    public static JSONArray readJSONArrayFromResource(String path) throws IOException {
        return JSON.parseArray(readStringFromResource(path));
    }
}
