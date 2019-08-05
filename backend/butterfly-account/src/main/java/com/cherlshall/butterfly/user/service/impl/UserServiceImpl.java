package com.cherlshall.butterfly.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cherlshall.butterfly.user.cache.GeographicConfig;
import com.cherlshall.butterfly.user.dao.AccountDao;
import com.cherlshall.butterfly.user.dao.NoticeDao;
import com.cherlshall.butterfly.user.entity.Geographic;
import com.cherlshall.butterfly.user.entity.Notice;
import com.cherlshall.butterfly.user.entity.UserDetail;
import com.cherlshall.butterfly.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private NoticeDao noticeDao;
    @Autowired
    private AccountDao accountDao;

    @Override
    public List<Notice> getNotices(int userId) {
        return noticeDao.getNoticesByUserId(userId);
    }

    @Override
    public UserDetail getCurrentUser(int id) {
        UserDetail userDetail = accountDao.getUserDetailById(id);
        String provinceId = userDetail.getProvince();
        String cityId = userDetail.getCity();
        JSONObject city = GeographicConfig.getCity(provinceId, cityId);
        Map<String, Geographic> geographic = new HashMap<>();
        geographic.put("province", new Geographic(provinceId, city.getString("province")));
        geographic.put("city", new Geographic(cityId, city.getString("name")));
        userDetail.setGeographic(geographic);
        return userDetail;
    }
}
