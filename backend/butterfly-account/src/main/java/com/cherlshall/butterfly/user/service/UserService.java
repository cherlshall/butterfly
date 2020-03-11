package com.cherlshall.butterfly.user.service;

import com.cherlshall.butterfly.user.entity.Notice;
import com.cherlshall.butterfly.user.entity.UserDetail;

import java.util.List;

public interface UserService {

    /**
     * 根据用户id查询通知信息
     */
    List<Notice> getNotices(Integer userId);

    /**
     * 根据用户id获得用户详细信息
     */
    UserDetail getCurrentUser(Integer id);
}
