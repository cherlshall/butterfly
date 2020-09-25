package com.cherlshall.butterfly.util.auth;

import lombok.Data;

/**
 * Created by htf on 2020/9/24.
 */
@Data
public class User {
    private String userName;
    private String password;
    private String[] authority;
}
