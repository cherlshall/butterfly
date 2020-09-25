package com.cherlshall.butterfly.user.dao;

import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AccountDao {

    /**
     * 根据用户名和密码查询用户权限 一般用于登陆
     */
    List<String> getAuthority(@Param("userName") String userName, @Param("password") String password);

}
