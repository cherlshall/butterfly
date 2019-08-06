package com.cherlshall.butterfly.user.service.impl;

import com.cherlshall.butterfly.common.TokenUtil;
import com.cherlshall.butterfly.user.config.AccountConfig;
import com.cherlshall.butterfly.user.dao.AccountDao;
import com.cherlshall.butterfly.user.entity.User;
import com.cherlshall.butterfly.user.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountConfig config;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private StringRedisTemplate template;

    @Override
    public Map<String, Object> login(String userName, String password) {
        User user = accountDao.getIdAndAuthority(userName, password);
        Map<String, Object> result = new HashMap<>();
        if (user != null && user.getAuthority() != null) {
            String token = TokenUtil.create(user.getId());
            result.put("status", "ok");
            result.put("type", "account");
            result.put("currentAuthority", user.getAuthority());
            result.put("id", user.getId());
            result.put("token", token);
            template.opsForValue().set(user.getId().toString(), token, config.getTokenTimeout(), TimeUnit.HOURS);
            return result;
        } else {
            result.put("status", "error");
            result.put("type", "account");
            result.put("currentAuthority", "guest");
            return result;
        }
    }

    @Override
    public boolean logout(int uid) {
        Boolean delete = template.delete(uid + "");
        if (delete == null) {
            return false;
        }
        return delete;
    }

}
