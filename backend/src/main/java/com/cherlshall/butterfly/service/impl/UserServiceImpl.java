package com.cherlshall.butterfly.service.impl;

import com.cherlshall.butterfly.cache.GeographicConfig;
import com.cherlshall.butterfly.entity.Geographic;
import com.cherlshall.butterfly.entity.User;
import com.cherlshall.butterfly.entity.UserDetail;
import com.cherlshall.butterfly.mapper.UserMapper;
import com.cherlshall.butterfly.service.UserService;
import com.cherlshall.butterfly.util.vo.ResponseVO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseVO<Map> login(String userName, String password) {
        User user = userMapper.getIdAndAuthority(userName, password);
        Map<String, Object> result = new HashMap<>();
        if (user != null && user.getAuthority() != null) {
            result.put("status", "ok");
            result.put("type", "account");
            result.put("currentAuthority", user.getAuthority());
            result.put("id", user.getId());
            return ResponseVO.ofSuccess(result);
        } else {
            result.put("status", "error");
            result.put("type", "account");
            result.put("currentAuthority", "guest");
            return ResponseVO.ofFailure(result);
        }
    }

    @Override
    public ResponseVO<UserDetail> getCurrentUser(int id) {
        UserDetail userDetail = userMapper.getUserDetailById(id);
        String provinceId = userDetail.getProvince();
        String cityId = userDetail.getCity();
        JSONObject city = GeographicConfig.getCity(provinceId, cityId);
        Map<String, Geographic> geographic = new HashMap<>();
        geographic.put("province", new Geographic(provinceId, city.getString("province")));
        geographic.put("city", new Geographic(cityId, city.getString("name")));
        userDetail.setGeographic(geographic);
        return ResponseVO.ofSuccess(userDetail);
    }

}
