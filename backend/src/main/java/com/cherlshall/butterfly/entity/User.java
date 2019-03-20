package com.cherlshall.butterfly.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class User {
    private Integer id;
    private String userName;
    @JsonIgnore
    private String password;
    private Integer status;
    private String authority;
    private String name;
    private String phone;
    private String email;
    private String avatar;
    private String title;
    private String signature;
    private String country;
    @JsonIgnore
    private String province;
    @JsonIgnore
    private String city;
    private String address;
    private String group;
    private Integer notifyCount;
    private Integer unreadCount;
}
