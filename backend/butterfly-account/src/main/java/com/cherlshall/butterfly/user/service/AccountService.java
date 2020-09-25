package com.cherlshall.butterfly.user.service;


import com.cherlshall.butterfly.user.entity.LoginResult;
import com.cherlshall.butterfly.util.entity.UserInfo;

public interface AccountService {

    /**
     * 根据账号密码登陆
     */
    LoginResult login(String userName, String password);

    /**
     * 登出
     */
    boolean logout(String token);

    /**
     * 获得身份
     */
    UserInfo getUserInfoByToken(String token);

}
