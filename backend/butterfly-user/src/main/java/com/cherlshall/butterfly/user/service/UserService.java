package com.cherlshall.butterfly.user.service;

import com.cherlshall.butterfly.user.entity.UserDetail;

import java.util.Map;

public interface UserService {

    /**
     * 根据账号密码登陆
     */
    Map<String, Object> login(String userName, String password);

    /**
     * 根据用户id获得用户详细信息
     */
    UserDetail getCurrentUser(int id);

}
