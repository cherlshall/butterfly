package com.cherlshall.butterfly.user.service;

import com.cherlshall.butterfly.user.entity.Notice;

import java.util.List;

public interface NoticeService {

    /**
     * 根据用户id查询通知信息
     */
    List<Notice> getNotices(int userId);
}
