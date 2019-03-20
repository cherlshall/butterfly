package com.cherlshall.butterfly.service;

import com.cherlshall.butterfly.util.vo.ResponseVO;
import com.cherlshall.butterfly.entity.UserDetail;

import java.util.Map;

public interface UserService {

    /**
     * 根据账号密码登陆
     */
    ResponseVO<Map> login(String userName, String password);

    /**
     * 根据用户id获得用户详细信息
     */
    ResponseVO<UserDetail> getCurrentUser(int id);

}
