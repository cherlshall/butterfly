package com.cherlshall.butterfly.user.service;


import java.util.Map;

public interface AccountService {

    /**
     * 根据账号密码登陆
     */
    Map<String, Object> login(String userName, String password);

    /**
     * 登出
     */
    boolean logout(int uid);

}
