package com.cherlshall.butterfly.user.entity;

import lombok.Data;

/**
 * Created by htf on 2020/9/25.
 */
@Data
public class LoginResult {

    private String status;
    private String type = "account";
    private String currentAuthority;
    private String token;

    public static LoginResult ofSuccess(String currentAuthority, String token) {
        LoginResult loginResult = new LoginResult();
        loginResult.setStatus("ok");
        loginResult.setCurrentAuthority(currentAuthority);
        loginResult.setToken(token);
        return loginResult;
    }

    public static LoginResult ofFailure() {
        LoginResult loginResult = new LoginResult();
        loginResult.setStatus("error");
        loginResult.setCurrentAuthority("guest");
        return loginResult;
    }
}
