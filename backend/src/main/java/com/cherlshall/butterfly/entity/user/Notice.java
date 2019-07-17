package com.cherlshall.butterfly.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Notice {
    private Integer id;
    @JsonIgnore
    private Integer userId;
    private String type;
    private Integer datetime;
    private String title;
    private Boolean read;
    private Boolean clickClose;
    private String avatar;
    private String description;
    private String extra;
    private String status;
}
