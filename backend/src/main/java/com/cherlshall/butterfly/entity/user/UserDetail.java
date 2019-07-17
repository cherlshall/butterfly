package com.cherlshall.butterfly.entity.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDetail extends User {
    private Map<String, Geographic> geographic;
    private List<UserTag> tags;
}
