package com.cherlshall.butterfly.cache;

import com.cherlshall.butterfly.util.ioUtil.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Set;

@Configuration
public class GeographicConfig {

    public static JSONArray PROVINCE;
    public static JSONObject CITY;

    @PostConstruct
    public void init() {
        try {
            PROVINCE = FileUtil.readJsonArrayFromResources("cache/geographic/province.json");
        } catch (IOException e) {
            System.out.println("初始化省份信息失败");
        }
        try {
            CITY = FileUtil.readJsonObjectFromResources("cache/geographic/city.json");
        } catch (IOException e) {
            System.out.println("初始化城市信息失败");
        }
    }

    /**
     * 根据省份id获得省份id和name
     */
    public static JSONObject getProvince(String id) {
        if (id == null)
            return null;
        for (int i = 0; i < PROVINCE.size(); i++) {
            JSONObject item = PROVINCE.getJSONObject(i);
            if (item.getString("id").equals(id))
                return item;
        }
        return null;
    }

    /**
     * 根据城市id获得城市所属province name id
     */
    public static JSONObject getCity(String id) {
        if (id == null)
            return null;
        Set<String> keySet = CITY.keySet();
        for (String key : keySet) {
            JSONObject city = getCity(key, id);
            if (city != null)
                return city;
        }
        return null;
    }

    /**
     * 根据省份id和城市id获得城市所属province name id
     */
    public static JSONObject getCity(String provinceId, String cityId) {
        if (provinceId == null || cityId == null)
            return null;
        JSONArray cities = CITY.getJSONArray(provinceId);
        for (int i = 0; i < cities.size(); i++) {
            JSONObject city = cities.getJSONObject(i);
            if (city.getString("id").equals(cityId))
                return city;
        }
        return null;
    }
}
