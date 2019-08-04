package com.cherlshall.butterfly.user.service.impl;

import com.cherlshall.butterfly.user.dao.NoticeMapper;
import com.cherlshall.butterfly.user.entity.Notice;
import com.cherlshall.butterfly.user.service.NoticeService;
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
