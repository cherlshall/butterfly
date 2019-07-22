package com.cherlshall.butterfly.module.basic;

import com.cherlshall.butterfly.module.basic.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTests {

    @Autowired
    private UserService userService;

    @Test
    public void getUsers() {
        Map<String, Object> map = userService.login("admin", "111111");
        System.out.println(map);
    }

}
