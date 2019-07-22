package com.cherlshall.butterfly.module.basic.service.impl;

import com.cherlshall.butterfly.module.basic.service.NoticeService;
import com.cherlshall.butterfly.module.basic.entity.Notice;
import com.cherlshall.butterfly.module.basic.dao.NoticeMapper;
import com.cherlshall.butterfly.common.vo.ResponseVO;
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
