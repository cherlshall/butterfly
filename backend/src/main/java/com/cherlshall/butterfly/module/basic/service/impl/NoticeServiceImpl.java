package com.cherlshall.butterfly.module.basic.service.impl;

import com.cherlshall.butterfly.module.basic.service.NoticeService;
import com.cherlshall.butterfly.module.basic.entity.Notice;
import com.cherlshall.butterfly.module.basic.dao.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public List<Notice> getNotices(int userId) {
        return noticeMapper.getNoticesByUserId(userId);
    }
}
