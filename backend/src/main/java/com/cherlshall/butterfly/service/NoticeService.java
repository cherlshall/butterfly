package com.cherlshall.butterfly.service;

import com.cherlshall.butterfly.util.vo.ResponseVO;

import java.util.List;

public interface NoticeService {

    /**
     * 根据用户id查询通知信息
     */
    ResponseVO<List> getNotices(int userId);
}
