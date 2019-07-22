package com.cherlshall.butterfly.module.basic.service;

import com.cherlshall.butterfly.module.basic.entity.Notice;

import java.util.List;

public interface NoticeService {

    /**
     * 根据用户id查询通知信息
     */
    List<Notice> getNotices(int userId);
}
