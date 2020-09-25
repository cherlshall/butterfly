package com.cherlshall.butterfly.util.entity;

import lombok.Data;

import java.util.List;

/**
 * Created by htf on 2020/9/24.
 */
@Data
public class UserInfo {
    private String userName;
    private List<String> identities;
}
