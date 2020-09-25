package com.cherlshall.butterfly.util.auth;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by htf on 2020/9/24.
 */
public class Crowd {

    public static List<String> defaultUser(String userName, String password) {
        if ("visitor".equals(userName) && "111111".equals(password)) {
            return Collections.singletonList("nca_visitor");
        }
        if ("super".equals(userName) && "super123@#".equals(password)) {
            return Collections.singletonList("super");
        }
        return null;
    }

    public static List<String> groupsToAuth(List<String> groups) {
        return groups.stream().filter(v -> v.startsWith("m2-"))
                .map(v -> v.substring(3).replace("-", "_")).collect(Collectors.toList());
    }
}
