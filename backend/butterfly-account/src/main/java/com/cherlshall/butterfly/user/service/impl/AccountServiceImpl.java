package com.cherlshall.butterfly.user.service.impl;

import com.cherlshall.butterfly.user.dao.AccountDao;
import com.cherlshall.butterfly.user.entity.LoginResult;
import com.cherlshall.butterfly.user.entity.User;
import com.cherlshall.butterfly.user.service.AccountService;
import com.cherlshall.butterfly.user.util.Token;
import com.cherlshall.butterfly.util.auth.Identity;
import com.cherlshall.butterfly.util.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public LoginResult login(String userName, String password) {
        List<String> authority = accountDao.getAuthority(userName, password);
        if (authority == null) {
            return LoginResult.ofFailure();
        }
        String token = Token.create(userName, authority);
        return LoginResult.ofSuccess(String.join(Identity.SEP, authority), token);
    }

    @Override
    public boolean logout(String token) {
        return true;
    }

    @Override
    public UserInfo getUserInfoByToken(String token) {
        return Token.parse(token);
    }


}
