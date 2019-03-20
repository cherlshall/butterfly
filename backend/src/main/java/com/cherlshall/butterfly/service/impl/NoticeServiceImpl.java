package com.cherlshall.butterfly.service.impl;

import com.cherlshall.butterfly.entity.Notice;
import com.cherlshall.butterfly.mapper.NoticeMapper;
import com.cherlshall.butterfly.service.NoticeService;
import com.cherlshall.butterfly.util.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    NoticeMapper noticeMapper;

    @Override
    public ResponseVO<List> getNotices(int userId) {
        List<Notice> notices = noticeMapper.getNoticesByUserId(userId);
        return ResponseVO.ofSuccess(notices);
    }
}
